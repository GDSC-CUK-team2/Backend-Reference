package gdsc.team2.matna.controller;

import gdsc.team2.matna.dto.CurrentUserGetDto;
import gdsc.team2.matna.entity.User;
import gdsc.team2.matna.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "유저")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "회원 정보 조회")
    public CurrentUserGetDto getCurrentUser(@AuthenticationPrincipal User user){
        log.info("현재 회원 정보 조회 start");
        CurrentUserGetDto currentUserGetDto = userService.getCurrentUser(user);
        log.info("현재 회원 정보 조회 end");
        return currentUserGetDto;
    }
}
