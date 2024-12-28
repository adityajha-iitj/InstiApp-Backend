package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.Student.Alumni.Alumni;
import in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator.WellBeingMember;
import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoFull;
import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoLimited;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WellBeingRepository {

    /**
     * @param member username could be null but userId exist
     * @asumptions assuming that user has already been created and user id is given by service layer
     */
    void save(WellBeingMember member);

    /**
     * @assumptions Constraints of pageable are already checked
     * @return List of Limited data dto of all the wellbeing members present without any filter of username
     *         Only username filled in user object in WellBeingMemberDtoLimited
     */
    List<WellBeingMemberDtoLimited> getAllMembers(Pageable pageable);

    /**
     * @param username
     * @return Only Username filled in WellBeingMemberDtoFull
     * @throws org.springframework.dao.EmptyResultDataAccessException when the wellbeing member of that username does not exist
     */

    WellBeingMemberDtoFull findByUsernameFull(String username);

    /**
     * @param username
     * @return True if there is a wellbeing member of that username otherwise return False
     */
    boolean memberExists(String username);


    /**
     * @assumptions None of the values of other than linked objects is null.Also userExists checked at upper level
     * @param wellBeingMember Only Id should Exist nothing else in user object;
     */
    void updateMember(WellBeingMember wellBeingMember);


    /**
     * After deleting the member delete the user for dataIntegrity
     * @param username
     * @return Id of user
     * @throws java.util.NoSuchElementException if the wellbeing member of that username doesn't exist
     */
    Long deleteMember(String username);
}
