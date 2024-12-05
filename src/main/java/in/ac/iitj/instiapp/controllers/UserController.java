package in.ac.iitj.instiapp.controllers;

import in.ac.iitj.instiapp.Repository.LostnFoundRepository;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    LostnFoundRepository lostnFoundRepository;

     @GetMapping("/")
    public ResponseEntity<?> test(){
         Pageable pageable = PageRequest.of(0,10);
        return  new ResponseEntity<>(lostnFoundRepository.getListOfLocationsName(pageable), HttpStatus.OK);
    }


}
