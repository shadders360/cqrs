package shadders.cqrs.vehicle.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * shadders on 23/05/2017.
 *
 * @since 1.0.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class VehicleQueryObjectNotFoundException extends RuntimeException {

    public VehicleQueryObjectNotFoundException(String vehicleId) {
        super("could not find vehicle [" + vehicleId + "]");
    }
}
