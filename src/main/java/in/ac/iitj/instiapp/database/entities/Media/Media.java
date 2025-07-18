package in.ac.iitj.instiapp.database.entities.Media;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false)
    String publicUrl ;

    public Media(String publicId) {
        this.publicUrl = publicUrl;
    }

    public Media(Long mediaId) {
        this.Id = mediaId;
    }
}
