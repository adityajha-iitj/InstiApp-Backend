package in.ac.iitj.instiapp.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BusRepository  {


    void saveBusLocation(String name);
    void deleteBusLocation(String name);
    List<String> getListOfBusLocations(Pageable pageable);



    void saveBusSchedule(String busNumber);
    void deleteBusSchedule(String busNumber);
    void updateBusSchedule(String oldBusNumber,String newBusNumber);



}