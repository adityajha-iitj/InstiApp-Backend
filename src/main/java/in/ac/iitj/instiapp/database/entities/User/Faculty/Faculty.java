package in.ac.iitj.instiapp.database.entities.User.Faculty;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;

public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    User user;
}
