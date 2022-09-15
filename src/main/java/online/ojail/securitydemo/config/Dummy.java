package online.ojail.securitydemo.config;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.ojail.securitydemo.model.Person;
import online.ojail.securitydemo.model.Role;
import online.ojail.securitydemo.repository.RoleRepo;
import online.ojail.securitydemo.service.PersonServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static online.ojail.securitydemo.model.RoleEnum.ADMIN;
import static online.ojail.securitydemo.model.RoleEnum.USER;

/**
 * @author Mo. Ojail
 * created: 2022-09-07
 */

@Component @RequiredArgsConstructor @Slf4j
public class Dummy implements CommandLineRunner {

    private final Faker faker;
    private final Random rnd;
    private final PersonServiceImpl personService;
    private final RoleRepo roleRepo;

    @Override
    public void run(String... args) throws Exception {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(null, ADMIN.name()));
        roles.add(new Role(null, USER.name()));
        roleRepo.saveAll(roles);

        for (int i = 0; i < 10; i++) {
            Person person = new Person();
            person.setFirstName(faker.name().firstName());
            person.setLastName(faker.name().lastName());
            person.setUserName(faker.internet().emailAddress());
            person.setPassword("pass");
            int j = rnd.nextInt(2);
            if (j>0) {
                person.getRoles().addAll(roleRepo.findAll());
            }else {
                person.getRoles().add(getRandomRole().get());
            }
            personService.savePerson(person);
        }

        Person personM = new Person(null, "Mohamad", "Ojail", "m@ojail.online", "pass", new ArrayList<>());
        personM.getRoles().add(getRandomRole().get());
        personService.savePerson(personM);

        personService.findAll().forEach(person -> {
            System.out.println("\n########################################");
            log.info("User: {} has these roles:", person.getUserName());
            person.getRoles().forEach(role -> log.info("Role: {}", role.getRoleName()));
            System.out.println("########################################");
        });
    }

    private Optional<Role> getRandomRole(){
        int val = rnd.nextInt(2) + 1;
        log.info("My Random Val:" + val);
        return roleRepo.findById(val);
    }
}
