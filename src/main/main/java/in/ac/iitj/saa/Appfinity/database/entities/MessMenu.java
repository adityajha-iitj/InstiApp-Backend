package in.ac.iitj.saa.Appfinity.database.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messmenu")
public class MessMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false)
    Integer year;

    @Column(nullable = false)
    Integer month;

    @Column( nullable = false)
    Integer day;

    @Column( nullable = false)
    String breakfast;

    @Column( nullable = false)
    String lunch;

    @Column( nullable = false)
    String snacks;

    @Column( nullable = false)
    String dinner;
}
