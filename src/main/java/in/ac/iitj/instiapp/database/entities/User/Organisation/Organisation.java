package in.ac.iitj.instiapp.database.entities.User.Organisation;

import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.User;
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
    User user;

    @ManyToOne
    Organisation parentOrganisation;

    @ManyToOne
    OrganisationType type;

    @Column(nullable = false)
    String Description;

    @OneToOne
    Media media;

    @Column(nullable = true)
    String Website;

    public Organisation(Long organisationId) {
        this.Id = organisationId;
    }

    public String getTypeName() {
        return type.getName();
    }
}
