package gdsc.team2.matna.service;

import gdsc.team2.matna.dto.CurrentUserGetDto;
import gdsc.team2.matna.entity.User;
import gdsc.team2.matna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public CurrentUserGetDto getCurrentUser(User user) {
        userRepository.findById(user.getId()).orElseThrow(()-> new AccessDeniedException("권한이 없습니다."));
        return CurrentUserGetDto.toDto(user);
    }
}
