package gdsc.team2.matna.dto;

import gdsc.team2.matna.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserGetDto {

    String nickname;
    String profileUrl;
    public static UserGetDto toDto (User user){
        return new UserGetDto(user.getNickname(), user.getProfileUrl());
    }
}
