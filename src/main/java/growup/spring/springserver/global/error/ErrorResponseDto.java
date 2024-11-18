package growup.spring.springserver.global.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ErrorResponseDto {
    // Private constructor to prevent instantiation
    // 기본 생성자가 자동으로 생성되는 것을 막는다.
    private ErrorResponseDto() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ResponseEntity<Map<String, Object>> makeMessage(HttpStatus status, String Message) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("status", Integer.toString(status.value()));
        body.put("status_message", status.getReasonPhrase());
        body.put("error_message", Message);

        return new ResponseEntity<>(body, status);
    }

//    security error response
    public static void writeErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException, IOException {
        Map<String, Object> body = new HashMap<>();
        body.put("status", Integer.toString(status.value()));
        body.put("status_message", status.getReasonPhrase());
        body.put("error_message", message);

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), body);
    }
}
