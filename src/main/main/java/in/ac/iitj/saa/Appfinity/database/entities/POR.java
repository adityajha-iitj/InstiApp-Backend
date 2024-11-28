package in.ac.iitj.saa.Appfinity.database.entities;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "por")
public class POR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    Organisation organisation;


    @Column( nullable = true)
    String vertical;

    @ManyToOne
    @JoinColumn(nullable = false)
    OrganisationRole role;

    @Column( nullable = false)
    Integer publicId;

    @Column( nullable = false)
    Integer userId;
}
