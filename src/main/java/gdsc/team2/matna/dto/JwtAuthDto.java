package gdsc.team2.matna.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthDto {

    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthDto(String accessToken) {
        this.accessToken = accessToken;
    }

}
