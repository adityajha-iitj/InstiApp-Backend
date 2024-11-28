package in.ac.iitj.saa.Appfinity.database.entities;

import jakarta.persistence.*;
import lombok.*;

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

    @Column( nullable = false)
    String public_id ;

    @Column( nullable = false)
    String asset_id;

    @Column( nullable = false)
    String public_url ;
}
