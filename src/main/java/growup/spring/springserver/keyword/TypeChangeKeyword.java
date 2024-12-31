package growup.spring.springserver.keyword;

import growup.spring.springserver.keyword.dto.KeywordResponseDto;
import growup.spring.springserver.keyword.domain.Keyword;

public class TypeChangeKeyword {
    public static KeywordResponseDto entityToResponseDto(Keyword keyword){
        return KeywordResponseDto.builder()
                .keyAdcost(keyword.getKeyAdcost())
                .keyAdsales(keyword.getKeyAdsales())
                .keyClickRate((double) (Math.round(keyword.getKeyClickRate()*100)/100))
                .keyKeyword(keyword.getKeyKeyword())
                .keyClicks(keyword.getKeyClicks())
                .keyCpc(keyword.getKeyCpc())
                .keyCvr((double) (Math.round(keyword.getKeyCvr()*100)/100))
                .keyImpressions(keyword.getKeyImpressions())
                .keyRoas((double) (Math.round(keyword.getKeyRoas()*100)/100))
                .keyDate(keyword.getKeyDate())
                .keyExcludeFlag(false)
                .keySearchType(keyword.getKeySearchType())
                .keyTotalSales(keyword.getKeyTotalSales())
                .build();
    }
}
