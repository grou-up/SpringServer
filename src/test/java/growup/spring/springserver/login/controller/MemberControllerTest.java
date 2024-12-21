//package growup.spring.springserver.login.controller;
//
//import growup.spring.springserver.login.domain.Member;
//import growup.spring.springserver.login.repository.MemberRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.junit.jupiter.api.MethodOrderer;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//@TestMethodOrder(MethodOrderer.DisplayName.class)
//public class MemberControllerTest {
//
//    private static final Logger log = LoggerFactory.getLogger(MemberControllerTest.class);
//    @Mock
//    private MemberRepository memberRepository;
//
//    public MemberControllerTest() {
//        MockitoAnnotations.openMocks(this); // Mock 초기화
//    }
//
//    @DisplayName("멤버 객체 가져오기")
//    @Test
//    void 멤버_객체_가져오기() {
//
//        // Given: Mock 리포지토리에 저장된 멤버 객체 준비
//        String email = "fa7273@naver.com";
//
//        when(memberRepository.findByEmail(email)).thenReturn(java.util.Optional.of(member));
//
//        // When: 리포지토리에서 이메일로 멤버 객체를 가져옴
//        Member result = memberRepository.findByEmail(email).orElse(null);
//
//        // Then: 결과 검증
//        assertThat(result).isNotNull();
//        assertThat(result.getEmail()).isEqualTo(email);
//        assertThat(result.getPassword()).isEqualTo("thdqhtjr123!!@");
//        // Verify: 리포지토리 메서드 호출 검증
//        verify(memberRepository, times(1)).findByEmail(email);
//    }
//
//
//}
