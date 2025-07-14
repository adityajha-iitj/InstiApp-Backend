//package in.ac.iitj.instiapp.Tests.Repository;
//
//import in.ac.iitj.instiapp.Repository.BusRepository;
//import in.ac.iitj.instiapp.Repository.impl.BusRepositoryImpl;
//import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusOverride;
//import in.ac.iitj.instiapp.database.entities.Scheduling.Buses.BusRun;
//import in.ac.iitj.instiapp.mappers.Scheduling.Buses.*;
//import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusOverrideDto;
//import in.ac.iitj.instiapp.payload.Scheduling.Buses.BusScheduleDto;
//    import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
//import static in.ac.iitj.instiapp.Tests.EntityTestData.Scheduling.Buses.BusSnippetData.BUS_SNIPPET3;
//
//@DataJpaTest
//@Import({BusRepositoryImpl.class, BusRunDtoMapperImpl.class, BusLocationDtoMapperImpl.class, BusOverrideDtoMapperImpl.class})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class BusTest {
//
//    @Autowired
//    BusRepository busRepository;
//    @Autowired
//    BusRunDtoMapper busRunDtoMapper;
//    @Autowired
//    BusOverrideDtoMapper busOverrideDtoMapper;
//
//    @Autowired
//    TestEntityManager entityManager;
//
//    @BeforeAll
//    public static void setUp(@Autowired BusRepository busRepository) {
//        busRepository.saveBusLocation(BUS_LOCATION1.toEntity().getName());
//        busRepository.saveBusLocation(BUS_LOCATION2.toEntity().getName());
//        busRepository.saveBusLocation(BUS_LOCATION3.toEntity().getName());
//
//
//        busRepository.saveBusSchedule(BUS_SCHEDULE1.busname);
//        busRepository.saveBusSchedule(BUS_SCHEDULE2.busname);
//
//        busRepository.saveBusRun(BUS_RUN1.toEntity(), BUS_SCHEDULE1.busname);
//        busRepository.saveBusRun(BUS_RUN2.toEntity(), BUS_SCHEDULE2.busname);
//        busRepository.saveBusRun(BUS_RUN3.toEntity(), BUS_SCHEDULE2.busname);
//
//        busRepository.saveBusOverride(BUS_SCHEDULE1.busname, BUS_OVERRIDE1.toEntity());
//        busRepository.saveBusOverride(BUS_SCHEDULE2.busname, BUS_OVERRIDE2.toEntity());
//        busRepository.saveBusOverride(BUS_SCHEDULE1.busname, BUS_OVERRIDE4.toEntity());
//    }
//
//
//    @Test
//    @Order(1)
//    @Rollback(value = true)
//    public void testSaveBusLocation() {
//
//        Assertions.assertThat(busRepository.isBusLocationExists(BUS_LOCATION4.name)).isEqualTo(-1L);
//        Assertions.assertThatThrownBy(
//                () -> busRepository.saveBusLocation(BUS_LOCATION1.name)
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
//        List<String> locations = busRepository.getListOfBusLocations(pageable);
//        Assertions.assertThat(locations).containsExactlyInAnyOrder(BUS_LOCATION1.name, BUS_LOCATION2.name, BUS_LOCATION3.name);
//
//    }
//
//    @Test
//    @Order(3)
//    public void testIsBusLocationExist() {
//        Assertions.assertThat(busRepository.isBusLocationExists(BUS_LOCATION1.name)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(busRepository.isBusLocationExists(BUS_LOCATION4.name)).isEqualTo(-1L);
//    }
//
//    @Test
//    @Order(4)
//    @Rollback(value = true)
//    public void testUpdateBusLocation() {
//
//        // Checking if the locations assumed to be in database exist in database and not in database
//        Assertions.assertThat(busRepository.isBusLocationExists(BUS_LOCATION1.name)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(busRepository.isBusLocationExists(BUS_LOCATION2.name)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(busRepository.isBusLocationExists(BUS_LOCATION4.name)).isEqualTo(-1L);
//
//        // If new BusLocation name exist in database
//        Assertions.assertThatThrownBy(
//                () -> busRepository.updateBusLocation(BUS_LOCATION1.name, BUS_LOCATION2.name)
//        ).isInstanceOf(DataIntegrityViolationException.class);
//
//        // If OldBusLocationName doesn't exist in database
//        Assertions.assertThatThrownBy(
//                () -> busRepository.updateBusLocation(BUS_LOCATION4.name, BUS_LOCATION4.name)
//        ).isInstanceOf(EmptyResultDataAccessException.class);
//
//        busRepository.updateBusLocation(BUS_LOCATION1.name, BUS_LOCATION4.name);
//
//        Assertions.assertThat(busRepository.isBusLocationExists(BUS_LOCATION1.name)).isEqualTo(-1L);
//        Assertions.assertThat(busRepository.isBusLocationExists(BUS_LOCATION4.name)).isNotNull().isNotEqualTo(-1L);
//    }
//
//
//    @Test
//    @Order(5)
//    @Rollback(value = true)
//    public void testDeleteBusLocation() {
//
//        //Checking if location exist in database
//        Assertions.assertThat(busRepository.isBusLocationExists(BUS_LOCATION2.name)).isNotNull().isNotEqualTo(-1L);
//
//        busRepository.deleteBusLocation(BUS_LOCATION2.name);
//
//        Assertions.assertThat(busRepository.isBusLocationExists(BUS_LOCATION2.name)).isEqualTo(-1L);
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
//            Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN1.publicId)).isFalse();
//            Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN2.publicId)).isFalse();
//        }).doesNotThrowAnyException();
//
//
//        Assertions.assertThat(busRepository.existsBusOverrideByPublicId(BUS_OVERRIDE1.publicId)).isFalse();
//    }
//
//
//    @Test
//    @Order(6)
//    @Rollback(value = true) // so that BUS_SCHEDULE3 doesn't get saved
//    public void testSaveBusSchedule() {
//        Assertions.assertThatThrownBy(
//                () -> busRepository.saveBusSchedule(BUS_SCHEDULE1.busname)
//        ).isInstanceOf(DataIntegrityViolationException.class);
//
//        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE3.busname)).isEqualTo(-1L);
//        busRepository.saveBusSchedule(BUS_SCHEDULE3.busname);
//        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE3.busname)).isNotNull().isNotEqualTo(-1L);
//    }
//
//    @Test
//    @Order(7)
//    public void testGetBusSchedule() {
//        Pageable pageable = PageRequest.of(0, 10);
//
//        BusScheduleDto busScheduleDto = busRepository.getBusSchedule(BUS_SCHEDULE1.busname);
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
//    @Order(8)
//    public void testGetBusNumbers() {
//        Assertions.assertThat(busRepository.getBusNumbers(PageRequest.of(0, 10))).containsExactlyInAnyOrder(BUS_SCHEDULE1.busname, BUS_SCHEDULE2.busname);
//    }
//
//    @Test
//    @Order(9)
//    public void testExistsBusSchedule() {
//        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE1.busname)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE2.busname)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE3.busname)).isEqualTo(-1L);
//    }
//
//
//    @Test
//    @Order(10)
//    @Rollback(value = true)
//    public void testUpdateBusSchedule() {
//        Assertions.assertThatThrownBy(
//                () -> busRepository.updateBusSchedule(BUS_SCHEDULE3.busname, BUS_SCHEDULE4.busname)
//        ).isInstanceOf(EmptyResultDataAccessException.class);
//
//        Assertions.assertThatThrownBy(
//                () -> busRepository.updateBusSchedule(BUS_SCHEDULE1.busname, BUS_SCHEDULE2.busname)
//        ).isInstanceOf(DataIntegrityViolationException.class);
//
//        //Before Update
//        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE2.busname)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE3.busname)).isEqualTo(-1L);
//
//        busRepository.updateBusSchedule(BUS_SCHEDULE2.busname, BUS_SCHEDULE3.busname);
//
//        //After Update
//        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE3.busname)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE2.busname)).isEqualTo(-1L);
//    }
//
//    @Test
//    @Order(11)
//    @Rollback(value = true)
//    public void testDeleteBusSchedule() {
//
//        //Also testing for cascading deletes
//        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE1.busname)).isNotNull().isNotEqualTo(-1L);
//        Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN1.publicId)).isTrue();
//        Assertions.assertThat(busRepository.existsBusOverrideByPublicId(BUS_OVERRIDE1.publicId)).isTrue();
//
//        busRepository.deleteBusSchedule(BUS_SCHEDULE1.busname);
//
//        Assertions.assertThat(busRepository.existsBusSchedule(BUS_SCHEDULE1.busname)).isEqualTo(-1L);
//        Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN1.publicId)).isFalse();
//        Assertions.assertThat(busRepository.existsBusOverrideByPublicId(BUS_OVERRIDE1.publicId)).isFalse();
//
//
//    }
//
//    @Test
//    @Order(12)
//    @Rollback(value = true)// So That BusRun3 doesn't get saved
//    public void testSaveBusRun() {
//        Assertions.assertThatThrownBy(
//                () -> busRepository.saveBusRun(BUS_RUN1.toEntity(), BUS_SCHEDULE1.busname)
//        ).isInstanceOf(DataIntegrityViolationException.class);
//
//        Assertions.assertThatThrownBy(
//                () -> busRepository.saveBusRun(BUS_RUN4.toEntity(), BUS_SCHEDULE4.busname)
//        ).isInstanceOf(EmptyResultDataAccessException.class);
//
//        Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN4.publicId)).isFalse();
//        busRepository.saveBusRun(BUS_RUN4.toEntity(), BUS_SCHEDULE2.busname);
//        Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN4.publicId)).isTrue();
//    }
//
//    @Test
//    @Order(13)
//    public void testExistsBusRun() {
//        Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN1.publicId)).isTrue();
//        Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN2.publicId)).isTrue();
//        Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN4.publicId)).isFalse();
//    }
//
//
//    @Test
//    @Order(14)
//    public void testUpdateBusScheduleRun() {
//        Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN1.publicId)).isTrue();
//
//        Assertions.assertThatThrownBy(() -> busRepository.updateBusScheduleRun(BUS_RUN4.publicId, BUS_RUN4.toEntity())).isInstanceOf(EmptyResultDataAccessException.class);
//
//        Assertions.assertThatThrownBy(() -> busRepository.updateBusScheduleRun(BUS_RUN2.publicId, BUS_RUN3.toEntity())).isInstanceOf(DataIntegrityViolationException.class);
//
//        busRepository.updateBusScheduleRun(BUS_RUN1.publicId, BUS_RUN3.toEntity());
//
//        Assertions.assertThatCode(() -> {
//            Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN1.publicId)).isTrue();
//            BusRun br = entityManager.getEntityManager().createQuery("select br from BusRun br where br.publicId = :publicId", BusRun.class).setParameter("publicId", BUS_RUN1.publicId).getSingleResult();
//            Assertions.assertThat(br.getBusSchedule().getBusNumber()).isEqualTo(BUS_SCHEDULE1.busname);
//            Assertions.assertThat(br.getScheduleType()).isEqualTo(BUS_RUN3.scheduleType);
//            Assertions.assertThat(br.getBusSnippet().getTimeOfDeparture()).isEqualTo(BUS_RUN3.busSnippet.timeOfDeparture);
//            Assertions.assertThat(br.getBusSnippet().getFromLocation().getName()).isEqualTo(BUS_RUN3.busSnippet.fromBusLocation.name);
//            Assertions.assertThat(br.getBusSnippet().getToLocation().getName()).isEqualTo(BUS_RUN3.busSnippet.toBusLocation.name);
//        }).doesNotThrowAnyException();
//    }
//
//    @Test
//    @Order(15)
//    @Rollback(value = true)
//    public void testDeleteBusRun() {
//        Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN1.publicId)).isTrue();
//        Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN2.publicId)).isTrue();
//        busRepository.deleteBusRuns(List.of(BUS_RUN1.publicId, BUS_RUN2.publicId));
//        Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN1.publicId)).isFalse();
//        Assertions.assertThat(busRepository.existsBusRunByPublicId(BUS_RUN2.publicId)).isFalse();
//
//    }
//
//
//    @Test
//    @Order(16)
//    @Rollback(value = true)
//    public void testSaveBusOverride() {
//        Assertions.assertThatThrownBy(
//                () -> busRepository.saveBusOverride(BUS_SCHEDULE1.busname, BUS_OVERRIDE1.toEntity())
//        ).isInstanceOf(DataIntegrityViolationException.class);
//
//        Assertions.assertThatThrownBy(
//                () -> busRepository.saveBusOverride(BUS_SCHEDULE4.busname, BUS_OVERRIDE2.toEntity())
//        ).isInstanceOf(EmptyResultDataAccessException.class);
//
//        Assertions.assertThat(busRepository.existsBusOverrideByPublicId(BUS_OVERRIDE3.publicId)).isFalse();
//        busRepository.saveBusOverride(BUS_SCHEDULE1.busname, BUS_OVERRIDE3.toEntity());
//        Assertions.assertThat(busRepository.existsBusOverrideByPublicId(BUS_OVERRIDE3.publicId)).isTrue();
//    }
//
//    @Test
//    @Order(17)
//    public void testGetBusOverrideForYearAndMonth() {
//        List<BusOverrideDto> busOverrideDtos = busRepository.getBusOverrideForYearAndMonth(2024, 12);
//        Assertions.assertThat(busOverrideDtos.size()).isEqualTo(3);
//        Assertions.assertThatCode(
//                () -> {
//                    BusOverride busOverride = busOverrideDtoMapper.toEntity(busOverrideDtos.get(0));
//                    Assertions.assertThat(busOverride.getBusSnippet())
//                            .usingRecursiveComparison()
//                            .ignoringFields("id")
//                            .isEqualTo(BUS_SNIPPET3.toEntity());
//                    Assertions.assertThat(busOverride.getOverrideDate().toInstant()).isEqualTo(BUS_OVERRIDE1.overrideDate.toInstant());
//                    Assertions.assertThat(busOverride.getDescription()).isEqualTo(BUS_OVERRIDE1.description);
//                    Assertions.assertThat(busOverride.getPublicId()).isEqualTo(BUS_OVERRIDE1.publicId);
//                    Assertions.assertThat(busOverride.getBusSchedule().getBusNumber()).isEqualTo(BUS_SCHEDULE1.busname);
//
//                }
//        ).doesNotThrowAnyException();
//    }
//
//    @Test
//    @Order(18)
//    public void testUpdateBusOverride() {
//        Assertions.assertThatThrownBy(
//                () -> busRepository.updateBusOverride(BUS_OVERRIDE3.publicId, BUS_OVERRIDE1.toEntity())
//        ).isInstanceOf(EmptyResultDataAccessException.class);
//
//        Assertions.assertThatThrownBy(
//                () -> busRepository.updateBusOverride(BUS_OVERRIDE1.publicId, BUS_OVERRIDE4.toEntity())
//        ).isInstanceOf(DataIntegrityViolationException.class);
//
//        Assertions.assertThat(busRepository.existsBusOverrideByPublicId(BUS_OVERRIDE2.publicId)).isTrue();
//        busRepository.updateBusOverride(BUS_OVERRIDE2.publicId, BUS_OVERRIDE4.toEntity());
//
//        Assertions.assertThatCode(() -> {
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(BUS_OVERRIDE4.overrideDate);
//            List<BusOverrideDto> busOverrideDto = busRepository.getBusOverrideForYearAndMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
//            BusOverride busOverride = busOverrideDtoMapper.toEntity(busOverrideDto.get(2));
//            Assertions.assertThat(busOverride.getBusSnippet())
//                    .usingRecursiveComparison()
//                    .ignoringFields("id")
//                    .isEqualTo(BUS_SNIPPET3.toEntity());
//            Assertions.assertThat(busOverride.getOverrideDate().toInstant()).isEqualTo(BUS_OVERRIDE4.overrideDate.toInstant());
//            Assertions.assertThat(busOverride.getDescription()).isEqualTo(BUS_OVERRIDE4.description);
//            Assertions.assertThat(busOverride.getPublicId()).isEqualTo(BUS_OVERRIDE2.publicId);
//            Assertions.assertThat(busOverride.getBusSchedule().getBusNumber()).isEqualTo(BUS_SCHEDULE2.busname);
//
//        }).doesNotThrowAnyException();
//    }
//
//
//    @Test
//    @Order(19)
//    @Rollback(value = true)
//    public void testBusOverrideDelete() {
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(BUS_OVERRIDE1.overrideDate);
//
//        List<BusOverrideDto> busOverrideDtos = busRepository.getBusOverrideForYearAndMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
//        Assertions.assertThat(busOverrideDtos.size()).isEqualTo(3);
//
//        List<String> publicIds = busOverrideDtos.stream().map(BusOverrideDto::getPublicId).toList();
//
//        busRepository.deleteBusOverride(publicIds);
//        busOverrideDtos = busRepository.getBusOverrideForYearAndMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
//        Assertions.assertThat(busOverrideDtos.size()).isEqualTo(0);
//    }
//
//
//}
