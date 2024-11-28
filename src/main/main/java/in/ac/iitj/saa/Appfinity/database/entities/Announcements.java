package in.ac.iitj.saa.Appfinity.database.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "announcements")
public class Announcements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @ManyToOne
    @JoinColumn(name = "announcer", nullable = false)
    User user;

    @Column(nullable = false)
    String Title;

    @Column( nullable = true)
    String Description;

    @ManyToOne
    @JoinColumn(name = "Media[]", nullable = true)
    Media media;

    @Column( nullable = false)
    String targetGroup;
}
