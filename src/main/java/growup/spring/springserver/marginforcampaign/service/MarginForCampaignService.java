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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
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
        for (MfcDto data : datas) {
            try {
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

    private void handleProduct(MfcDto data, Campaign campaign, List<MarginForCampaign> successfulProducts) {
        Optional<MarginForCampaign> existingProduct = marginForCampaignRepository.findByCampaignAndMfcProductName(data.getMfcProductName(), campaign.getCampaignId());
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