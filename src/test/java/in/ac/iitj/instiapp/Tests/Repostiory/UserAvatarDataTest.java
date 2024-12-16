package in.ac.iitj.instiapp.Tests.Repostiory;


import in.ac.iitj.instiapp.Repository.UserAvatarRepository;
import in.ac.iitj.instiapp.Repository.impl.UserAvatarRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.Media.UserAvatar;
import jakarta.persistence.NoResultException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import static in.ac.iitj.instiapp.Tests.EntityTestData.UserAvatarData.*;

@DataJpaTest
@Import({UserAvatarRepositoryImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserAvatarDataTest {


    @Autowired
    UserAvatarRepository userAvatarRepository;

    @Autowired
    TestEntityManager entityManager;

    @BeforeAll
    public static void setUp(@Autowired UserAvatarRepository userAvatarRepository) {
        userAvatarRepository.save(USER_AVATAR1.toEntity());
        userAvatarRepository.save(USER_AVATAR2.toEntity());
    }

    public UserAvatar getUserAvatar(String publicId) {
        return entityManager.getEntityManager().createQuery("SELECT u from UserAvatar u where publicId= :publicId", UserAvatar.class).setParameter("publicId", publicId).getSingleResult();
    }

    @Test
    @Order(1)
    public void testUpdateByPublicIdReturningOldAssetId() {
        String OLD_ASSET_ID = userAvatarRepository.updateByPublicIdReturningOldAssetId(USER_AVATAR1.publicId, USER_AVATAR3.toEntity());

        Assertions.assertThat(OLD_ASSET_ID).isEqualTo(USER_AVATAR1.assetId);

        Assertions.assertThat(getUserAvatar(USER_AVATAR3.publicId))
                .usingRecursiveComparison()
                .ignoringFields("Id")
                .isEqualTo(USER_AVATAR3.toEntity());
    }

    @Test
    @Order(2)
    public void testDeleteById() {
        userAvatarRepository.deleteById(
                getUserAvatar(USER_AVATAR2.publicId).getId()
        );

        Assertions.assertThatThrownBy(() -> getUserAvatar(USER_AVATAR2.publicId)).isInstanceOf(NoResultException.class);
    }

}
