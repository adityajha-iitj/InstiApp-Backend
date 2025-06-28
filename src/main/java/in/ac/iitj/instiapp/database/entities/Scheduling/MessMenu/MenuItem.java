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


    @Column(name = "breakfast", columnDefinition = "text", nullable = false)
    private String breakfast;

    @Column(name = "lunch", columnDefinition = "text", nullable = false)
    private String lunch;

    @Column(name = "snacks", columnDefinition = "text", nullable = false)
    private String snacks;

    @Column(name = "dinner", columnDefinition = "text", nullable = false)
    private String dinner;




}
