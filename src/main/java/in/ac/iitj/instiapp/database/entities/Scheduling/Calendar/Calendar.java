package in.ac.iitj.instiapp.database.entities.Scheduling.Calendar;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false)
    String public_id;

    @OneToOne(mappedBy = "calendar")
    User user;

    @OneToMany(mappedBy = "calendar")
    List<Events> events;
}
