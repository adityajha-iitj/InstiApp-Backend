package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFoundType;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface LostnFoundService {


/*--------------------------------------------------------LOCATIONS---------------------------------------------------*/
    /**
     * @param pageable
     * @return
     */
    List<String> getListOfLocationsName(Pageable pageable);

    /**
     * @param location
     */
    void saveLocation(Locations location);

    /**
     * @param locationName
     */
    void deleteLocationByName(String locationName);


    void updateLocation(String oldLocationName ,String newLocationName);


/*-----------------------------------------------------LOST AND FOUND-------------------------------------------------*/
    /**
     * @param lostnFoundDto
     */
    void saveLostAndFound(LostnFoundDto lostnFoundDto);

    /**
     * @param lostnFoundDto
     * @asumptions the Dto contains the public key attribute to update the lost and found
     */
    void updateLostAndFound(LostnFoundDto lostnFoundDto);

    /**
     * @param publicId
     */
    void deleteLostAndFound(String publicId);

    /**
     * @param status
     * @param owner
     * @param finder
     * @param landmark
     * @param pageable
     * @return
     */
    List<LostnFoundDto> getLostAndFoundByFilter(LostnFoundType type,Optional<Boolean> status , Optional<String> owner , Optional<String> finder, Optional<String> landmark , Pageable pageable);

    boolean isOwner(String userName, String publicId);
    boolean isFinder(String userName, String publicId);
    public LostnFoundType findTypeByPublicId(String publicId);


    String uploadLostnFoundImage(String publicId, MultipartFile file) throws Exception;

    String getLostnFoundImageUrl(String publicId) throws Exception;

}