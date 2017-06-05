package shadders.cqrs.vehicle.event;

import shadders.cqrs.vehicle.api.VehicleId;

/**
 * shadders on 04/05/2017.
 *
 * @since 1.0.0
 */
public class SornedVehicleEvent {

    private VehicleId id;

    public SornedVehicleEvent(VehicleId id) {
        this.id = id;
    }

    public VehicleId getId() {
        return id;
    }

    @Override
    public String toString() {
        return "SornedVehicleEvent{" +
                "id=" + id +
                '}';
    }
}
