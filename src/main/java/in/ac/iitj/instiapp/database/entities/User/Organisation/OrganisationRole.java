package in.ac.iitj.instiapp.database.entities.User.Organisation;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;


    @ManyToOne
    Organisation organisation;

    String roleName;

    @Enumerated(EnumType.STRING)
    OrganisationPermission permission;




}
