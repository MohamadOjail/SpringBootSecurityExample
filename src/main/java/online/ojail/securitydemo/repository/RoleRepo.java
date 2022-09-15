package online.ojail.securitydemo.repository;

import online.ojail.securitydemo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {
}