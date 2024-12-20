package in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "well_being_member")
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

    public WellBeingMember(String qualification, String availability) {
        this.qualification = qualification;
        this.availability = availability;
    }
}
