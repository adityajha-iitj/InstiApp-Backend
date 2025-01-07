package in.ac.iitj.instiapp.database.entities.User.Organisation;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organisation_role")
public class OrganisationRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;


    @ManyToOne
    Organisation organisation;

    @Column
    String roleName;

    @Enumerated(EnumType.STRING)
    OrganisationPermission permission;


    public OrganisationRole(Long userId) {
        this.Id = userId;
    }
    
    public OrganisationRole(Organisation organisation, String roleName, OrganisationPermission permission) {
        this.organisation = organisation;
        this.roleName = roleName;
        this.permission = permission;
    }
}
