package in.ac.iitj.instiapp.database.entities.Scheduling.Calendar;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "calendar")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false)
    String public_id;

    @OneToOne(mappedBy = "calendar")
    User user;

    @OneToMany(mappedBy = "calendar")
    Set<Events> events;
}
