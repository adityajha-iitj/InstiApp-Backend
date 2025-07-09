package in.ac.iitj.instiapp.payload.Scheduling.Buses;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Value
@Builder
@With
public class BusRouteDto implements Serializable {
    String routeId;
    String routeName;
    List<RouteStopDto> stops;
}