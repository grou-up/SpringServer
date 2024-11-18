package growup.spring.springserver.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@ToString
public class CommonResponse<T> implements Serializable {
    private final String message;
    private final T result;


    public static class ResponseBuilder<T> {

        private final String message;
        private T result;

        private ResponseBuilder(String message) {
            this.message = message;
        }

        public ResponseBuilder<T> result(T value) {
            result = value;
            return this;
        }

        public CommonResponse<T> build() {
            return new CommonResponse<>(this);
        }
    }

    public static <T> ResponseBuilder<T> builder(String message) {
        return new ResponseBuilder<>(message);
    }

    private CommonResponse(ResponseBuilder<T> responseBuilder) {
        message = responseBuilder.message;
        result = responseBuilder.result;
    }
}
