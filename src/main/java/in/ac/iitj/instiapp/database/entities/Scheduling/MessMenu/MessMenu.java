package in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false)
    Integer year;

    @Column(nullable = false)
    Integer month;

    @Column( nullable = true)
    Integer day;

    MenuItem menuItem;
}
