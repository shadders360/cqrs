package shadders.cqrs.vehicle.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shadders.cqrs.vehicle.api.VehicleId;

/**
 * shadders on 20/04/2017.
 *
 * @since 1.0.0
 */
public class RegisterVehicle  {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterVehicle.class);

    private VehicleId vehicleId;
    private String vin;
    private String make;
    private String model;
    private String colour;
    private Double listPrice;
    private String vrm;

    public RegisterVehicle() { //required by framework
    }

    public RegisterVehicle(VehicleId vehicleId, String vrm, String vin, String make, String model,
                           String colour, Double listPrice) {
        this.vehicleId = vehicleId;
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.colour = colour;
        this.listPrice = listPrice;
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

    public String getVrm() {
        return vrm;
    }

    @Override
    public String toString() {
        return "RegisterVehicle{" +
                "vehicleId=" + vehicleId +
                ", vin='" + vin + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", colour='" + colour + '\'' +
                ", listPrice=" + listPrice +
                ", vrm='" + vrm + '\'' +
                '}';
    }
}
