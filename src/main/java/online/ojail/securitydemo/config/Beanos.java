package online.ojail.securitydemo.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

/**
 * @author Mo. Ojail
 * created: 2022-09-07
 */

@Configuration
public class Beanos {

    @Bean
    public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

    @Bean
    public Faker faker(){ return new Faker(); }

    @Bean
    public Random rnd(){ return new Random(); }

    @Bean
    public Algorithm algorithm(){ return Algorithm.HMAC256("secret".getBytes());}

    @Bean
    public JWTVerifier verifier(){
        return JWT.require(algorithm()).build();
    }
}
