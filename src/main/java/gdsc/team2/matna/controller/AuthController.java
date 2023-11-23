package gdsc.team2.matna.controller;

import gdsc.team2.matna.dto.JwtAuthDto;
import gdsc.team2.matna.dto.LoginDto;
import gdsc.team2.matna.dto.RegisterDto;
import gdsc.team2.matna.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "회원가입/로그인")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public JwtAuthDto login(@Valid @RequestBody LoginDto loginDto) {
        log.info("로그인 stard");
        String token = authService.login(loginDto);
        log.info("로그인 end");
        return new JwtAuthDto(token);
    }

//
//    @PostMapping("/login")
//    public void login(@RequestBody LoginDto LoginDto) {
//        String token = authService.login(LoginDto);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("Authorization", token);
//    }

    @PostMapping( "/signup")
    @Operation(summary = "회원 가입")
    public void register (@Valid @RequestBody RegisterDto registerDto) {
        log.info("회원가입 start");
        authService.register(registerDto);
        log.info("회원가입 end");
     }
}
