package growup.spring.springserver;

import growup.spring.springserver.exclusionKeyword.dto.ExclusionKeywordRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ExclusionDtoValidTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    public static void close() {
        factory.close();
    }

    @DisplayName("exclusionKeywordRequestDto : keyword is null")
    @Test
    void 빈문자열_유효성_실패_테스트() {
        // given
        ExclusionKeywordRequestDto signInDto = ExclusionKeywordRequestDto.builder().exclusionKeyword(null).campaignId(1L).build();

        // when
        Set<ConstraintViolation<ExclusionKeywordRequestDto>> violations = validator.validate(signInDto); // 유효하지 않은 경우 violations 값을 가지고 있다.

        // then
        assertThat(violations).isNotEmpty();
        violations
                .forEach(error -> {
                    assertThat(error.getMessage()).isEqualTo("제외 키워드를 입력해주세요");
                });
    }
}
