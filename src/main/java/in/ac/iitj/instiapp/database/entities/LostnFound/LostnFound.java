package in.ac.iitj.instiapp.database.entities.LostnFound;

import in.ac.iitj.instiapp.database.entities.Media;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;

import javax.xml.stream.Location;


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
    @JoinColumn(name = "finder_id", nullable = false)
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
