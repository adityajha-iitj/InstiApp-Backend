package in.ac.iitj.instiapp.payload.Scheduling.Buses;

import lombok.*;

import java.io.Serializable;
import java.sql.Time;

@Value
@Builder
@With
public class RouteStopDto implements Serializable {
    String locationName;
    Integer stopOrder;
    Time arrivalTime;
    Time departureTime;
}