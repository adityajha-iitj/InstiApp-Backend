package in.ac.iitj.instiapp.Repository;

import in.ac.iitj.instiapp.database.entities.User.Group;
import in.ac.iitj.instiapp.payload.User.GroupDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface GroupRepository {


     /**
      * @assumptions not all values are null
      * @param group
      */
     Long save(Group group);

     /**
      * If not filter by something then make it null
      * @param userId
      * @param branchId
      * @param programId
      * @param organisationRoleIds
      * @param userTypeId
      * @param userTypeAttributes
      * @param pageable
      * @return The ids of groups which match the parameter
      */
     List<Long> getGroupIdsByGroup(Long userId, Long branchId, Long programId, List<Long> organisationRoleIds, Long userTypeId, Map<String, Map<String, String>> userTypeAttributes, Pageable pageable);

     Long existByPublicId(String publicId);

     /**
      * @param publicId
      * @param group if not update then list should be null or hstore should be null
      */
     void update(String publicId,Group group);

     /**
      * Assumes the announcement is deleted before
      * @param publicId
      */
     void deleteByPublicId(String publicId);
}
