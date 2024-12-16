package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.BusRepository;
import in.ac.iitj.instiapp.Repository.impl.BusRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.ScheduleType;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@DataJpaTest
@Import({BusRepositoryImpl.class})
@Rollback(value = false)
public class BustTest {

    @Autowired
    BusRepository busRepository;

    @BeforeAll
    public static void setUp(@Autowired BusRepository busRepository) {
        busRepository.saveBusLocation("MBM College");
        busRepository.saveBusLocation("IIT Jodhpur");

        busRepository.saveBusSchedule(new BusSchedule("B1"));

        BusRun busRun = new BusRun();
        busRun.setBusSchedule(busRepository.getBusSchedule("B1"));
        busRun.setFromLocation(busRepository.getBusLocation("MBM College"));
        busRun.setToLocation(busRepository.getBusLocation("IIT Jodhpur"));
        busRun.setTimeOfDeparture(Time.valueOf(LocalTime.of(5, 30)));
        busRun.setScheduleType(ScheduleType.WEEKDAY);

        busRepository.saveBusRun(busRun);
    }


    @Test
    @Order(1)
    public void testGetListOfBusLocations() {

        Pageable pageable = PageRequest.of(0, 10);
        List<String> locations = busRepository.getListOfBusLocations(pageable);
        Assertions.assertThat(locations).containsExactlyInAnyOrder("MBM College", "IIT Jodhpur");
    }

    @Test
    @Order(2)
    @Rollback(value = true)
    public void testDeleteBusLocation() {
        busRepository.deleteBusLocation("MBM College");
        List<String> locations = busRepository.getListOfBusLocations(PageRequest.of(0, 10));
        Assertions.assertThat(locations).containsExactlyInAnyOrder("IIT Jodhpur");
    }

    @Test
    @Order(3)
    public void testExistsBusLocation() {
        Assertions.assertThat(busRepository.isBusLocationExists("MBM College")).isTrue();
        Assertions.assertThat(busRepository.isBusLocationExists("Railway Station")).isFalse();

    }

    @Test
    @Order(4)
    public void testGetBusSchedules() {
        Pageable pageable = PageRequest.of(0, 10);
        List<BusSchedule> busSchedules = busRepository.getBusSchedules(pageable);
        BusSchedule busSchedule = busSchedules.get(0);
        BusRun busRun = busSchedule.getRuns().stream().findFirst().get();
        org.junit.jupiter.api.Assertions.assertAll(
                () -> Assertions.assertThat(busSchedule.getBusNumber()).isEqualTo("B1"),
                () -> Assertions.assertThat(busRun.getScheduleType()).isEqualTo(ScheduleType.WEEKDAY),
                () -> Assertions.assertThat(busRun.getFromLocation().getName()).isEqualTo("MBM College"),
                () -> Assertions.assertThat(busRun.getToLocation().getName()).isEqualTo("IIT Jodhpur"),
                () -> Assertions.assertThat(busRun.getTimeOfDeparture().toLocalTime()).isEqualTo(LocalTime.of(5, 30))
        );
    }


    @Test
    @Order(5)
    @Rollback(value = true)
    public void testUpdateBusScheduleRun() {
        BusRun br = new BusRun();
        br.setScheduleType(ScheduleType.WEEKDAY);
        br.setFromLocation(busRepository.getBusLocation("IIT Jodhpur"));
        br.setToLocation(busRepository.getBusLocation("MBM College"));
        br.setTimeOfDeparture(Time.valueOf(LocalTime.of(11, 30)));

        busRepository.updateBusScheduleRun("B1",ScheduleType.WEEKDAY,Time.valueOf(LocalTime.of(5,30)),br);

        Pageable pageable = PageRequest.of(0, 10);
        List<BusSchedule> busSchedules = busRepository.getBusSchedules(pageable);
        BusSchedule busSchedule = busSchedules.get(0);
        BusRun busRun = busSchedule.getRuns().stream().findFirst().get();
        org.junit.jupiter.api.Assertions.assertAll(
                () -> Assertions.assertThat(busSchedule.getBusNumber()).isEqualTo("B1"),
                () -> Assertions.assertThat(busRun.getScheduleType()).isEqualTo(ScheduleType.WEEKDAY),
                () -> Assertions.assertThat(busRun.getFromLocation().getName()).isEqualTo("IIT Jodhpur"),
                () -> Assertions.assertThat(busRun.getToLocation().getName()).isEqualTo("MBM College"),
                () -> Assertions.assertThat(busRun.getTimeOfDeparture().toLocalTime()).isEqualTo(LocalTime.of(11, 30))
        );
    }


    @Test
    @Order(6)
    public  void testBusScheduleExists(){
        Assertions.assertThat(busRepository.existsBusSchedule("B1")).isTrue();
        Assertions.assertThat(busRepository.existsBusSchedule("B2")).isFalse();
    }


    @Test
    @Order(7)
    @Rollback(value = true)
    public  void testUpdateBusSchedule(){
        Assertions.assertThatThrownBy(() -> busRepository.updateBusSchedule("B1","B1")).isInstanceOf(DataIntegrityViolationException.class);
        busRepository.updateBusSchedule("B1","B2");
        Assertions.assertThat(busRepository.existsBusSchedule("B2")).isTrue();
    }

    @Test
    @Order(8)
    @Rollback(value = true)
    public void testDeleteBusSchedule() {
        Assertions.assertThat(busRepository.existsBusSchedule("B1")).isTrue();

        busRepository.deleteBusSchedule("B1");
        // Verify the schedule no longer exists
        Assertions.assertThat(busRepository.existsBusSchedule("B1")).isFalse();
    }


}
