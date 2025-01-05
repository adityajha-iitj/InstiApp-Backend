package in.ac.iitj.instiapp.Tests.Repository;

import in.ac.iitj.instiapp.Repository.FacultyRepository;
import in.ac.iitj.instiapp.database.entities.User.Faculty.Faculty;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyDetailedDto;
import in.ac.iitj.instiapp.payload.User.Faculty.FacultyBaseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import in.ac.iitj.instiapp.Tests.EntityTestData.UserData;
import in.ac.iitj.instiapp.Tests.Utilities.InitialiseEntities;
import in.ac.iitj.instiapp.Tests.Utilities.Utils;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@Import({InitialiseEntities.class})
public class FacultyTest {

}
