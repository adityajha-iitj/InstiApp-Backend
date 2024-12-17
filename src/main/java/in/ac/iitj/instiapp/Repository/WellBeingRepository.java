package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator.WellBeingMember;

import java.util.List;

public interface WellBeingRepository {

    void save(WellBeingMember member);
    void updateQualification(String username , String new_qualification);
    void updateAvailability(String username , String new_availability);
    List<WellBeingMember> getAllMembers();
    boolean memberExists(String username);
    void deleteMember(String username);
}
