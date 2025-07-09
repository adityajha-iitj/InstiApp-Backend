package in.ac.iitj.instiapp.database.entities.Scheduling.Buses;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "bus_route")
public class BusRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String routeId; // e.g., "ROUTE_COLLEGE_TO_CITY"
    
    @Column(nullable = false)
    private String routeName; // e.g., "College to Main City"
    
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("stopOrder ASC")
    private List<RouteStop> stops;
    
    public BusRoute(String routeId, String routeName) {
        this.routeId = routeId;
        this.routeName = routeName;
    }
}