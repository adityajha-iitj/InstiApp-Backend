package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.UserAvatarRepository;
import in.ac.iitj.instiapp.database.entities.Media.UserAvatar;
import jakarta.persistence.EntityManager;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserAvatarRepositoryImpl implements UserAvatarRepository {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;


    public UserAvatarRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Long save(UserAvatar userAvatar) {
      return   jdbcTemplate.queryForObject("INSERT into user_avatar (type, public_id, asset_id, public_url) values(?, ?, ?, ?) RETURNING  id",Long.class,userAvatar.getType().name(), userAvatar.getPublicId(), userAvatar.getAssetId(), userAvatar.getPublicUrl());
    }

    @Override
    public String updateByPublicIdReturningOldAssetId(String publicId, UserAvatar userAvatar) {
        return jdbcTemplate.queryForObject("UPDATE user_avatar SET public_id = ?, asset_id = ?, public_url = ? WHERE public_id = ? RETURNING (SELECT asset_id FROM user_avatar WHERE public_id = ? LIMIT 1) AS oldAssetId;", String.class, userAvatar.getPublicId(), userAvatar.getAssetId(), userAvatar.getPublicUrl(), publicId,publicId);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM user_avatar WHERE id = ?", id);
    }



}
