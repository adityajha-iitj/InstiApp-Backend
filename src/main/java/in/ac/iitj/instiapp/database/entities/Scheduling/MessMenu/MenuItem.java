package in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu;

import jakarta.persistence.*;
import lombok.*;

import java.awt.*;
@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MenuItem {


    private String breakfast;
    private String lunch;
    private String snacks;
    private String dinner;




}
