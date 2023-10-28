package gdsc.team2.matna.service;

import gdsc.team2.matna.dto.LoginDto;
import gdsc.team2.matna.dto.RegisterDto;
import gdsc.team2.matna.entity.User;
import gdsc.team2.matna.exception.UserEmailAlreadyExistException;
import gdsc.team2.matna.repository.UserRepository;
import gdsc.team2.matna.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void register(RegisterDto registerDto) {
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new UserEmailAlreadyExistException("이메일이 이미 존재합니다.");
        }
        User user = new User(registerDto.getEmail(), passwordEncoder.encode(registerDto.getPassword()), registerDto.getNickname());
        userRepository.save(user);
    }

    @Transactional
    public String login(LoginDto loginDto) {
        userRepository.findByEmail(loginDto.getEmail())
                .filter(it -> passwordEncoder.matches(loginDto.getPassword(), it.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }
}
