package in.ac.iitj.instiapp.database.entities.User;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "groups")
public class Groups {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    String name;

    @ManyToMany
    @JoinTable(name = "groups_users")
    Set<User> users;
}
