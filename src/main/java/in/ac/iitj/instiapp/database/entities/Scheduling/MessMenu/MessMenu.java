package in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mess_menu")
public class MessMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column( nullable = false)
    Integer year;

    @Column(nullable = false)
    Integer month;

//    min 0 max 6
    @Column( nullable = true)
    Integer day;

    MenuItem menuItem;

    public MessMenu(Integer year, Integer month, Integer day, MenuItem menuItem) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.menuItem = menuItem;
    }
}
