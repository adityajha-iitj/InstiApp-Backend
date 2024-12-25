package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyDetailedDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FacultyRepository {


//    void save(User user, Faculty faculty);
//    Faculty getFaculty(String username);
//    void updateFaculty(Faculty faculty);
//    void deleteFaculty(Faculty faculty);
//    boolean FacultyExists(String username);


    /**
     * @assumptions User Exists
     *              Organisation Exists
     * @param faculty userId should not be null
     *                OrganisationId shouldnot be null
     */
    void save(Faculty faculty);


    /**
     * @param username
     * @return FacultyBaseDto only name filled in organisation
     *         Only username filled in UserbaseDto
     */
    FacultyBaseDto getFaculty(String username);


    /**
     * @param username
     * @return FacultyDetailedDto only name filled in organisation\
     *                            only username filled in UserDetailedDto
     */
    FacultyDetailedDto getDetailedFaculty(String username);


    /**
     * The OptionalValues shouldn't be null it should be always Optional.empty()
     */
    List<FacultyBaseDto> getFacultyByFilter(Optional<String> organisationName, Pageable pageable);


    /**
     * It doesn't update Userdetails.
     * @param faculty Organisation Id shoudn't be null
     *                UserId shouldn't be null  in user
     */
    void updateFaculty(Faculty faculty);

}
