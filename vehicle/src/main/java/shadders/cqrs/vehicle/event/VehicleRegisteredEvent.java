package shadders.cqrs.vehicle.event;

import shadders.cqrs.vehicle.aggregate.Vehicle;
import shadders.cqrs.vehicle.api.VehicleId;

/**
 * shadders on 03/05/2017.
 *
 * @since 1.0.0
 */
public class VehicleRegisteredEvent {

    private VehicleId vehicleId;
    private String vin;
    private String make;
    private String model;
    private String colour;
    private Double listPrice;
    private Vehicle.TaxStatus status;
    private String vrm;

    public VehicleRegisteredEvent(VehicleId vehicleId, String vrm, String vin,
                                  String make, String model, String colour,
                                  Double listPrice, Vehicle.TaxStatus taxStatus) {
        this.vehicleId = vehicleId;
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.colour = colour;
        this.listPrice = listPrice;
        this.status = taxStatus;
        this.vrm = vrm;
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

    public Vehicle.TaxStatus getStatus() {
        return status;
    }

    public void setStatus(Vehicle.TaxStatus status) {
        this.status = status;
    }

    public String getVrm() {
        return vrm;
    }

    @Override
    public String toString() {
        return "VehicleRegisteredEvent{" +
                "vehicleId=" + vehicleId +
                ", vin='" + vin + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", colour='" + colour + '\'' +
                ", listPrice=" + listPrice +
                ", status=" + status +
                ", vrm='" + vrm + '\'' +
                '}';
    }
}
