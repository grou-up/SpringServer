package growup.spring.springserver.keyword.service;

import growup.spring.springserver.exclusionKeyword.dto.ExclusionKeywordResponseDto;
import growup.spring.springserver.exclusionKeyword.service.ExclusionKeywordService;
import growup.spring.springserver.keyword.domain.Keyword;
import growup.spring.springserver.keyword.dto.KeywordResponseDto;
import growup.spring.springserver.keyword.TypeChangeKeyword;
import growup.spring.springserver.keyword.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Service
public class KeywordService {
    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private ExclusionKeywordService exclusionKeywordService;
    public List<KeywordResponseDto> getKeywordsByCampaignId(String start, String end , Long campaignId){
        LocalDate startDate;
        LocalDate endDate;
        try{
            startDate = LocalDate.parse(start, DateTimeFormatter.ISO_DATE);
            endDate = LocalDate.parse(end, DateTimeFormatter.ISO_DATE);
            if(startDate.isAfter(endDate)) throw new DataFormatException();
        }catch (DateTimeParseException | DataFormatException e){
            throw new IllegalArgumentException("날짜 형식이 이상합니다");
        }
        List<Keyword> data = keywordRepository.findAllByDateANDCampaign(startDate,endDate,campaignId);
        if(data.isEmpty()) throw new NullPointerException("해당 캠페인의 키워드가 없습니다.");
        HashMap<String,KeywordResponseDto> map = new HashMap<>();
        for(Keyword keyword : data){
            if(keyword.getKeyKeyword() == null || keyword.getKeyKeyword().isEmpty()){
                continue;
            }
            if(map.containsKey(keyword.getKeyKeyword())){
                map.get(keyword.getKeyKeyword()).update(keyword);
                continue;
            }
            map.put(keyword.getKeyKeyword(),TypeChangeKeyword.entityToResponseDto(keyword));
        }
        List<KeywordResponseDto> keywordResponseDtos = new ArrayList<>();
        Set<String> exclusions = getExclusionKeywordToSet(campaignId);
        for(String key : map.keySet()){
            if(!exclusions.isEmpty() &&exclusions.contains(key)) map.get(key).setKeyExcludeFlag(true);
            keywordResponseDtos.add(map.get(key));
        }
        return keywordResponseDtos;
    }

    public Set<String> getExclusionKeywordToSet(Long campaignId){
        List<ExclusionKeywordResponseDto> exclusionKeywordResponseDtos;
        try {
            exclusionKeywordResponseDtos = exclusionKeywordService.getExclusionKeywords(campaignId);
        }catch (IllegalArgumentException e){
            exclusionKeywordResponseDtos = List.of();
        }
        return exclusionKeywordResponseDtos.stream()
                .map(ExclusionKeywordResponseDto::getExclusionKeyword)
                .collect(Collectors.toSet());
    }
}
