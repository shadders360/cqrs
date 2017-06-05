package shadders.cqrs.vehicle.api;

import io.swagger.annotations.ApiModelProperty;

/**
 * shadders on 17/05/2017.
 *
 * @since 1.0.0
 */
public class TaxRequest {

    @ApiModelProperty(value="VED duration in months", example = "12", required = true)
    private int durationInMonths;
    @ApiModelProperty(value="VED paid", example = "140.0", required = true)
    private Double vedPaid;

    public TaxRequest() { //for rest service framework
    }

    public TaxRequest(int durationInMonths, Double vedPaid) {
        this.durationInMonths = durationInMonths;
        this.vedPaid = vedPaid;
    }

    public int getDurationInMonths() {
        return durationInMonths;
    }

    public Double getVedPaid() {
        return vedPaid;
    }

    @Override
    public String toString() {
        return "TaxRequest{" +
                "durationInMonths=" + durationInMonths +
                ", vedPaid=" + vedPaid +
                '}';
    }
}
