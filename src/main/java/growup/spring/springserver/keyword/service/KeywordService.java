package growup.spring.springserver.keyword.service;

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
import java.util.zip.DataFormatException;

@Service
public class KeywordService {
    @Autowired
    private KeywordRepository keywordRepository;

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
        List<Keyword> data = keywordRepository.findAllByDateANDFlag(startDate,endDate,campaignId);
        if(data.isEmpty()) throw new NullPointerException("해당 캠페인의 키워드가 없습니다.");
        HashMap<String,KeywordResponseDto> map = new HashMap<>();
        for(Keyword keyword : data){
            if(keyword.getKeyKeyword() == null || keyword.getKeyKeyword().equals("nan")){
                continue;
            }
            if(map.containsKey(keyword.getKeyKeyword())){
                map.get(keyword.getKeyKeyword()).update(keyword);
                continue;
            }
            map.put(keyword.getKeyKeyword(),TypeChangeKeyword.entityToResponseDto(keyword));
        }
        List<KeywordResponseDto> keywordResponseDtos = new ArrayList<>();
        for(String key : map.keySet()){
            keywordResponseDtos.add(map.get(key));
        }
        return keywordResponseDtos;
    }
}