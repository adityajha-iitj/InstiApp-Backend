package in.ac.iitj.instiapp.database.entities.User;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLHStoreType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "groups")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Group {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    String name;

    String publicId;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "bigint[]", name = "user_ids")
    List<Long> userIds;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "bigint[]", name = "branch_ids")
    List<Long> branchIds;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "bigint[]", name = "program_ids")
    List<Long> programIds;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "bigint[]", name = "organisation_role_ids")
    List<Long> organisationRoleIds;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "bigint[]", name = "user_type_ids")
    List<Long> userTypeIds;

    @Type(PostgreSQLHStoreType.class)
    @Column(columnDefinition = "hstore", name = "user_type_attributes")
    Map<String, Map<String, String>> userTypeAttributes;

    Boolean isCommon;
}
