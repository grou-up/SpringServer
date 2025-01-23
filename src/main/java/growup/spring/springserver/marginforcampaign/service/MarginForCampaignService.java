package growup.spring.springserver.marginforcampaign.service;

import growup.spring.springserver.marginforcampaign.TypeChangeMarginForCampaign;
import growup.spring.springserver.marginforcampaign.domain.MarginForCampaign;
import growup.spring.springserver.marginforcampaign.dto.MarginForCampaignResDto;
import growup.spring.springserver.marginforcampaign.repository.MarginForCampaignRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MarginForCampaignService {
    private final MarginForCampaignRepository marginForCampaignRepository;

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
}