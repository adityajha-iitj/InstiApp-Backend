package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.Media.UserAvatar;

import java.util.Map;

public interface UserAvatarRepository {


    Long save(UserAvatar userAvatar);

    String updateByPublicIdReturningOldAssetId(String publicId,UserAvatar userAvatar);

    void deleteById(Long id);

    String updateAvatar(String username,UserAvatar userAvatar);

}
