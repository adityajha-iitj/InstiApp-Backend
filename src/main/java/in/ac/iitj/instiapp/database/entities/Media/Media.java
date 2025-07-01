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

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    Mediatype type;

    @Column(name = "public_id", unique = true, nullable = false, updatable = false, length = 36)
    private String publicId;

    @Column( nullable = false)
    String assetId;

    @Column( nullable = false)
    String publicUrl ;

    public Media(String publicId, String publicUrl, String assetId, Mediatype mediatype) {
        this.publicId = publicId;
        this.publicUrl = publicUrl;
        this.assetId = assetId;
        this.type = mediatype;
    }

    public Media(Long mediaId) {
        this.Id = mediaId;
    }

    @PrePersist
    private void ensurePublicId() {
        if (publicId == null) {
            publicId = UUID.randomUUID().toString();
        }
    }
}
