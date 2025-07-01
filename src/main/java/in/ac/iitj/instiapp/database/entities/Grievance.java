package in.ac.iitj.instiapp.database.entities;

import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grievance")
public class Grievance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false, length = 36)
    private String publicId;

    @Column( nullable = false)
    String Title;

    @Column(nullable = false)
    String Description;

    @ManyToOne
    User userFrom;

    //To be implemented permission > read
    @ManyToOne
    OrganisationRole organisationRole;

    @Column(nullable = false, columnDefinition = "boolean default false")
    Boolean resolved;

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = true)
    Media media;

    @PrePersist
    private void ensurePublicId() {
        if (publicId == null) {
            publicId = UUID.randomUUID().toString();
        }
    }
}
