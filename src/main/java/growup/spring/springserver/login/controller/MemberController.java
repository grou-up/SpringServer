package growup.spring.springserver.login.controller;

import growup.spring.springserver.global.common.CommonResponse;
import growup.spring.springserver.login.domain.Member;
import growup.spring.springserver.login.dto.request.LoginSignInReqDto;
import growup.spring.springserver.login.dto.request.LoginSignUpReqDto;
import growup.spring.springserver.login.dto.response.LoginResDto;
import growup.spring.springserver.login.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/test1")
    public String test() {
        return "test success";
    }

    @GetMapping("/Authtest")
    @PreAuthorize("hasRole('SILVER')")
    public String Authtest(@AuthenticationPrincipal UserDetails userDetails) {
        log.info(userDetails.getUsername());
        log.info(userDetails.getAuthorities().toString());
        // 추가 로직 수행
        return "ok";
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<String>> signup(@Valid @RequestBody LoginSignUpReqDto loginCreateReqDto, BindingResult bindingResult) throws BindException {
        log.info("start signup");
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        memberService.signup(loginCreateReqDto);

        return new ResponseEntity<>(CommonResponse
                .<String>builder("member successfully created")
                .result(loginCreateReqDto.email())
                .build(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResDto>> login(@Valid @RequestBody LoginSignInReqDto loginSignInReqDto, BindingResult bindingResult) throws BindException {
        log.info("start login check");
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        LoginResDto loginresDto = memberService.login(loginSignInReqDto);

        return new ResponseEntity<>(CommonResponse
                .<LoginResDto>builder("login success")
                .result(loginresDto)
                .build(), HttpStatus.CREATED);
    }
}