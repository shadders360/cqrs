package shadders.cqrs.vehicle.command;

import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import shadders.cqrs.vehicle.aggregate.Vehicle;
import shadders.cqrs.vehicle.api.VehicleId;
import shadders.cqrs.vehicle.event.TaxedVehicleEvent;
import shadders.cqrs.vehicle.event.VehicleRegisteredEvent;
import shadders.cqrs.vehicle.event.SornedVehicleEvent;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * shadders on 03/05/2017.
 *
 * @since 1.0.0
 */
public class VehicleCommandHandlerTest {

    private static final String VRM = "CP17ABC";
    private static final String VIN = "AB123456";
    private static final String MAKE = "FORD";
    private static final String MODEL = "FOCUS";
    private static final String COLOUR = "RED";
    private static final Double LIST_PRICE = 25000.0;
    private VehicleId vehicleId;

    private FixtureConfiguration<Vehicle> fixture;

    @Before
    public void setUp() throws Exception {

        fixture = new AggregateTestFixture<Vehicle>(Vehicle.class);
        VehicleCommandHandler commandHandler = new VehicleCommandHandler();
        commandHandler.setRepository(fixture.getRepository());
        fixture.registerAnnotatedCommandHandler(commandHandler);
        vehicleId = new VehicleId();
    }

    @Test
    public void handleRegisterVehicle() throws Exception {

        RegisterVehicle command;
        command = new RegisterVehicle(vehicleId, VRM, VIN, MAKE, MODEL, COLOUR, LIST_PRICE);

        fixture.givenNoPriorActivity()
                .when(command)
        .expectEvents(new VehicleRegisteredEvent(vehicleId, VRM,
                VIN, MAKE, MODEL, COLOUR, LIST_PRICE, Vehicle.TaxStatus.UNTAXED));

    }


    @Test
    public void handleTaxVehicleNotRegistered() throws Exception {
        TaxVehicle taxCmd = new TaxVehicle(vehicleId, 12, 130.0);
        fixture.givenNoPriorActivity()
                .when(taxCmd)
                .expectException(Matchers.instanceOf(AggregateNotFoundException.class));
    }

    @Test
    public void handleTaxVehicle() throws Exception {

        RegisterVehicle regCmd = new RegisterVehicle(vehicleId, null, VIN, MAKE, MODEL, COLOUR, LIST_PRICE);
        TaxVehicle taxCmd = new TaxVehicle(vehicleId, 12, 130.0);

        VehicleRegisteredEvent regEvent = new VehicleRegisteredEvent(vehicleId, VRM,
                VIN, MAKE, "ddddd", COLOUR, LIST_PRICE, Vehicle.TaxStatus.UNTAXED);

        fixture.givenCommands(regCmd)
                .when(taxCmd)
                .expectEvents(new TaxedVehicleEvent(vehicleId, 12, 130.0));

    }


    @Test
    public void handleSornVehicle() throws Exception {

        RegisterVehicle regCmd = new RegisterVehicle(vehicleId, null, VIN, MAKE, MODEL, COLOUR, LIST_PRICE);
        TaxVehicle taxCmd = new TaxVehicle(vehicleId, 12, 130.0);
        SornVehicle sornCmd = new SornVehicle(vehicleId);


        VehicleRegisteredEvent regEvent = new VehicleRegisteredEvent(vehicleId, VRM,
                VIN, MAKE, "ddddd", COLOUR, LIST_PRICE, Vehicle.TaxStatus.UNTAXED);

        fixture.givenCommands(regCmd,taxCmd)
                .when(sornCmd)
                .expectEvents(new SornedVehicleEvent(vehicleId));
   }

}