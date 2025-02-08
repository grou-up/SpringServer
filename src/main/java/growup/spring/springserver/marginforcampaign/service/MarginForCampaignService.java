package growup.spring.springserver.marginforcampaign.service;

import growup.spring.springserver.campaign.domain.Campaign;
import growup.spring.springserver.campaign.service.CampaignService;
import growup.spring.springserver.exception.marginforcampaign.MarginForCampaignIdNotFoundException;
import growup.spring.springserver.exception.marginforcampaign.MarginForCampaignProductNameNotFoundException;
import growup.spring.springserver.marginforcampaign.TypeChangeMarginForCampaign;
import growup.spring.springserver.marginforcampaign.domain.MarginForCampaign;
import growup.spring.springserver.marginforcampaign.dto.MarginForCampaignResDto;
import growup.spring.springserver.marginforcampaign.dto.MfcDto;
import growup.spring.springserver.marginforcampaign.dto.MfcRequestDtos;
import growup.spring.springserver.marginforcampaign.dto.MfcValidationResponseDto;
import growup.spring.springserver.marginforcampaign.repository.MarginForCampaignRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class MarginForCampaignService {
    private final MarginForCampaignRepository marginForCampaignRepository;
    private final CampaignService campaignService;

    public List<MarginForCampaignResDto> marginForCampaignByCampaignId(Long id) {
        List<MarginForCampaign> marginForCampaigns = marginForCampaignRepository.MarginForCampaignByCampaignId(id);

        ArrayList<MarginForCampaignResDto> datas = new ArrayList<>();
        for (MarginForCampaign data : marginForCampaigns) {
            datas.add(
                    TypeChangeMarginForCampaign.EntityToDto(data)
            );
        }
        return datas;
    }

    public MfcValidationResponseDto searchMarginForCampaignProductName(String email, MfcRequestDtos requestDtos) {
        Campaign campaign = campaignService.getCampaign(requestDtos.getCampaignId());

        return validateProducts(requestDtos.getData(), email, campaign);
    }

    public void deleteMarginForCampaign(Long id) {
        if (!marginForCampaignRepository.existsById(id)) {
            throw new MarginForCampaignIdNotFoundException();
        }
        // 존재할 경우 삭제
        marginForCampaignRepository.deleteById(id);
    }

    private MfcValidationResponseDto validateProducts(List<MfcDto> datas, String email, Campaign campaign) {
        List<MarginForCampaign> successfulProducts = new ArrayList<>();
        List<String> failedProductNames = new ArrayList<>();
        Set<String> productNamesSet = new HashSet<>(); // 중복 체크를 위한 Set

        for (MfcDto data : datas) {
            if (productNamesSet.contains(data.getMfcProductName())) {
                // 이미 처리한 상품명이면 건너뜁니다.
                failedProductNames.add(data.getMfcProductName());
                continue;
            }
            productNamesSet.add(data.getMfcProductName()); // Set에 추가

            try {
                checkDuplicate(data,campaign);
                checkProductName(email, data, campaign);
                failedProductNames.add(data.getMfcProductName());
            } catch (MarginForCampaignProductNameNotFoundException exception) {
                // 상품이 존재하지 않을 경우, 추가 또는 업데이트 처리
                handleProduct(data, campaign, successfulProducts);
            }
        }

        saveSuccessfulProducts(successfulProducts);

        return createValidationResponse(datas.size(), successfulProducts.size(), failedProductNames);
    }

    // 자기 캠페인에서 중복 체크 -> 존재하면 새로 만드는게 아닌 업데이트를 해야함
    private void checkDuplicate(MfcDto data, Campaign campaign) {
        if (marginForCampaignRepository.existsByCampaignAndMfcProductName(campaign, data.getMfcProductName())) {
            throw new MarginForCampaignProductNameNotFoundException();
        }
    }

    private void handleProduct(MfcDto data, Campaign campaign, List<MarginForCampaign> successfulProducts) {
//         상품명으로 찾을경우 상품명을 수정하였을때 새롭게 같은이름으로 db가 들어감
//        Optional<MarginForCampaign> existingProduct = marginForCampaignRepository.findByCampaignAndMfcProductName(data.getMfcProductName(), campaign.getCampaignId());

//        캠페인 명과 MfcId 로 찾음
        Optional<MarginForCampaign> existingProduct = marginForCampaignRepository.findByCampaignAndMfcId(data.getMfcId(), campaign.getCampaignId());
        if (existingProduct.isPresent()) {
            // 기존 상품 업데이트
            MarginForCampaign updateMarginForCampaign = existingProduct.get();
            updateMarginForCampaign.updateExistingProduct(data);
            successfulProducts.add(updateMarginForCampaign);
        } else {
            // 새로운 상품 생성
            MarginForCampaign newMargin = createMarginFromDto(data, campaign);
            successfulProducts.add(newMargin);
        }
    }


    private MarginForCampaign createMarginFromDto(MfcDto data, Campaign campaign) {
        return TypeChangeMarginForCampaign.createDtoToMargin(data, campaign);
    }

    private void saveSuccessfulProducts(List<MarginForCampaign> successfulProducts) {
        marginForCampaignRepository.saveAll(successfulProducts);

    }

    private MfcValidationResponseDto createValidationResponse(int requestCount, int successCount, List<String> failedProductNames) {
        return TypeChangeMarginForCampaign.validationResponse(requestCount, successCount, failedProductNames);
    }
    /*
    ** TODO
    *   email, data, camapign
    *   현재 캠페인을 제외하고 중복되는 이름이 있는지 확인
     */
    private MarginForCampaign checkProductName(String email, MfcDto data, Campaign campaign) {
        return marginForCampaignRepository.findByEmailAndMfcProductNameExcludingCampaign(email, data.getMfcProductName(), campaign.getCampaignId())
                .orElseThrow(MarginForCampaignProductNameNotFoundException::new);
    }
}