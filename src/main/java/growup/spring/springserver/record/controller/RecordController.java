package growup.spring.springserver.record.controller;

import growup.spring.springserver.record.service.RecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "데이터 1 관리", description = "데이터 관련 API")
@RestController
@Slf4j
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;
}