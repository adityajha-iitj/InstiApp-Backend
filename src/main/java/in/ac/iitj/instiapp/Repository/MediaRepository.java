package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.payload.Media.MediaBaseDto;

public interface MediaRepository {

    void save(Media media);
    MediaBaseDto findByPublicId(String publicId);
    void delete(String publicUrl);

    Long getIdByPublicId(String publicId);



}
