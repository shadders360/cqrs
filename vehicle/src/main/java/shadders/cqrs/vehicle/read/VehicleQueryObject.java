package shadders.cqrs.vehicle.read;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import shadders.cqrs.vehicle.aggregate.Vehicle;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * shadders on 09/05/2017.
 *
 * @since 1.0.0
 */
@Entity
@ApiModel(description = "The vehicle read model")
public class VehicleQueryObject {

    @Id
    @ApiModelProperty(value="unique vehicle Id within the system.(system generated UUID)", required = true)
    private String vehicleId;

    @ApiModelProperty(value="VRM (vehicle registration mark", example = "CP99XYZ", required = true)
    private String vrm;

    @ApiModelProperty(value="vin (vehicle identification number", example = "AB1234567890", required = true)
    private String vin;

    @ApiModelProperty(value="status of the vehicle with in the system", allowableValues ="UNTAXED,TAXED,SORNED",
            example = "TAXED", required = true)
    private Vehicle.TaxStatus status;

    @ApiModelProperty(value="how much VED was paid when the vehicle was taxed", example = "140.0", required = false)
    private double vedPaid;

    @ApiModelProperty(value="ved duration in months when the vehicle was taxed", example = "12", required = false)
    private int vedDuration;

    @ApiModelProperty(value="list price of the vehicle at the dealer if known", example = "15000.0", required = false)
    private double listPrice;

    @ApiModelProperty(value="make of the vehicle", example = "FORD", required = false)
    private String make;

    @ApiModelProperty(value="model of the vehicle", example = "FOCUS", required = false)
    private String model;


    public VehicleQueryObject() {
    }

    public VehicleQueryObject(String vehicleId, String make, String model, String vin,
                              double listPrice, Vehicle.TaxStatus status, String vrm) {
        this.vehicleId = vehicleId;
        this.make = make;
        this.model = model;
        this.vin = vin;
        this.status = status;
        this.listPrice = listPrice;
        this.vrm = vrm;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVrm() {
        return vrm;
    }

    public void setVrm(String vrm) {
        this.vrm = vrm;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Vehicle.TaxStatus getStatus() {
        return status;
    }

    public void setStatus(Vehicle.TaxStatus status) {
        this.status = status;
    }

    public double getVedPaid() {
        return vedPaid;
    }

    public void setVedPaid(double vedPaid) {
        this.vedPaid = vedPaid;
    }

    public double getListPrice() {
        return listPrice;
    }

    public void setListPrice(double listPrice) {
        this.listPrice = listPrice;
    }

    public void setVedDuration(int vedDuration) {
        this.vedDuration = vedDuration;
    }

    public int getVedDuration() {
        return vedDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleQueryObject that = (VehicleQueryObject) o;
        return Double.compare(that.vedPaid, vedPaid) == 0 &&
                Double.compare(that.listPrice, listPrice) == 0 &&
                vedDuration == that.vedDuration &&
                Objects.equals(vehicleId, that.vehicleId) &&
                Objects.equals(vrm, that.vrm) &&
                Objects.equals(make, that.make) &&
                Objects.equals(model, that.model) &&
                Objects.equals(vin, that.vin) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, vrm, make, model, vin, status, vedPaid, listPrice, vedDuration);
    }
}
