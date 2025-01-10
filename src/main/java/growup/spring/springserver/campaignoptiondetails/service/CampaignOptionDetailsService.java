package growup.spring.springserver.campaignoptiondetails.service;

import growup.spring.springserver.campaignoptiondetails.TypeChangeCampaignOptionDetails;
import growup.spring.springserver.campaignoptiondetails.domain.CampaignOptionDetails;
import growup.spring.springserver.campaignoptiondetails.dto.CampaignOptionDetailsResponseDto;
import growup.spring.springserver.campaignoptiondetails.repository.CampaignOptionDetailsRepository;
import growup.spring.springserver.exception.InvalidDateFormatException;
import growup.spring.springserver.exception.campaignoptiondetails.CampaignOptionDataNotFoundException;
import growup.spring.springserver.exception.campaignoptiondetails.CampaignOptionNotFoundException;
import growup.spring.springserver.execution.repository.ExecutionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CampaignOptionDetailsService {
    private final CampaignOptionDetailsRepository campaignOptionDetailsRepository;
    private final ExecutionRepository executionRepository;

    public List<CampaignOptionDetails> getRawCampaignDetails(LocalDate start, LocalDate end, long id) {
        if (start.isAfter(end)) {
            throw new InvalidDateFormatException();
        }

        List<Long> byCampaignCampaignIds = executionRepository.findExecutionIdsByCampaignId(id);
        if (byCampaignCampaignIds.isEmpty()) {
            throw new CampaignOptionNotFoundException();
        }
        List<CampaignOptionDetails> byExecutionIdsAndDateRange = campaignOptionDetailsRepository.findByExecutionIdsAndDateRange(
                byCampaignCampaignIds, start, end);
        if (byExecutionIdsAndDateRange.isEmpty()) {
            throw new CampaignOptionDataNotFoundException();
        }
        return byExecutionIdsAndDateRange;
    }
    public List<CampaignOptionDetailsResponseDto> getCampaignDetailsByCampaignsIds(LocalDate start, LocalDate end, long id) {
        List<CampaignOptionDetails> data = getRawCampaignDetails(start, end, id);

        HashMap<Long, CampaignOptionDetailsResponseDto> map = new HashMap<>();
        for(CampaignOptionDetails details : data){

            if(map.containsKey(details.getExecution().getExeId())){
                map.get(details.getExecution().getExeId()).update(details);
                continue;
            }
            map.put(details.getExecution().getExeId(), TypeChangeCampaignOptionDetails.entityToResponseDto(details));
        }

        List<CampaignOptionDetailsResponseDto> campaignOptionDetailsResponseDtos = new ArrayList<>();
        for(Long key : map.keySet()){
            campaignOptionDetailsResponseDtos.add(map.get(key));
        }
        return campaignOptionDetailsResponseDtos;
    }
}