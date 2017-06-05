package shadders.cqrs.vehicle.event;

import shadders.cqrs.vehicle.api.VehicleId;

/**
 * shadders on 04/05/2017.
 *
 * @since 1.0.0
 */
public class TaxedVehicleEvent {

    private VehicleId id;
    private int durationInMonth;
    private Double vedPaid;


    public TaxedVehicleEvent(VehicleId id, int durationInMonth, Double vedPaid) {
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

    @Override
    public String toString() {
        return "TaxedVehicleEvent{" +
                "id=" + id +
                ", durationInMonth=" + durationInMonth +
                ", vedPaid=" + vedPaid +
                '}';
    }
}
