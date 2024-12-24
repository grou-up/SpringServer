package growup.spring.springserver.keyword;

import growup.spring.springserver.keyword.dto.KeywordResponseDto;
import growup.spring.springserver.keyword.domain.Keyword;

public class TypeChangeKeyword {
    public static KeywordResponseDto entityToResponseDto(Keyword keyword){
        return KeywordResponseDto.builder()
                .keyAdcost(keyword.getKeyAdcost())
                .keyAdsales(keyword.getKeyAdsales())
                .keyClickRate(keyword.getKeyClickRate())
                .keyKeyword(keyword.getKeyKeyword())
                .keyClicks(keyword.getKeyClicks())
                .keyCpc(keyword.getKeyCpc())
                .keyCvr(keyword.getKeyCvr())
                .keyImpressions(keyword.getKeyImpressions())
                .keyRoas(keyword.getKeyRoas())
                .keyDate(keyword.getKeyDate())
                .keyExcludeFlag(keyword.getKeyExcludeFlag())
                .keySearchType(keyword.getKeySearchType())
                .keyTotalSales(keyword.getKeyTotalSales())
                .build();
    }
}
