package in.ac.iitj.instiapp.database.entities.Scheduling.Buses;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "route_stop")
public class RouteStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private BusRoute route;
    
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private BusLocation location;
    
    @Column(nullable = false)
    private Integer stopOrder; // 1, 2, 3, etc.
    
    @Column(nullable = true)
    private Time arrivalTime;
    
    @Column(nullable = true)
    private Time departureTime;
    
    public RouteStop(BusRoute route, BusLocation location, Integer stopOrder, Time arrivalTime, Time departureTime) {
        this.route = route;
        this.location = location;
        this.stopOrder = stopOrder;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }
}