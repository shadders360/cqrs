package shadders.cqrs.vehicle.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * shadders on 24/05/2017.
 *
 * @since 1.0.0
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VehicleQueryObjectDuplicateVrmFoundException extends RuntimeException {

    public VehicleQueryObjectDuplicateVrmFoundException(String vrm) {
        super("vehicle vrm already been used [" + vrm + "]");
    }
}
