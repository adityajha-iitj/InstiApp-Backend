package in.ac.iitj.instiapp.database.entities;

import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.database.entities.User.User;
import jakarta.persistence.*;
import lombok.*;


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
}
