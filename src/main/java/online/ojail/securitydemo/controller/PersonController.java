package online.ojail.securitydemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.ojail.securitydemo.config.security.utils.TokenUtil;
import online.ojail.securitydemo.model.Person;
import online.ojail.securitydemo.service.PersonServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Mo. Ojail
 * created: 2022-09-07
 */

@Slf4j @RequiredArgsConstructor @RestController @RequestMapping("/api")
public class PersonController {

    private final PersonServiceImpl personService;
    private final TokenUtil tokenUtil;

    @GetMapping("/users")
    public ResponseEntity<List<Person>> findAll(){
        return ResponseEntity.ok().body(personService.findAll());
    }

    @PostMapping("/users/save")
    public ResponseEntity<Person> saveUser(@RequestBody Person person){
        return ResponseEntity.ok().body(personService.savePerson(person));
    }

    @GetMapping("/tokens/refresh")
    public void refreshTokens(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            try {
                String requestUrl = request.getRequestURL().toString();
                Map<String, String> newTokens = tokenUtil.renewTokens(authHeader, requestUrl);
                if (newTokens != null){
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), newTokens);
                } else throw new Exception("Provided Token could not be verified");

            } catch (Exception e) {
                response.setStatus(FORBIDDEN.value());
                Map<String, String> body = new HashMap<>();
                body.put("TokenException", e.getMessage());
                body.put("error", "token refresh failed");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), body);
            }
        }else throw new RuntimeException("Refresh token error");
    }
}
