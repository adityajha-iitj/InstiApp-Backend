package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.Repository.OrganisationRoleRepository;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;

public interface OrganisationRoleService {


    public OrganisationRole getOrganisationRoleByUsername(String username);
}
