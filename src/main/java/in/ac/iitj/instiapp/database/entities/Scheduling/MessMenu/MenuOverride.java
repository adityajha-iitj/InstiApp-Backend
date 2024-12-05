package in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "menu_override")
public class MenuOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    private MessMenu messMenu;

    private Date date;

    private MenuItem menuItem;
}
