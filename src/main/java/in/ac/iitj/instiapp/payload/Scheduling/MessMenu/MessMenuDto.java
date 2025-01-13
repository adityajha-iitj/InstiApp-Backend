package in.ac.iitj.instiapp.payload.Scheduling.MessMenu;

import in.ac.iitj.instiapp.database.entities.Scheduling.MessMenu.MessMenu;
import lombok.Value;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link MessMenu}
 */
@Value
public class MessMenuDto implements Serializable {
    Integer year;
    Integer month;
    Integer day;
    String menuItemBreakfast;
    String menuItemLunch;
    String menuItemSnacks;
    String menuItemDinner;
}