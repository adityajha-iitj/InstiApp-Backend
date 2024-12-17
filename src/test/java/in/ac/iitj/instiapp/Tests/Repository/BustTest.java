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

import static in.ac.iitj.instiapp.Tests.EntityTestData.BusLocationData.*;
import static in.ac.iitj.instiapp.Tests.EntityTestData.BusScheduleData.BUS_SCHEDULE1;
import static in.ac.iitj.instiapp.Tests.EntityTestData.BusScheduleData.BUS_SCHEDULE2;

@DataJpaTest
@Import({BusRepositoryImpl.class})
@Rollback(value = false)
public class BustTest {

    @Autowired
    BusRepository busRepository;

    @BeforeAll
    public static void setUp(@Autowired BusRepository busRepository) {
        busRepository.saveBusLocation(BUS_LOCATION1.toEntity().getName());
        busRepository.saveBusLocation(BUS_LOCATION2.toEntity().getName());

        busRepository.saveBusSchedule(BUS_SCHEDULE1.toEntity());

        BusRun busRun = new BusRun();
        busRun.setBusSchedule(busRepository.getBusSchedule(BUS_SCHEDULE1.busname));
        busRun.setFromLocation(busRepository.getBusLocation(BUS_LOCATION1.name));
        busRun.setToLocation(busRepository.getBusLocation(BUS_LOCATION2.name));
        busRun.setTimeOfDeparture(Time.valueOf(LocalTime.of(5, 30)));
        busRun.setScheduleType(ScheduleType.WEEKDAY);

        busRepository.saveBusRun(busRun);
    }


    @Test
    @Order(1)
    public void testGetListOfBusLocations() {

        Pageable pageable = PageRequest.of(0, 10);
        List<String> locations = busRepository.getListOfBusLocations(pageable);
        Assertions.assertThat(locations).containsExactlyInAnyOrder(BUS_LOCATION1.name,BUS_LOCATION2.name);
    }

    @Test
    @Order(2)
    @Rollback(value = true)
    public void testDeleteBusLocation() {
        busRepository.deleteBusLocation(BUS_LOCATION1.name);
        List<String> locations = busRepository.getListOfBusLocations(PageRequest.of(0, 10));
        Assertions.assertThat(locations).containsExactlyInAnyOrder(BUS_LOCATION2.name);
    }

    @Test
    @Order(3)
    public void testExistsBusLocation() {
        Assertions.assertThat(busRepository.isBusLocationExists(BUS_LOCATION1.name)).isTrue();
        Assertions.assertThat(busRepository.isBusLocationExists(BUS_LOCATION3.name)).isFalse();

    }

    @Test
    @Order(4)
    public void testGetBusSchedules() {
        Pageable pageable = PageRequest.of(0, 10);
        List<BusSchedule> busSchedules = busRepository.getBusSchedules(pageable);
        BusSchedule busSchedule = busSchedules.get(0);
        BusRun busRun = busSchedule.getRuns().stream().findFirst().get();
        org.junit.jupiter.api.Assertions.assertAll(
                () -> Assertions.assertThat(busSchedule.getBusNumber()).isEqualTo(BUS_SCHEDULE1.busname),
                () -> Assertions.assertThat(busRun.getScheduleType()).isEqualTo(ScheduleType.WEEKDAY),
                () -> Assertions.assertThat(busRun.getFromLocation().getName()).isEqualTo(BUS_LOCATION1.name),
                () -> Assertions.assertThat(busRun.getToLocation().getName()).isEqualTo(BUS_LOCATION2.name),
                () -> Assertions.assertThat(busRun.getTimeOfDeparture().toLocalTime()).isEqualTo(LocalTime.of(5, 30))
        );
    }


    @Test
    @Order(5)
    @Rollback(value = true)
    public void testUpdateBusScheduleRun() {
        BusRun br = new BusRun();
        br.setScheduleType(ScheduleType.WEEKDAY);
        br.setFromLocation(busRepository.getBusLocation(BUS_LOCATION2.name));
        br.setToLocation(busRepository.getBusLocation(BUS_LOCATION1.name));
        br.setTimeOfDeparture(Time.valueOf(LocalTime.of(11, 30)));

        busRepository.updateBusScheduleRun(BUS_SCHEDULE1.busname, ScheduleType.WEEKDAY,Time.valueOf(LocalTime.of(5,30)),br);

        Pageable pageable = PageRequest.of(0, 10);
        List<BusSchedule> busSchedules = busRepository.getBusSchedules(pageable);
        BusSchedule busSchedule = busSchedules.get(0);
        BusRun busRun = busSchedule.getRuns().stream().findFirst().get();
        org.junit.jupiter.api.Assertions.assertAll(
                () -> Assertions.assertThat(busSchedule.getBusNumber()).isEqualTo(BUS_SCHEDULE1.busname),
                () -> Assertions.assertThat(busRun.getScheduleType()).isEqualTo(ScheduleType.WEEKDAY),
                () -> Assertions.assertThat(busRun.getFromLocation().getName()).isEqualTo(BUS_LOCATION2.name),
                () -> Assertions.assertThat(busRun.getToLocation().getName()).isEqualTo(BUS_LOCATION1.name),
                () -> Assertions.assertThat(busRun.getTimeOfDeparture().toLocalTime()).isEqualTo(LocalTime.of(11, 30))
        );
    }


    @Test
    @Order(6)
    public  void testBusScheduleExists(){
        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE1.busname)).isTrue();
        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE2.busname)).isFalse();
    }


    @Test
    @Order(7)
    @Rollback(value = true)
    public  void testUpdateBusSchedule(){
        Assertions.assertThatThrownBy(() -> busRepository.updateBusSchedule(BUS_SCHEDULE1.busname, BUS_SCHEDULE1.busname)).isInstanceOf(DataIntegrityViolationException.class);
        busRepository.updateBusSchedule(BUS_SCHEDULE1.busname, BUS_SCHEDULE2.busname);
        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE2.busname)).isTrue();
    }

    @Test
    @Order(8)
    @Rollback(value = true)
    public void testDeleteBusSchedule() {
        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE1.busname)).isTrue();

        busRepository.deleteBusSchedule(BUS_SCHEDULE1.busname);
        // Verify the schedule no longer exists
        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE1.busname)).isFalse();
    }


}
