package gdsc.team2.matna.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiration.milliseconds}")
    private long jwtExpirationDate;
    private final CustomUserDetailsService customUserDetailsService;
    private Key key;

    //Secret Key를 Base64로 인코딩
    @PostConstruct
    protected void init() {

        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰 생성
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", authentication.getAuthorities());

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    // 토큰에서 username 추출(email)
    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        return username;
    }

    // jwt 토큰 검증
    public boolean validateToken(String token){
        if(token!=null) {
            try {
                log.info("=============토큰검증 시작");
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parse(token);
                log.info("=============토큰검증 성공");
                return true;
            } catch (MalformedJwtException ex) {
                log.error("잘못된 토큰");
            } catch (SignatureException ex) {
                log.error("유효하지 않은 JWT signature");
            } catch (ExpiredJwtException ex) {
                log.error("만료된 토큰");
            } catch (UnsupportedJwtException ex) {
                log.error("지원하지 않는 토큰");
            } catch (IllegalArgumentException ex) {
                log.error("유효하지 않은 토큰");
            }
            return false;
        }
        else{
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(this.getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }
}
