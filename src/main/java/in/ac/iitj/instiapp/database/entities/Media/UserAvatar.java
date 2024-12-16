package in.ac.iitj.instiapp.database.entities.Media;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_avatar")
@AllArgsConstructor
@NoArgsConstructor
public class UserAvatar {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
//   In future provide user feature to upload videos
    Mediatype type;

    @Column( nullable = false)
    String publicId ;

    @Column( nullable = false)
    String assetId;

    @Column( nullable = false)
    String publicUrl ;


}
