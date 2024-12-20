package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.Wellbeingmoderator.WellBeingMember;
import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoFull;
import in.ac.iitj.instiapp.payload.User.WellBeingModerator.WellBeingMemberDtoLimited;

import java.util.List;

public interface WellBeingRepository {

    /**
     * @param member
     * @param user_id
     * @asumptions assuming that user has already been created and user id is given by service layer
     */
    void save(WellBeingMember member , long user_id);

    /**
     * @param username
     * @param new_qualification
     * @throws java.util.NoSuchElementException when wellbeing member of that username doesn't exist
     */
    void updateQualification(String username , String new_qualification);

    /**
     * @param username
     * @param new_availability
     * @throws java.util.NoSuchElementException when wellbeing member of that username doesn't exist
     */
    void updateAvailability(String username , String new_availability);

    /**
     * @param username
     * @return Returns a Dto which contains the limited information of the user along with the wellbeing member information
     * @throws java.util.NoSuchElementException when the username given doesn't exist
     */
    WellBeingMemberDtoLimited findByUsernameLimited(String username);

    /**
     * @param username
     * @return return a Dto which contains the full data of user i.e. it also contains the phone number of the wellbeing moderator
     * @throws java.util.NoSuchElementException when the wellbeing member of that username does not exist
     */

    WellBeingMemberDtoFull findByUsernameFull(String username);

    /**
     * @return List of Limited data dto of all the wellbeing members present without any filter of username
     */
    List<WellBeingMemberDtoLimited> getAllMembers();

    /**
     * @param username
     * @return True if there is a wellbeing member of that username otherwise return False
     */
    boolean memberExists(String username);

    /**
     * @param username
     * @Asumption user is already deleted from the user table in the service layer
     * @throws java.util.NoSuchElementException if the wellbeing member of that username doesn't exists
     */
    void deleteMember(String username);
}
