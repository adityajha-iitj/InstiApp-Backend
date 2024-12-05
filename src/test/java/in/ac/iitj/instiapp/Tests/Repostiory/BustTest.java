package in.ac.iitj.instiapp.Tests.Repostiory;


import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.Repository.impl.BusRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@DataJpaTest
@Import({BusRepositoryImpl.class})
public class BustTest {

@Autowired
    BusRepository busRepository;

    @Test
    @Order(1)
    public void getListOfBusLocations(){
        busRepository.saveBusLocation("MBM College");
        busRepository.saveBusLocation("IIT Jodhpur");
        Pageable pageable = PageRequest.of(0,10);
        List<String> locations = busRepository.getListOfBusLocations(pageable);
        Assertions.assertThat(locations).containsExactlyInAnyOrder("MBM College", "IIT Jodhpur");
    }

    @Test
    @Order(2)
    public void deleteBusLocation(){
        busRepository.deleteBusLocation("MBM College");
        List<String> locations = busRepository.getListOfBusLocations(PageRequest.of(0,10));
        Assertions.assertThat(locations).containsExactlyInAnyOrder("IIT Jodhpur");
    }



}
