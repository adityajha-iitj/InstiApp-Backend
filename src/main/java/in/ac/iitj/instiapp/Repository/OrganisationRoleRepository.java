package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRoleRepository extends JpaRepository<OrganisationRole, Integer> {

    @Query("SELECT o FROM OrganisationRole o WHERE o.organisation.user.userName= :username")
    public OrganisationRole getOrganisationRoleByUsername(String username);

}
