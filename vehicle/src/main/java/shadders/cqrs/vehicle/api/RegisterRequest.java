package shadders.cqrs.vehicle.api;

import io.swagger.annotations.ApiModelProperty;

/**
 * shadders on 17/05/2017.
 *
 * @since 1.0.0
 */
public class RegisterRequest {

    public RegisterRequest() {
       //needed for rest framework to build inbound object }
    }

    private RegisterRequest(Builder build) {
        vrm = build.vrm;
        vin = build.vin;
        model = build.model;
        make = build.make;
        colour = build.colour;
        listPrice = build.listPrice;
    }

    @ApiModelProperty(value="VRM (vehicle registration mark", example = "CA99XYZ", required = true)
    private String vrm;

    @ApiModelProperty(value="vin (vehicle identification number", example = "XYZ1234567890", required = true)
    private String vin;

    @ApiModelProperty(value="list price of the vehicle at the dealer if known", example = "10500.0", required = false)
    private double listPrice;

    @ApiModelProperty(value="make of the vehicle", example = "FORD", required = false)
    private String make;

    @ApiModelProperty(value="model of the vehicle", example = "KA", required = false)
    private String model;

    @ApiModelProperty(value="colour of the vehicle", example = "RED", required = false)
    private String colour;

    public String getVin() {
        return vin;
    }

    public String getVrm() {
        return vrm;
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

    public static class Builder {

        //required
        private String vrm;
        private String vin;

        //optional
        private String make;
        private String model;
        private String colour;
        private Double listPrice;

        public Builder(String vrm, String vin) {
            this.vrm = vrm;
            this.vin = vin;
        }

        public Builder make(String value) {
            make = value;
            return this;
        }

        public Builder model(String value) {
            model = value;
            return this;
        }

        public Builder colour(String value) {
            colour = value;
            return this;
        }

        public Builder listPrice(Double value) {
            listPrice = value;
            return this;
        }

        public RegisterRequest build() {
            return new RegisterRequest(this);
        }
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "vin='" + vin + '\'' +
                ", vrm='" + vrm + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", colour='" + colour + '\'' +
                ", listPrice=" + listPrice +
                '}';
    }
}
