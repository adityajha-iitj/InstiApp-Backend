package in.ac.iitj.instiapp.database.entities.LostnFound;

import in.ac.iitj.instiapp.database.entities.Media.Media;
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

    @Column(nullable = false , unique = true )
    String publicId;

    @ManyToOne
    @JoinColumn(name = "finder_id", nullable = true)
    User finder;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = true)
    User owner;

    @ManyToOne
    Locations Landmark;

    @Column( nullable = true)
    String extraInfo;

    @Column( nullable = false)
    Boolean status;

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = true)
    Media media;
}
