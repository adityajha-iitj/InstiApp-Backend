package in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "menu_override")
@EqualsAndHashCode
public class MenuOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;


    @Temporal(TemporalType.DATE)
    private Date date;

    private MenuItem menuItem;
    
    public MenuOverride(Date date, MenuItem menuItem){
        this.date = date;
        this.menuItem = menuItem;
    }

}
