package shadders.cqrs.vehicle.read;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shadders.cqrs.vehicle.event.SornedVehicleEvent;
import shadders.cqrs.vehicle.event.VehicleRegisteredEvent;
import shadders.cqrs.vehicle.jpa.VehicleQueryRepository;
import shadders.cqrs.vehicle.aggregate.Vehicle;
import shadders.cqrs.vehicle.event.TaxedVehicleEvent;

/**
 * shadders on 14/05/2017.
 *
 * @since 1.0.0
 */
@Component
public class VehicleQueryObjectHandler {

    private VehicleQueryRepository repository;

    @Autowired
    public VehicleQueryObjectHandler(VehicleQueryRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(VehicleRegisteredEvent event) {
        VehicleQueryObject vehicle = new VehicleQueryObject(event.getVehicleId().getIdentifier(),
                event.getMake(),event.getModel(),
                event.getVin(), event.getListPrice(), event.getStatus(), event.getVrm());
        repository.save(vehicle);
    }

    @EventHandler
    public void on(TaxedVehicleEvent event) {
        VehicleQueryObject vehicle = repository.findOne(event.getId().getIdentifier());
        vehicle.setStatus(Vehicle.TaxStatus.TAXED);
        vehicle.setVedPaid(event.getVedPaid());
        vehicle.setVedDuration(event.getDurationInMonth());
        repository.save(vehicle);
    }


    @EventHandler
    public void on(SornedVehicleEvent event) {
        VehicleQueryObject vehicle = repository.findOne(event.getId().getIdentifier());
        vehicle.setStatus(Vehicle.TaxStatus.SORNED);
        repository.save(vehicle);
    }
}
