package in.ac.iitj.instiapp.database.entities.LostnFound;

import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


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

    @ManyToOne
    Locations Landmark;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    LostnFoundType type;

    @Column( nullable = true)
    String extraInfo;

    @Column( nullable = false)
    Boolean status;

    @Column(name="time")
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = true)
    Media media;
}
