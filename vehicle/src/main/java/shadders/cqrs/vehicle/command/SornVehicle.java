package shadders.cqrs.vehicle.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shadders.cqrs.vehicle.api.VehicleId;


/**
 * shadders on 20/04/2017.
 *
 * @since 1.0.0
 */
public class SornVehicle {

    private static final Logger LOGGER = LoggerFactory.getLogger(SornVehicle.class);

    private VehicleId id;

    public SornVehicle(VehicleId id) {
        this.id = id;
    }

    public VehicleId getId() {
        return id;
    }

    public void setId(VehicleId id) {
        this.id = id;
    }
}
