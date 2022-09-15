package online.ojail.securitydemo.service;

import online.ojail.securitydemo.model.Person;
import online.ojail.securitydemo.model.Role;

import java.util.List;

/**
 * @author Mo. Ojail
 * created: 2022-09-07
 */

public interface PersonService {

    Person savePerson(Person person);
    Person getPerson(String userName);
    List<Person> findAll();
    List<Role> addRoleToPerson(String userName, Role role);
    List<Role> getPersonRoles(String userName);
}
