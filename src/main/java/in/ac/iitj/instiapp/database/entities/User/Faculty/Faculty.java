package in.ac.iitj.instiapp.database.entities.User.Faculty;

import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "faculty")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    User user;

    @ManyToOne
    Organisation organisation;
}
