package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusOverride;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusScheduleDto;
import org.springframework.data.domain.Pageable;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

public interface BusRepository  {


//    --------------------------------- Operations For Bus Location -------------------------------------------

    /**
     * @param name Of Location to be inserted
     * @throws org.springframework.dao.DataIntegrityViolationException if Bus Name already exists
     */
    void saveBusLocation(String name);


    /**
     * @param pageable  firstResult Max Value-INT_MAX,Min Value-0 PageSize-Max value-TODO
     * @return List<String> of BustLocations name
     */
    List<String> getListOfBusLocations(Pageable pageable);


    /**
     * @param name Name of BusLocation
     * @return  -1 if bus Location doesn't exist in database else id
     */
    Long isBusLocationExists(String name);

    /**
     * Only changes the name of the bus location
     * @param oldName - Previous Bus Location Name
     * @param newName - New Bus Location Name
     * @throws org.springframework.dao.EmptyResultDataAccessException Old Bus Location Does Not exist
     * @throws org.springframework.dao.DataIntegrityViolationException New Bus Location exist
     */
    void updateBusLocation(String oldName, String newName);


    /**
     * Deletes all bus runs and overrides linked to the specified location.
     * Use with caution as it may cause cascading impacts.
     * @param name Of Location to be deleted
     * @throws org.springframework.dao.EmptyResultDataAccessException if bus location does not exist
     */
    void deleteBusLocation(String name);




//   -------------------------------------------- Operations for BusSchedule ---------------------------------------------


    /**
     * @param busNumber Unique Identifier for bus
     * @throws org.springframework.dao.DataIntegrityViolationException if the identifier already exists
     */
    void saveBusSchedule(String busNumber);


    /**
     * @param busNumber - Whose BusSchedule we need to get
     * @return BusScheduleDto not populated by Overrides but only BusRuns.You have to do a separate fetch to get BusOverrides
     * @throws org.springframework.dao.EmptyResultDataAccessException if BusNumber doesn't exists
     */
    BusScheduleDto getBusSchedule(String busNumber);


    /**
     * @param pageable Pageable Constraints
     * @return List of Name of Current Buses
     */
    List<String> getBusNumbers(Pageable pageable);

    /**
     * @return -1 if busNumber does not exists in database
     */
    Long existsBusSchedule(String busNumber);


    /**
     * Only Changes the name
     * @throws org.springframework.dao.DataIntegrityViolationException if newBusNumber exist
     * @throws org.springframework.dao.EmptyResultDataAccessException if oldBusNumber does not exist
     */
    void updateBusSchedule(String oldBusNumber,String newBusNumber);

    /**
     * Also Deletes the BusOverrides and BusRuns associated with BusNumber
     * Use with caution as it could have cascading effects
     * @throws org.springframework.dao.EmptyResultDataAccessException if busNumber does not exist
     */
    void deleteBusSchedule(String busNumber);


    //   -------------------------------------------- Operations for BusRuns ---------------------------------------------

    /**
     * @assumptions PublicId is not null
     * @param busRun Dto may be converted to busRun, but it doesn't take value from BusSchedule.So buschedule could be null
     * @param busNumber The busNumber to whose busSchedule BusRun should be added
     * @throws org.springframework.dao.DataIntegrityViolationException if Bus run for particular bus with same BusRun exists
     * @throws org.springframework.dao.EmptyResultDataAccessException if busNumber doesn't exist for the bus
     */
    void saveBusRun(BusRun busRun,String busNumber);


    /**
     * @param publicId of BusRun to be checked if it exists
     * @return false if publicId doesn't exist else id of object
     */
    Boolean existsBusRunByPublicId(String publicId);

    /**
     * Doesn't changes publicId, Nor the BusSchedule.
     * @param publicId of BusRun to be changed.It should exist in database
     * @param newBusRun Should not exist in database.BusSchedule would not be changed so its value could be null
     * @throws org.springframework.dao.DataIntegrityViolationException if busRun with same parameter exist for different publicId.
     * @throws org.springframework.dao.EmptyResultDataAccessException if either of the new locations do not exist
     * @throws org.springframework.dao.EmptyResultDataAccessException If PublicId doesn't exist
     */
    void updateBusScheduleRun(String publicId, BusRun newBusRun);


    /**
     * Can be used for single singlePublicId
     * @param busRunPublicIds May not exists in database.
    */
    void deleteBusRuns(List<String> busRunPublicIds);

    //   -------------------------------------------- Operations for BusOverride ---------------------------------------------

    /**
     * @throws org.springframework.dao.EmptyResultDataAccessException if busNumber doesn't exist in database
     * @throws org.springframework.dao.DataIntegrityViolationException if override exist in database for the same bus
     */
    void saveBusOverride(String busNumber, BusOverride busOverride);

    /**
     * @param publicId checks if exist
     * @return Optional.empty() if it doesn't exist
     */
    boolean existsBusOverrideByPublicId(String publicId);

    /**
     * @return Empty List if anything doesn't exist
     */
    List<BusOverrideDto> getBusOverrideForYearAndMonth(int year, int month);

    /**
     * Doesn't updates publicId nor the BusSchedule
     * @param publicId  of BusOverride to be updated.BusSchedule details wouldn't be updated.so it could be null
     * @throws org.springframework.dao.EmptyResultDataAccessException if busNumber doesn't exist or BusOverride doesn't exist
     */
    void updateBusOverride(String publicId,BusOverride newBusOverride);

    /**
     * Can be used in combination with exist function to delete single id
     * @param busOverrideIds may or maynot exist in database
     */
    void deleteBusOverride(List<String> busOverrideIds);



}