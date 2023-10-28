package gdsc.team2.matna.dto;

import gdsc.team2.matna.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CurrentUserGetDto {

    Long userId;
    String email;
    String nickname;
    // TODO: List -> String
    List<String> role;

    public static CurrentUserGetDto toDto(User user) {
        return new CurrentUserGetDto(user.getId(), user.getEmail(), user.getNickname(), user.getRoles());
    }
}
