package online.ojail.securitydemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.ojail.securitydemo.model.Person;
import online.ojail.securitydemo.model.Role;
import online.ojail.securitydemo.repository.PersonRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Mo. Ojail
 * created: 2022-09-07
 */

@Service @Slf4j @Transactional @RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, UserDetailsService {

    private final PersonRepo personRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepo.findByUserName(username);
        if (person == null){
            log.error("User: {} - Not Found", username);
            throw new UsernameNotFoundException("User not Found");
        }
        log.info("User: {} - Found", username);
        List<SimpleGrantedAuthority> authorities = person.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).toList();
        return new User(person.getUserName(), person.getPassword(), authorities);
    }

    @Override
    public Person savePerson(Person person) {
        log.info("Saving new User: {}", person.getUserName());
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        return personRepo.save(person);
    }

    @Override
    public Person getPerson(String userName) {
        log.info("Getting User: {} - from DataBase", userName);
        return personRepo.findByUserName(userName);
    }

    @Override
    public List<Person> findAll() {
        log.info("Getting a list of Users");
        return personRepo.findAll();
    }

    @Override
    public List<Role> addRoleToPerson(String userName, Role role) {
        log.info("Adding Role: {} to User: {} - from DataBase", role.getRoleName(), userName);
        Person byUserName = personRepo.findByUserName(userName);
        byUserName.getRoles().add(role);
        return byUserName.getRoles();
    }

    @Override
    public List<Role> getPersonRoles(String userName) {
        log.info("Getting list of roles for User: {} - from DataBase", userName);
        Person byUserName = personRepo.findByUserName(userName);
        return byUserName.getRoles();
    }
}
