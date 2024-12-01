package in.ac.iitj.instiapp.database.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    Mediatype type;

    @Column( nullable = false)
    String publicId ;

    @Column( nullable = false)
    String assetId;

    @Column( nullable = false)
    String publicUrl ;
}
