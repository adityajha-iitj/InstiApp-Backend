package in.ac.iitj.instiapp.controllers;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MenuOverrideDto;
import in.ac.iitj.instiapp.payload.Scheduling.MessMenu.MessMenuDto;
import in.ac.iitj.instiapp.services.MessService;
import jakarta.persistence.NamedQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class MessController {

    private final MessService messService;

    @Autowired
    public MessController(MessService messService) {
        this.messService = messService;
    }

    /*----------------------------------MESS MENU-------------------------------------------------------------------------*/
    @PostMapping("/mess-menu")
    public void saveMessMenu(@RequestBody MessMenuDto menu) {
        messService.saveMessMenu(menu);
    }

    @GetMapping("/mess-menu")
    public List<MessMenuDto> getMessMenu(@RequestParam Integer year, @RequestParam Integer   month) {
        return messService.getMessMenu(year, month);
    }

    @PutMapping("/mess-menu")
    public void updateMessMenu(@RequestBody MessMenuDto menu) {
        messService.updateMessMenu(menu);

    }

    @DeleteMapping("/mess-menu")
    public void deleteMessMenu(@RequestBody Map<String, Object> requestBody) {
        try{
            Integer year = (Integer) requestBody.get("year");
            Integer month = (Integer) requestBody.get("month");
            Integer day = (Integer) requestBody.get("day");
            messService.deleteMessMenu(year, month, day);

        }
        catch(ClassCastException e){
            throw e;
        }
    }

    /*----------------------------------------------------------MENU OVERRIDE---------------------------------------------*/
    @PostMapping("/menu-override")
    public void saveOverrideMessMenu(@RequestBody MenuOverrideDto menu) {
        messService.saveOverrideMessMenu(menu);
    }

    @GetMapping("/menu-override")
    public MenuOverrideDto getOverrideMessMenu(@RequestParam String date) {
        try {
            // Parse the input date string into a Date object
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format as needed
            Date parsedDate = dateFormat.parse(date);

            // Pass the parsed Date to the service layer
            return messService.getOverrideMessMenu(parsedDate);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd'.", e);
        }
    }

    @PutMapping("/menu-override")
    public void updateOverrideMessMenu(@RequestBody MenuOverrideDto menu) {
        messService.updateOverrideMessMenu(menu);
    }

    @DeleteMapping("/menu-override")
    public void deleteOverrideMessMenu(@RequestParam Date date) {
//        try {
            // Parse the input date string into a Date object
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format as needed
//            Date parsedDate = dateFormat.parse(date);
            // Pass the parsed Date to the service layer
            messService.deleteOverrideMessMenu(date);
//        } catch (ParseException e) {
//            throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd'.", e);
//        }

    }

}