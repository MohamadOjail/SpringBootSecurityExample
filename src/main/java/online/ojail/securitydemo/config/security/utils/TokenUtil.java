package online.ojail.securitydemo.config.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.ojail.securitydemo.service.PersonServiceImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mo. Ojail
 * created: 2022-09-07
 */

@Component @RequiredArgsConstructor @Slf4j
public class TokenUtil {
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final PersonServiceImpl personService;

    public Map<String, String> generateTokens(String requestUrl, User user){

        return Map.of("accessToken", generateAccessToken(user, requestUrl),
                "refreshToken", generateRefreshToken(user, requestUrl));
    }

    public DecodedJWT getDecodedToken(String authHeader){
        String accessToken = authHeader.substring("Bearer ".length());
        return verifier.verify(accessToken);
    }

    public Map<String, String>  renewTokens(String authHeader, String requestUrl){
        DecodedJWT decodedToken = getDecodedToken(authHeader);
        log.warn(decodedToken.getToken());
        try {
            String userName = decodedToken.getSubject();
            User user = (User) personService.loadUserByUsername(userName);

            return Map.of("accessToken", generateAccessToken(user, requestUrl),
                    "refreshToken", generateRefreshToken(user, requestUrl));

        } catch (Exception e) {
            return null;
        }
    }

    private String generateAccessToken(User user, String requestUrl){
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date( System.currentTimeMillis() + 10 * 60 * 1000 ))
                .withIssuer(requestUrl)
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    private String generateRefreshToken(User user, String requestUrl){
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date( System.currentTimeMillis() + 30 * 60 * 1000 ))
                .withIssuer(requestUrl)
                .sign(algorithm);
    }
}