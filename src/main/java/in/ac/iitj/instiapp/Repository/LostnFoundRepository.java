package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFoundType;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import in.ac.iitj.instiapp.payload.User.Student.StudentBaseDto;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.Optional;


public interface LostnFoundRepository {



/*--------------------------------------------LOCATIONS---------------------------------------------------------------*/
   /**
    * @param locationName
    * @return id of the location name or doesn't exist return -1L
    */
   Long existLocation(String locationName);
   /**
    * @param locations
    * @throws org.springframework.dao.DataIntegrityViolationException When the location name already exist in the database
    */
   void saveLocation(Locations locations);

   /**
    * @param pageable
    * @return list of locations name
    */
   List<String> getListOfLocationsName(Pageable pageable);

   /**
    * @param oldLocationName
    * @param locations new location object
    * @throws org.springframework.dao.EmptyResultDataAccessException when the old location name doesnot exist in the dataabse
    * @throws org.springframework.dao.DataIntegrityViolationException when the locations has a location name which already exist in the daabase
    */
   void updateLocation(String oldLocationName, Locations locations);

   /**
    * @param locationName
    * @implNote first make sure to set the location to null in lostnfound if location is present in thee lostnfound then delete
    */
   void deleteLocationByName(String locationName);



/*---------------------------------------------------LOST AND FOUND---------------------------------------------------*/
   /**
    * @param publicId
    * @return id of the lost and found else return -1L
    */
   Long existLostnFound(Long Id);
   /**
    * @param lostnFound
    */
   void saveLostnFoundDetails(LostnFound lostnFound);

   /**
    * @param status cannot be null must be Optional.empty()
    * @param owner cannot be null must be Optional.empty()
    * @param finder cannot be null must be Optional.empty()
    * @param landmark cannot be null must be Optional.empty()
    * @param pageable
    * @return List of all the lost and found dto based on the given filter
    */
   List<LostnFoundDto> getLostnFoundByFilter(
           LostnFoundType type, Optional<Boolean> status ,Optional<String> owner , Optional<String> finder, Optional<String> landmark ,  Pageable pageable
   );

   /**
    * @param lostnFound
    * @param publicId
    * @throws org.springframework.dao.EmptyResultDataAccessException when no last and found is present in the database of the given public id
    */
   void updateLostnFound(LostnFound lostnFound , Long Id);

   /**
    * @param publicId
    * @return String publicId of media or return Optional.empty()
    */
   Optional<String> deleteLostnFound(Long Id);

   boolean isOwner(String userName, Long Id);
   boolean isFinder(String userName, Long Id);

   LostnFoundType findTypeById(Long Id);


}


