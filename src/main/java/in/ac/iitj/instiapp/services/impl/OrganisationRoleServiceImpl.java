package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.OrganisationRoleRepository;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import in.ac.iitj.instiapp.services.OrganisationRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganisationRoleServiceImpl implements OrganisationRoleService {

    private final OrganisationRoleRepository organisationRoleRepository;

    @Autowired
    public OrganisationRoleServiceImpl(OrganisationRoleRepository organisationRoleRepository) {
        this.organisationRoleRepository = organisationRoleRepository;
    }

    public OrganisationRole getOrganisationRoleByUsername(String username){
       return organisationRoleRepository.getOrganisationRoleByUsername(username);
    }
}
