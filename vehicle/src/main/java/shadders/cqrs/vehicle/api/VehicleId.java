package shadders.cqrs.vehicle.api;

import io.swagger.annotations.ApiModelProperty;
import org.axonframework.common.IdentifierFactory;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;

/**
 * shadders on 03/05/2017.
 *
 * @since 1.0.0
 */
public class VehicleId implements Serializable {

    private static final long serialVersionUID = 5512248601730343273L;

    @ApiModelProperty(value="system generated identifier within the system",
            example = "bdaeca3c-3418-4927-bc61-8156159c5de2", required = true)
    private final String identifier;

    /**
     * generated when the class in constructed.
     */
    private final int hashCode;

    public VehicleId() {
        this.identifier = IdentifierFactory.getInstance().generateIdentifier();
        this.hashCode = identifier.hashCode();
    }

    public VehicleId(String identifier) {
        Assert.notNull(identifier, "Identifier may not be null");
        this.identifier = identifier;
        this.hashCode = identifier.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        VehicleId vehicleId = (VehicleId) obj;
        return Objects.equals(identifier, vehicleId.identifier);
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return this.getIdentifier();
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
