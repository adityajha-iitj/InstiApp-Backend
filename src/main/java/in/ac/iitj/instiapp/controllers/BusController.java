package in.ac.iitj.instiapp.controllers;

import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusOverride;
import in.ac.iitj.instiapp.payload.Scheduling.Buses.*;
import in.ac.iitj.instiapp.services.BusService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BusController {
    private final BusService busService;

    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }

    // ------------------- Bus Location Endpoints -------------------

    @PostMapping("/bus-location")
    public ResponseEntity<String> saveBusLocation(@Valid @RequestBody BusLocationDto busLocationDto) {
        busService.saveBusLocation(busLocationDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body("BusLocation saved successfully");
    }

    @GetMapping("/bus-locations")
    public List<String> getBusLocations(Pageable pageable){
        return busService.getBusLocations(pageable);
    }


    @PutMapping("/bus-location")
    public ResponseEntity<String> updateBusLocation(@Valid @RequestParam String oldName, @Valid @RequestParam String newName) {
        Long oldNameExists = busService.isBusLocationExist(oldName);
        Long newNameExists = busService.isBusLocationExist(newName);
        if (oldNameExists == -1L) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Old bus location does not exist.");
        }
        if (newNameExists != -1L) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: New bus location name already exists.");
        }
        busService.updateBusLocation(oldName, newName);
        return ResponseEntity.ok("Bus location updated successfully.");
    }


    @DeleteMapping("/bus-location")
    public ResponseEntity<String> deleteBusLocation(@Valid @RequestParam String name) {
        Long oldNameExists = busService.isBusLocationExist(name);
        if(oldNameExists == -1L) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Bus location does not exist.");
        }
        busService.deleteBusLocation(name);
        return ResponseEntity.ok("Bus location deleted successfully.");
    }

    // ------------------- Bus Schedule endpoints -------------------


    @PostMapping("/bus-schedule")
    public ResponseEntity<String> saveBusSchedule(@Valid @RequestParam String busNumber) {
        busService.saveBusSchedule(busNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body("BusSchedule saved successfully");
    }

    @GetMapping("/bus-schedule")
    public ResponseEntity<?> getBusSchedule(@Valid @RequestParam String busNumber) {
        Long busNumberExists = busService.existsBusSchedule(busNumber);
        if (busNumberExists == -1L) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The bus number is not found");
        }
        return ResponseEntity.ok(busService.getBusSchedule(busNumber));
    }

    @GetMapping("/bus-numbers")
    public List<String> getBusNumbers(Pageable pageable) {
        return busService.getBusNumbers(pageable);
    }

    @PutMapping("/bus-schedule")
    public ResponseEntity<String> updateBusSchedule(@Valid @RequestParam String oldBusNumber, @Valid @RequestParam String newBusNumber) {
        Long oldBusExists = busService.existsBusSchedule(oldBusNumber);
        Long newBusExists = busService.existsBusSchedule(newBusNumber);
        if (oldBusExists == -1L) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Old bus number does not exist.");
        }
        if (newBusExists != -1L) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: New bus number already exists.");
        }
        busService.updateBusSchedule(oldBusNumber, newBusNumber);
        return ResponseEntity.ok("Bus schedule updated successfully.");
    }

    @DeleteMapping("/bus-schedule")
    public ResponseEntity<String> deleteBusSchedule(@Valid @RequestParam String busNumber) {
        Long busExists = busService.existsBusSchedule(busNumber);
        if (busExists == -1L) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Bus schedule does not exist.");
        }
        busService.deleteBusSchedule(busNumber);
        return ResponseEntity.ok("Bus schedule deleted successfully.");
    }

    // ------------------- Bus Override Endpoints -------------------


//    @PostMapping("/bus-override")
//    public ResponseEntity<String> saveBusOverride(@Valid @RequestParam String busNumber, @Valid @RequestBody BusOverride busOverride) {
//        busService.saveBusOverride(busNumber, busOverride);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Bus override saved successfully.");
//    }
//
//    @GetMapping("bus-override")
//    public ResponseEntity<List<BusOverrideDto>> getBusOverrideForYearAndMonth(@Valid @RequestParam int year, @Valid @RequestParam int month) {
//        return ResponseEntity.ok(busService.getBusOverrideForYearAndMonth(year, month));
//    }
//    @PutMapping("/bus-override")
//    public ResponseEntity<String> updateBusOverride(@Valid @RequestParam String publicId, @Valid @RequestBody BusOverride newBusOverride) {
//        if (!busService.existsBusOverrideByPublicId(publicId)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Bus override does not exist.");
//        }
//        busService.updateBusOverride(publicId, newBusOverride);
//        return ResponseEntity.ok("Bus override updated successfully.");
//    }
//    @DeleteMapping("/bus-override")
//    public ResponseEntity<String> deleteBusOverride(@Valid @RequestBody List<String> busOverrideIds) {
//        busService.deleteBusOverride(busOverrideIds);
//        return ResponseEntity.ok("Bus overrides deleted successfully.");
//    }

    // ------------------- BusRoute and RouteStop Endpoints-------------------

    @PostMapping("/bus-routes")
    public void createBusRoute(@Valid @RequestBody BusRouteDto busRouteDto) {
        busService.saveBusRoute(busRouteDto);
        ResponseEntity.status(HttpStatus.CREATED);
    }

    @GetMapping("/bus-routes")
    public ResponseEntity<List<BusRouteDto>> getAllBusRoutes() {
        List<BusRouteDto> routes = busService.getAllBusRoutes();
        return ResponseEntity.ok(routes);
    }
    @GetMapping("/bus-routes/{routeId}")
    public ResponseEntity<BusRouteDto> getBusRoute(@PathVariable Long routeId) {
        BusRouteDto route = busService.getBusRouteByRouteId(routeId);
        if (route == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(route);
    }

    @PutMapping("/bus-routes/{routeId}")
    public ResponseEntity<BusRouteDto> updateBusRoute(@PathVariable Long routeId, @Valid @RequestBody BusRouteDto busRouteDto) {
        BusRouteDto updated = busService.updateBusRoute(routeId, busRouteDto);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

//    @DeleteMapping("/bus-routes/{routeId}")
//    public ResponseEntity<Void> deleteBusRoute(@PathVariable Long routeId) {
//        busService.deleteBusRoute(routeId);
//        return ResponseEntity.noContent().build();
//    }

    // RouteStop endpoints
    @PostMapping("/bus-routes/{routeName}/stops")
    public ResponseEntity<RouteStopDto> addRouteStop(@PathVariable String routeName, @Valid @RequestBody RouteStopDto stopDto) {
        RouteStopDto created = busService.addRouteStop(routeName, stopDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
//    @PutMapping("/bus-routes/{routeId}/stops/{stopId}")
//    public ResponseEntity<RouteStopDto> updateRouteStop(@PathVariable Long routeId, @PathVariable Long stopId, @Valid @RequestBody RouteStopDto stopDto) {
//        RouteStopDto updated = busService.updateRouteStop(routeId, stopId, stopDto);
//        if (updated == null) return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(updated);
//    }

//    @DeleteMapping("/bus-routes/{routeId}/stops/{stopId}")
//    public ResponseEntity<Void> deleteRouteStop(@PathVariable Long routeId, @PathVariable Long stopId) {
//        busService.deleteRouteStop(routeId, stopId);
//        return ResponseEntity.noContent().build();
//    }

    // BusRun with Route Endpoint

    @PostMapping("/bus-runs")
    public ResponseEntity<BusRunDto> createBusRun(@Valid @RequestBody BusRunDto busRunDto) {
        try {
            BusRunDto created = busService.createBusRunWithRoute(busRunDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Or a custom error message
        } catch(DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/bus-runs/{busNumber}")
    public ResponseEntity<List<BusRunDto>> getBusRun(@PathVariable String busNumber) {
        try{
            List<BusRunDto> busRunDtos = busService.getBusRunByBusNumber(busNumber);
            return ResponseEntity.ok(busRunDtos);
        } catch(EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/bus-runs")
    public ResponseEntity<BusRunDto> updateBusRun(@Valid @RequestBody BusRunDto busRunDto) {
        try {
            BusRunDto created = busService.updateBusRunWithRouteName(busRunDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Or a custom error message
        } catch(DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }
}












