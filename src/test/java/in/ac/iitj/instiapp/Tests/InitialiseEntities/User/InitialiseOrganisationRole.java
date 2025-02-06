package in.ac.iitj.instiapp.Tests.InitialiseEntities.User;


import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRepository;
import in.ac.iitj.instiapp.Repository.User.Organisation.OrganisationRoleRepository;
import in.ac.iitj.instiapp.Repository.impl.OrganisationRoleRepositoryImpl;
import in.ac.iitj.instiapp.Tests.EntityTestData.OrganisationRoleData;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.User.Organisation.Organisation;
import in.ac.iitj.instiapp.database.entities.User.Organisation.OrganisationRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import static in.ac.iitj.instiapp.Tests.EntityTestData.UserData.*;

@Component
@Import({OrganisationRoleRepositoryImpl.class , InitialiseOrganisation.class})
public class InitialiseOrganisationRole implements InitialiseEntities.Initialise {


    private final OrganisationRoleRepository organisationRoleRepository;
    private final OrganisationRepository organisationRepository;



    @Autowired
    public InitialiseOrganisationRole(OrganisationRoleRepository organisationRoleRepository, OrganisationRepository organisationRepository, InitialiseOrganisation initialiseOrganisation) {
        this.organisationRoleRepository = organisationRoleRepository;
        this.organisationRepository = organisationRepository;

        initialiseOrganisation.initialise();

    }


    @Transactional
    public void initialise(){
        OrganisationRole organisationRole1 = OrganisationRoleData.ORGANISATION_ROLE1.toEntity();
        OrganisationRole organisationRole2 = OrganisationRoleData.ORGANISATION_ROLE2.toEntity();
        OrganisationRole organisationRole3 = OrganisationRoleData.ORGANISATION_ROLE3.toEntity();

        organisationRole1.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER1.userName)));
        organisationRole2.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER2.userName)));
        organisationRole3.setOrganisation(new Organisation(organisationRepository.existOrganisation(USER3.userName)));

        organisationRoleRepository.saveOrganisationRole(organisationRole1);
        organisationRoleRepository.saveOrganisationRole(organisationRole2);
        organisationRoleRepository.saveOrganisationRole(organisationRole3);

    }

}

