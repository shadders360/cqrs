package shadders.cqrs.vehicle.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shadders.cqrs.vehicle.api.VehicleId;

/**
 * shadders on 20/04/2017.
 *
 * @since 1.0.0
 */
public class TaxVehicle {

    /** standard class logger.*/
    private static final Logger LOGGER = LoggerFactory.getLogger(TaxVehicle.class);

    private VehicleId id;
    private int durationInMonth;
    private Double vedPaid;

    public TaxVehicle(VehicleId id, int durationInMonth, Double vedPaid) {
        this.id = id;
        this.durationInMonth = durationInMonth;
        this.vedPaid = vedPaid;
    }

    public VehicleId getId() {
        return id;
    }

    public int getDurationInMonth() {
        return durationInMonth;
    }

    public Double getVedPaid() {
        return vedPaid;
    }
}
