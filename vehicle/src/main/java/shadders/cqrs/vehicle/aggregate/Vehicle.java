package shadders.cqrs.vehicle.aggregate;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateRoot;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shadders.cqrs.vehicle.api.VehicleId;
import shadders.cqrs.vehicle.event.SornedVehicleEvent;
import shadders.cqrs.vehicle.event.TaxedVehicleEvent;
import shadders.cqrs.vehicle.event.VehicleRegisteredEvent;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/**
 * shadders on 21/04/2017.
 *
 * @since 1.0.0
 */
@AggregateRoot
public class Vehicle {

    private static final Logger LOGGER = LoggerFactory.getLogger(Vehicle.class);

    public enum TaxStatus {
        UNTAXED,
        TAXED,
        SORNED;
    }

    @AggregateIdentifier
    private VehicleId vehicleId;

    private String vin;
    private String make;
    private String model;
    private String colour;
    private Double listPrice;
    private String vrm;

    private int durationInMonth;
    private Double vedPaid;
    private TaxStatus status = TaxStatus.UNTAXED;


    public Vehicle() { //used by framework
        LOGGER.debug("NO ARG Vehicle constructor called ");
    }

    public Vehicle(VehicleId vehicleId, String vrm, String vin, String make,
                   String model, String colour, Double listPrice) {
        LOGGER.debug("Vehicle constructor called " + vehicleId);
        this.vehicleId = vehicleId;
        this.vrm = vrm;
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.colour = colour;
        this.listPrice = listPrice;
        this.status = TaxStatus.UNTAXED;
        apply(new VehicleRegisteredEvent(vehicleId, vrm, vin, make, model, colour, listPrice, TaxStatus.UNTAXED));
    }

    public void taxVehicle(int durationInMonth , Double vedPaid) {
        apply(new TaxedVehicleEvent(this.vehicleId, durationInMonth, vedPaid));
    }

    public void sornVehicle() {
        apply(new SornedVehicleEvent(this.vehicleId));
    }

    @EventSourcingHandler
    public void on(VehicleRegisteredEvent event) {
        LOGGER.info("event received :" + event.toString());
        this.vehicleId = event.getVehicleId();
        this.colour = event.getColour();
        this.listPrice = event.getListPrice();
        this.make = event.getMake();
        this.model = event.getModel();
        this.vin = event.getVin();
        this.status = event.getStatus();
        this.vrm = event.getVrm();
    }


    @EventSourcingHandler
    public void on(TaxedVehicleEvent event) {
        LOGGER.info("event received :" + event.toString());
        this.vedPaid = event.getVedPaid();
        this.durationInMonth = event.getDurationInMonth();
        this.status = TaxStatus.TAXED;
    }

    @EventSourcingHandler
    public void on(SornedVehicleEvent event) {
        LOGGER.info("event received :" + event.toString());
        this.status = TaxStatus.SORNED;
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }

    public String getVin() {
        return vin;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getColour() {
        return colour;
    }

    public Double getListPrice() {
        return listPrice;
    }

    public int getDurationInMonth() {
        return durationInMonth;
    }

    public Double getVedPaid() {
        return vedPaid;
    }

    public TaxStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", vin='" + vin + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", colour='" + colour + '\'' +
                ", listPrice=" + listPrice +
                ", durationInMonth=" + durationInMonth +
                ", vedPaid=" + vedPaid +
                ", status=" + status +
                '}';
    }
}
