package in.ac.iitj.instiapp.database.entities.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;

import java.util.Set;

@Entity
public class Groups {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    String name;

    @ManyToMany
    Set<User> users;
}
