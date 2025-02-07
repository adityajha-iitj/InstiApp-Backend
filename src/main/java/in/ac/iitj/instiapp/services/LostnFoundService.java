package in.ac.iitj.instiapp.services;

import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import org.springframework.data.domain.Pageable;

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

    /**
     * @param oldLocationName
     * @param location
     */
    void updateLocation(String oldLocationName , Locations location);


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
    List<LostnFoundDto> getLostAndFoundByFilter(Optional<Boolean> status , Optional<String> owner , Optional<String> finder, Optional<String> landmark , Pageable pageable);

}