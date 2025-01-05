package in.ac.iitj.instiapp.Repository.impl;

import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.payload.Media.MediaBaseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MediaRepositoryImpl implements MediaRepository {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MediaRepositoryImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void save(Media media) {
        entityManager.persist(media);
    }

    @Override
    @Transactional
    public void delete(String publicId) {

        Query query = entityManager.createQuery("DELETE FROM Media m WHERE m.publicUrl = :publicId");
        query.setParameter("publicId", publicId);

        int deletedCount = query.executeUpdate();

        if (deletedCount == 0) {
            throw new EmptyResultDataAccessException("Media with publicUrl '" + publicId + "' not found.",1);
        }
    }

    @Override
    public Long getIdByPublicId(String publicId) {
     return   jdbcTemplate.queryForObject("select max(m.id,-1) from Media m where m.public_id = ?", Long.class, publicId);
    }

    @Override
    public MediaBaseDto findByPublicId(String publicId) {

        return entityManager.createQuery("select new in.ac.iitj.instiapp.payload.Media.MediaBaseDto( m.type , m.publicId , m.publicUrl) from Media m where m.publicId = :publicId", MediaBaseDto.class)
                .setParameter("publicId" , publicId)
                .getSingleResult();
    }
}

