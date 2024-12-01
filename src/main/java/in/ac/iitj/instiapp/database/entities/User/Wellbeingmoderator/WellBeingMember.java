package in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wellBeingMember")
public class WellBeingMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key in Media table
    User user;

    @Column(nullable = false)
    String qualification;

    @Column( nullable = false)
    String availability;
}
