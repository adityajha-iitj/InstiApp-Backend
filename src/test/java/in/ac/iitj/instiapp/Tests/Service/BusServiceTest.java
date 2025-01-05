//package in.ac.iitj.instiapp.Tests.Service;
//
//
//
//import in.ac.iitj.instiapp.Repository.BusRepository;
//import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusOverride;
//import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
//import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusSchedule;
//import in.ac.iitj.instiapp.mappers.Scheduling.Buses.BusRunDtoMapper;
//import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto;
//import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusScheduleDto;
//import in.ac.iitj.instiapp.services.BusService;
//import in.ac.iitj.instiapp.services.impl.BusServiceImpl;
//import jakarta.persistence.EntityManager;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.annotation.Rollback;
//
//import java.util.Calendar;
//import java.util.List;
//
//import static in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses.BusLocationData.*;
//import static in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses.BusOverrideData.*;
//import static in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses.BusRunData.*;
//import static in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses.BusScheduleData.*;
//import static in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses.BusScheduleData.BUS_SCHEDULE4;
//import static in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses.BusSnippetData.BUS_SNIPPET3;
//import static in.ac.iitj.instiapp.mappers.Scheduling.Buses.BusScheduleDtoMapper.busOverrideDtoMapper;
//
//@SpringBootTest
//@Import({BusServiceImpl.class})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class )
//public class BusServiceTest {
//
//    @Autowired
//    BusService service;
//    @Autowired
//    BusRepository repo;
//    @Autowired
//    EntityManager entityManager;
//    @Autowired
//    private BusRunDtoMapper busRunDtoMapper;
//
//    @BeforeAll
//    public static void setUp(@Autowired BusService service) {
//        service.saveBusLocation(BUS_LOCATION1.toEntity().getName());
//        service.saveBusLocation(BUS_LOCATION2.toEntity().getName());
//        service.saveBusLocation(BUS_LOCATION3.toEntity().getName());
//
//
//        service.saveBusSchedule(BUS_SCHEDULE1.busname);
//        service.saveBusSchedule(BUS_SCHEDULE2.busname);
//
//        service.saveBusRun(BUS_RUN1.toEntity(), BUS_SCHEDULE1.busname);
//        service.saveBusRun(BUS_RUN2.toEntity(), BUS_SCHEDULE2.busname);
//        service.saveBusRun(BUS_RUN3.toEntity(), BUS_SCHEDULE2.busname);
//
//        service.saveBusOverride(BUS_SCHEDULE1.busname, BUS_OVERRIDE1.toEntity());
//        service.saveBusOverride(BUS_SCHEDULE2.busname, BUS_OVERRIDE2.toEntity());
//        service.saveBusOverride(BUS_SCHEDULE1.busname, BUS_OVERRIDE4.toEntity());
//    }
//
//
//    @Test
//    @Order(1)
//    @Rollback(value = true)
//    public void testSaveBusLocation() {
//
//        Assertions.assertThat(service.isBusLocationExist(BUS_LOCATION4.name)).isEqualTo(-1L);
//        Assertions.assertThatThrownBy(
//                () -> service.saveBusLocation(BUS_LOCATION1.name)
//        ).isInstanceOf(DataIntegrityViolationException.class);
//
//    }
//
//    // From Here BusLocation1 , BusLocation2, BusLocation3 exist in database
//
//
//    @Test
//    @Order(2)
//    public void testGetListOfBusLocations() {
//
//        Pageable pageable = PageRequest.of(0, 10);
//        List<String> locations = service.getBusLocations(pageable);
//        Assertions.assertThat(locations).containsExactlyInAnyOrder(BUS_LOCATION1.name, BUS_LOCATION2.name, BUS_LOCATION3.name);
//
//    }
//
//    @Test
//    @Order(3)
//    public void testIsBusLocationExist() {
//        Assertions.assertThat(service.isBusLocationExist(BUS_LOCATION1.name)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(service.isBusLocationExist(BUS_LOCATION4.name)).isEqualTo(-1L);
//    }
//
//
//    @Test
//    @Order(4)
//    @Rollback(value = true) // so that BUS_SCHEDULE3 doesn't get saved
//    public void testSaveBusSchedule() {
//        Assertions.assertThatThrownBy(
//                () -> service.saveBusSchedule(BUS_SCHEDULE1.busname)
//        ).isInstanceOf(DataIntegrityViolationException.class);
//
//        Assertions.assertThat(service.existsBusSchedule(BUS_SCHEDULE3.busname)).isEqualTo(-1L);
//        service.saveBusSchedule(BUS_SCHEDULE3.busname);
//        Assertions.assertThat(service.existsBusSchedule(BUS_SCHEDULE3.busname)).isNotNull().isNotEqualTo(-1L);
//    }
//
//    @Test
//    @Order(5)
//    public void testGetBusSchedule() {
//        Pageable pageable = PageRequest.of(0, 10);
//
//        BusScheduleDto busScheduleDto = service.getBusSchedule(BUS_SCHEDULE1.busname);
//
//        Assertions.assertThatCode(() -> {
//            Assertions.assertThat(busScheduleDto.getBusNumber()).isEqualTo(BUS_SCHEDULE1.busname);
//            Assertions.assertThat(busScheduleDto.getRuns()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("Id").containsExactlyInAnyOrder(
//                    busRunDtoMapper.toDto(BUS_RUN1.toEntity())
//            );
//            Assertions.assertThat(busScheduleDto.getBusOverrides()).isEmpty();
//        }).doesNotThrowAnyException();
//    }
//
//    @Test
//    @Order(6)
//    public void testGetBusNumbers() {
//        Assertions.assertThat(service.getBusNumbers(PageRequest.of(0, 10))).containsExactlyInAnyOrder(BUS_SCHEDULE1.busname, BUS_SCHEDULE2.busname, BUS_SCHEDULE3.busname);
//    }
//
//    @Test
//    @Order(7)
//    public void testExistsBusSchedule() {
//        Assertions.assertThat(service.existsBusSchedule(BUS_SCHEDULE1.busname)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(service.existsBusSchedule(BUS_SCHEDULE2.busname)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(service.existsBusSchedule(BUS_SCHEDULE4.busname)).isEqualTo(-1L);
//    }
//
//
//    @Test
//    @Order(8)
//    @Rollback(value = true)// So That BusRun3 doesn't get saved
//    public void testSaveBusRun() {
//        Assertions.assertThatThrownBy(
//                () -> service.saveBusRun(BUS_RUN1.toEntity(), BUS_SCHEDULE1.busname)
//        ).isInstanceOf(DataIntegrityViolationException.class);
//
//        Assertions.assertThatThrownBy(
//                () -> service.saveBusRun(BUS_RUN4.toEntity(), BUS_SCHEDULE4.busname)
//        ).isInstanceOf(EmptyResultDataAccessException.class);
//
//        Assertions.assertThat(service.existsBusRunByPublicId(BUS_RUN4.publicId)).isFalse();
//        service.saveBusRun(BUS_RUN4.toEntity(), BUS_SCHEDULE2.busname);
//        Assertions.assertThat(service.existsBusRunByPublicId(BUS_RUN4.publicId)).isTrue();
//    }
//
//    @Test
//    @Order(9)
//    public void testExistsBusRun() {
//        Assertions.assertThat(service.existsBusRunByPublicId(BUS_RUN1.publicId)).isTrue();
//        Assertions.assertThat(service.existsBusRunByPublicId(BUS_RUN2.publicId)).isTrue();
//    }
//
//
//    @Test
//    @Order(10)
//    public void testGetBusOverrideForYearAndMonth() {
//        List<BusOverrideDto> busOverrideDtos = service.getBusOverrideForYearAndMonth(2024, 12);
//        Assertions.assertThat(busOverrideDtos.size()).isEqualTo(3);
//
//        Assertions.assertThat(busOverrideDtos.get(0).getDescription()).isEqualTo(BUS_OVERRIDE1.description);
//        Assertions.assertThat(busOverrideDtos.get(0).getPublicId()).isEqualTo(BUS_OVERRIDE1.publicId);
//        Assertions.assertThat(busOverrideDtos.get(0).getBusScheduleBusNumber()).isEqualTo(BUS_SCHEDULE1.busname);
//
//    }
//
//    @Test
//    @Order(11)
//    public void testUpdateBusOverride() {
//        Assertions.assertThatThrownBy(
//                () -> service.updateBusOverride(BUS_OVERRIDE3.publicId, BUS_OVERRIDE1.toEntity())
//        ).isInstanceOf(EmptyResultDataAccessException.class);
//
//        Assertions.assertThatThrownBy(
//                () -> service.updateBusOverride(BUS_OVERRIDE1.publicId, BUS_OVERRIDE4.toEntity())
//        ).isInstanceOf(DataIntegrityViolationException.class);
//
//        Assertions.assertThat(service.existsBusOverrideByPublicId(BUS_OVERRIDE2.publicId)).isTrue();
//        service.updateBusOverride(BUS_OVERRIDE2.publicId, BUS_OVERRIDE4.toEntity());
//
//
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(BUS_OVERRIDE4.overrideDate);
//            List<BusOverrideDto> busOverrideDto = service.getBusOverrideForYearAndMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
//            Assertions.assertThat(busOverrideDto.get(2).getOverrideDate().toInstant()).isEqualTo(BUS_OVERRIDE4.overrideDate.toInstant());
//            Assertions.assertThat(busOverrideDto.get(2).getDescription()).isEqualTo(BUS_OVERRIDE4.description);
//            Assertions.assertThat(busOverrideDto.get(2).getPublicId()).isEqualTo(BUS_OVERRIDE2.publicId);
//            Assertions.assertThat(busOverrideDto.get(2).getBusScheduleBusNumber()).isEqualTo(BUS_SCHEDULE2.busname);
//
//    }
//
//
//    @Test
//    @Order(12)
//    @Rollback(value = true)
//    public void testBusOverrideDelete() {
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(BUS_OVERRIDE1.overrideDate);
//
//        List<BusOverrideDto> busOverrideDtos = service.getBusOverrideForYearAndMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
//        Assertions.assertThat(busOverrideDtos.size()).isEqualTo(3);
//
//        List<String> publicIds = busOverrideDtos.stream().map(BusOverrideDto::getPublicId).toList();
//
//        service.deleteBusOverride(publicIds);
//        busOverrideDtos = service.getBusOverrideForYearAndMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
//        Assertions.assertThat(busOverrideDtos.size()).isEqualTo(0);
//    }
//
//
//
//    @Test
//    @Order(13)
//    public void testUpdateBusScheduleRun() {
//        Assertions.assertThat(service.existsBusRunByPublicId(BUS_RUN1.publicId)).isTrue();
//
//        Assertions.assertThatThrownBy(() -> service.updateBusScheduleRun(BUS_RUN2.publicId, BUS_RUN3.toEntity())).isInstanceOf(DataIntegrityViolationException.class);
//
//        service.updateBusScheduleRun(BUS_RUN1.publicId, BUS_RUN3.toEntity());
//
//        Assertions.assertThatCode(() -> {
//            Assertions.assertThat(service.existsBusRunByPublicId(BUS_RUN1.publicId)).isTrue();
//            BusRun br = entityManager.createQuery("select br from BusRun br where br.publicId = :publicId", BusRun.class).setParameter("publicId", BUS_RUN1.publicId).getSingleResult();
//            Assertions.assertThat(br.getBusSchedule().getBusNumber()).isEqualTo(BUS_SCHEDULE1.busname);
//            Assertions.assertThat(br.getScheduleType()).isEqualTo(BUS_RUN3.scheduleType);
//            Assertions.assertThat(br.getBusSnippet().getTimeOfDeparture()).isEqualTo(BUS_RUN3.busSnippet.timeOfDeparture);
//            Assertions.assertThat(br.getBusSnippet().getFromLocation().getName()).isEqualTo(BUS_RUN3.busSnippet.fromBusLocation.name);
//            Assertions.assertThat(br.getBusSnippet().getToLocation().getName()).isEqualTo(BUS_RUN3.busSnippet.toBusLocation.name);
//        }).doesNotThrowAnyException();
//    }
//
//    @Test
//    @Order(14)
//    @Rollback(value = true)
//    public void testDeleteBusRun() {
//        Assertions.assertThat(service.existsBusRunByPublicId(BUS_RUN1.publicId)).isTrue();
//        Assertions.assertThat(service.existsBusRunByPublicId(BUS_RUN2.publicId)).isTrue();
//        service.deleteBusRuns(List.of(BUS_RUN1.publicId, BUS_RUN2.publicId));
//        Assertions.assertThat(service.existsBusRunByPublicId(BUS_RUN1.publicId)).isFalse();
//        Assertions.assertThat(service.existsBusRunByPublicId(BUS_RUN2.publicId)).isFalse();
//
//    }
//    @Test
//    @Order(15)
//    @Rollback(value = true)
//    public void testUpdateBusLocation() {
//
//        // Checking if the locations assumed to be in database exist in database and not in database
//        Assertions.assertThat(service.isBusLocationExist(BUS_LOCATION1.name)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(service.isBusLocationExist(BUS_LOCATION2.name)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(service.isBusLocationExist(BUS_LOCATION4.name)).isEqualTo(-1L);
//
//        // If new BusLocation name exist in database
//        Assertions.assertThatThrownBy(
//                () -> service.updateBusLocation(BUS_LOCATION1.name, BUS_LOCATION2.name)
//        ).isInstanceOf(DataIntegrityViolationException.class);
//
//        // If OldBusLocationName doesn't exist in database
//        Assertions.assertThatThrownBy(
//                () -> service.updateBusLocation(BUS_LOCATION4.name, BUS_LOCATION4.name)
//        ).isInstanceOf(EmptyResultDataAccessException.class);
//
//        service.updateBusLocation(BUS_LOCATION1.name, BUS_LOCATION4.name);
//
//        Assertions.assertThat(service.isBusLocationExist(BUS_LOCATION1.name)).isEqualTo(-1L);
//        Assertions.assertThat(service.isBusLocationExist(BUS_LOCATION4.name)).isNotNull().isNotEqualTo(-1L);
//    }
//
//
//    @Test
//    @Order(16)
//    @Rollback(value = true)
//    public void testDeleteBusLocation() {
//
//        //Checking if location exist in database
//        Assertions.assertThat(service.isBusLocationExist(BUS_LOCATION2.name)).isNotNull().isNotEqualTo(-1L);
//
//        service.deleteBusLocation(BUS_LOCATION2.name);
//
//        Assertions.assertThat(service.isBusLocationExist(BUS_LOCATION2.name)).isEqualTo(-1L);
//
//        /* Checking if cascading has worked
//         * Since all the busruns and busoverride exist on bussnippet1 and bussnippet2 which inturn
//         * depend on buslocation1 the database should be empty for busrun and busoverride
//         */
//
//
////        Since busrun and busoverride dependent on busrun they should also get deleted. due to cascading
//
//        Assertions.assertThatCode(() -> {
//            Assertions.assertThat(service.existsBusRunByPublicId(BUS_RUN1.publicId)).isFalse();
//            Assertions.assertThat(service.existsBusRunByPublicId(BUS_RUN2.publicId)).isFalse();
//        }).doesNotThrowAnyException();
//
//
//        Assertions.assertThat(service.existsBusOverrideByPublicId(BUS_OVERRIDE1.publicId)).isFalse();
//    }
//    @Test
//    @Order(17)
//    @Rollback(value = true)
//    public void testUpdateBusSchedule() {
//        Assertions.assertThatThrownBy(
//                () -> service.updateBusSchedule(BUS_SCHEDULE1.busname, BUS_SCHEDULE2.busname)
//        ).isInstanceOf(DataIntegrityViolationException.class);
//
//        //Before Update
//        Assertions.assertThat(service.existsBusSchedule(BUS_SCHEDULE2.busname)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(service.existsBusSchedule(BUS_SCHEDULE4.busname)).isEqualTo(-1L);
//
//        service.updateBusSchedule(BUS_SCHEDULE2.busname, BUS_SCHEDULE4.busname);
//
//        //After Update
//        Assertions.assertThat(service.existsBusSchedule(BUS_SCHEDULE4.busname)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(service.existsBusSchedule(BUS_SCHEDULE2.busname)).isEqualTo(-1L);
//    }
//
//    @Test
//    @Order(18)
//    @Rollback(value = true)
//    public void testDeleteBusSchedule() {
//
//        //Also testing for cascading deletes
//        Assertions.assertThat(service.existsBusSchedule(BUS_SCHEDULE1.busname)).isNotNull().isNotEqualTo(-1L);
//        service.deleteBusSchedule(BUS_SCHEDULE1.busname);
//        Assertions.assertThat(service.existsBusSchedule(BUS_SCHEDULE1.busname)).isEqualTo(-1L);
//
//    }
//}
