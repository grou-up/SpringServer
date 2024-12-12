package growup.spring.springserver.login.service;

import growup.spring.springserver.global.config.JwtTokenProvider;
import growup.spring.springserver.global.domain.TypeChange;
import growup.spring.springserver.login.domain.Member;
import growup.spring.springserver.login.dto.request.LoginSignInReqDto;
import growup.spring.springserver.login.dto.request.LoginSignUpReqDto;
import growup.spring.springserver.login.dto.response.LoginResDto;
import growup.spring.springserver.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final TypeChange typeChange;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public void signup(LoginSignUpReqDto loginCreateReqDto) {
        memberRepository.findByEmail(loginCreateReqDto.email())
                .ifPresent(email -> {
                    throw new IllegalArgumentException("already member exist");
                });
        String encode = passwordEncoder.encode(loginCreateReqDto.password());
        Member member = typeChange.memberCreateDtoToMember(loginCreateReqDto, encode);

        memberRepository.save(member);

    }

    public LoginResDto login(LoginSignInReqDto loginSignInReqDto) {
        log.info("Check account validity");
        Member findmember = memberRepository.findByEmail(loginSignInReqDto.email())
                .filter(it -> {
                    if (it.getPassword() == null || it.getPassword().isEmpty()) {
                        // 비밀번호가 없는 경우, 카카오 계정으로 가입된 사용자
                        throw new IllegalArgumentException("This account is registered with Kakao login.");
                    }
                    return passwordEncoder.matches(loginSignInReqDto.password(), it.getPassword());
                })
                .orElseThrow(() -> new IllegalArgumentException("Account cannot be found"));

        String memberSpecification = String.format("%s:%s:%s", findmember.getEmail(), findmember.getName(), findmember.getRole());

        log.info("member :{}",memberSpecification);
        String accessToken = jwtTokenProvider.createAccessToken(memberSpecification);

        log.info("ACCess token success");
        return typeChange.memberToLoginResDto(findmember, accessToken);
    }
}
