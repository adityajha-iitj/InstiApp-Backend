package in.ac.iitj.instiapp.Tests.InitialiseEntities;

import in.ac.iitj.instiapp.Repository.PermissionsRepository;
import in.ac.iitj.instiapp.Repository.impl.PermissionsRepositoryImpl;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.database.entities.User.PermissionsData;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Import({PermissionsRepositoryImpl.class})
public  class InitialisePermission implements InitialiseEntities.Initialise {

    private final PermissionsRepository permissionsRepository;

    @Autowired
    public InitialisePermission(PermissionsRepository permissionsRepository){
        this.permissionsRepository = permissionsRepository;
    }


    @Transactional
    public void initialise(){
        permissionsRepository.savePermission(PermissionsData.BUS_SCHEDULE.toEntity());
        permissionsRepository.savePermission(PermissionsData.CALENDAR.toEntity());
    }

}

