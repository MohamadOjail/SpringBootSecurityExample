package online.ojail.securitydemo.repository;

import online.ojail.securitydemo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepo extends JpaRepository<Person, Long> {

    Person findByUserName(String userName);
}