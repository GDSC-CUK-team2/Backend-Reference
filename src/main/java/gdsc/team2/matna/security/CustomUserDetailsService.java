package gdsc.team2.matna.security;
import gdsc.team2.matna.entity.User;
import gdsc.team2.matna.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
          User user = userRepository.findByEmail(username)
                 .orElseThrow(() ->
                         new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));
          return user;
    }
}
