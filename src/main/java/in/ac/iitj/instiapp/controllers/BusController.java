package in.ac.iitj.instiapp.controllers;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusLocation;
import in.ac.iitj.instiapp.services.BusService;
import org.springframework.data.domain.PageRequest;
import org. springframework. data. domain. Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BusController {
    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

/*--------------------------------------------------BUS LOCATION------------------------------------------------------*/

    @PostMapping("/bus-location")
    public void saveBusLocation(@RequestBody String location){
        busService.saveBusLocation(location);
    }

    @PutMapping("/bus-location")
    public void updateBusLocation(@RequestBody Map<String, String> requestBody){
        String oldlocation = requestBody.get("oldLocationName");
        String newlocation = requestBody.get("newLocationName");
        busService.updateBusLocation(oldlocation, newlocation);
    }

    @GetMapping("/bus-location")
    public List<String> getBusLocation(@RequestParam Integer pageSize , @RequestParam Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return  busService.getBusLocations(pageable);
    }




}
