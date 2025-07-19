package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    @Query("SELECT d.username FROM DeviceToken d WHERE d.token = :token")
    public String findUserIdByToken(@Param("token") String token);

    @Query("SELECT d FROM DeviceToken d WHERE d.username = :username")
    public DeviceToken getAllDeviceTokens(String username);
}
