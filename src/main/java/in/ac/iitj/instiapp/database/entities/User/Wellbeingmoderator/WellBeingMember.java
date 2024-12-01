package in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WellBeingMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @ManyToOne
    User user;

    @Column(nullable = false)
    String qualification;

    @Column( nullable = false)
    String availability;
}
