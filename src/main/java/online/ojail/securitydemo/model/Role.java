package online.ojail.securitydemo.model;

import lombok.*;

import javax.persistence.*;

/**
 * @author Mo. Ojail
 * created: 2022-09-07
 */

@Entity @Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Role {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull @Column(unique = true)
    private String roleName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        return roleName.equals(role.roleName);
    }

    @Override
    public int hashCode() {
        return roleName.hashCode();
    }
}