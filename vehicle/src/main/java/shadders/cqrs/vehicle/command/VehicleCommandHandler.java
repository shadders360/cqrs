package shadders.cqrs.vehicle.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Aggregate;

import org.axonframework.commandhandling.model.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import shadders.cqrs.vehicle.aggregate.Vehicle;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * shadders on 03/05/2017.
 *
 * @since 1.0.0
 */
@Component
public class VehicleCommandHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleCommandHandler.class);
    private Repository<Vehicle> repository;

    @CommandHandler
    public void handleRegisterVehicle(RegisterVehicle command) throws Exception {

        repository.newInstance(new Callable<Vehicle>() {
            @Override
            public Vehicle call() throws Exception {
                return new Vehicle(command.getVehicleId(), command.getVrm(), command.getVin(), command.getMake(),
                        command.getModel(), command.getColour(), command.getListPrice());
            }
        });
    }

    @CommandHandler
    public void handleTaxVehicle(TaxVehicle command) throws Exception {

        Aggregate<Vehicle> vehicle = repository.load(command.getId().getIdentifier());
        vehicle.execute(aggregate -> aggregate.taxVehicle(command.getDurationInMonth()
                , command.getVedPaid()));
        LOGGER.info(vehicle.toString());
    }

    @CommandHandler
    public void sornVehicle(SornVehicle command) throws Exception {

        Aggregate<Vehicle> vehicle = repository.load(command.getId().getIdentifier());

        vehicle.execute(new Consumer<Vehicle>() {
            @Override
            public void accept(Vehicle aggregate) {
                aggregate.sornVehicle();
            }
        });

        LOGGER.info(vehicle.toString());
    }

    @Autowired
    @Qualifier("vehicleRepository")
    public void setRepository(Repository<Vehicle> vehicleRepository) {
       this.repository = vehicleRepository;
    }

}
