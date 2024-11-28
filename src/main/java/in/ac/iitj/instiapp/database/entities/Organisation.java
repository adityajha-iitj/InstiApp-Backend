package in.ac.iitj.instiapp.database.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organisation")
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    Organisation parentOrganisation;

    @OneToOne
    @JoinColumn(name = "type", nullable = false)
    OrganisationType type;

    @Column(nullable = false)
    String Description;

    @ManyToOne
    @JoinColumn(name = "IntroMedia", nullable = false)
    Media media;

    @Column(nullable = false)
    String Website;
}
