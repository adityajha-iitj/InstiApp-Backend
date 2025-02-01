package in.ac.iitj.instiapp.database.entities.User;

import in.ac.iitj.instiapp.database.entities.Media.Mediatype;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="permissions")
public class Permissions {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    PermissionsData permissionsData;
}
