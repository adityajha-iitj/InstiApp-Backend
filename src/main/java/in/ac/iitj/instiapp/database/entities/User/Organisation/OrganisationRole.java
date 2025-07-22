package in.ac.iitj.instiapp.database.entities.User.Organisation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import in.ac.iitj.instiapp.database.entities.User.User;
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

    // In your OrganisationRole entity class

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // This creates the foreign key column in the organisation_roles table
    @JsonBackReference
    private User user;


    public OrganisationRole(Long userId) {
        this.Id = userId;
    }
    
    public OrganisationRole(Organisation organisation, String roleName, OrganisationPermission permission) {
        this.organisation = organisation;
        this.roleName = roleName;
        this.permission = permission;
    }
}
