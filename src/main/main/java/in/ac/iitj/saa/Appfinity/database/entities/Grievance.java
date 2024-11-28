package in.ac.iitj.saa.Appfinity.database.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grievance")
public class Grievance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false)
    String Title;

    @Column(nullable = false)
    String Description;

    @ManyToOne
    @JoinColumn(name = "por_id", nullable = true)
    POR toPor;

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = true)
    Media media;
}
