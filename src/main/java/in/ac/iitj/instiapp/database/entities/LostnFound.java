package in.ac.iitj.instiapp.database.entities;

import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lostnfound")
public class LostnFound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @ManyToOne
    @JoinColumn(name = "finder_id", nullable = true)
    User finder;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = true)
    User owner;

    @Column( nullable = false)
    String Location;

    @Column( nullable = false)
    Boolean status;

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = true)
    Media media;
}
