package gdsc.team2.matna.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
public class ErrorResponse {

    private int code;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final HashMap<String, Object> detail = new HashMap<>();

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;

    }

    public ErrorResponse(int code, String message, Map<String, Object> detail) {
        this.code = code;
        this.message = message;
        if (!ObjectUtils.isEmpty(detail)) {
            this.detail.putAll(detail);
        }
    }
}
