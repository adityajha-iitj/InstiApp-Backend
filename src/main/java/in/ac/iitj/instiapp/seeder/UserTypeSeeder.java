package in.ac.iitj.instiapp.seeder;

import in.ac.iitj.instiapp.Repository.UserTypeRepository;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserTypeSeeder implements CommandLineRunner {

    private final UserTypeRepository userTypeRepository;

    @Autowired
    public UserTypeSeeder(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        userTypeRepository.save(new Usertype(null, "Student"));
        userTypeRepository.save(new Usertype(null, "Faculty"));
        userTypeRepository.save(new Usertype(null, "Alumni"));
    }
}
