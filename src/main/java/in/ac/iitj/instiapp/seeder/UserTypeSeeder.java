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
    public void run(String... args) {
        // We will now check for each user type individually before saving.
        seedUserTypeIfNotExists("Student");
        seedUserTypeIfNotExists("Faculty");
        seedUserTypeIfNotExists("Alumni");
    }

    private void seedUserTypeIfNotExists(String name) {
        // Use the new repository method to check if a Usertype with this name already exists.
        // The .isEmpty() check is more robust than counting.
        if (userTypeRepository.findByName(name).isEmpty()) {
            try {
                userTypeRepository.save(new Usertype(null, name));
                System.out.println("UserTypeSeeder: Successfully seeded '" + name + "'.");
            } catch (Exception e) {
                // This catch is a fallback for other potential database issues.
                System.err.println("UserTypeSeeder: Error while seeding '" + name + "': " + e.getMessage());
            }
        }
    }
}
