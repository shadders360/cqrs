package shadders.cqrs.vehicle.resource;

import io.swagger.annotations.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shadders.cqrs.vehicle.api.RegisterRequest;
import shadders.cqrs.vehicle.api.TaxRequest;
import shadders.cqrs.vehicle.api.VehicleId;
import shadders.cqrs.vehicle.command.RegisterVehicle;
import shadders.cqrs.vehicle.command.SornVehicle;
import shadders.cqrs.vehicle.command.TaxVehicle;
import shadders.cqrs.vehicle.jpa.VehicleQueryRepository;
import shadders.cqrs.vehicle.read.VehicleQueryObject;


import java.util.List;
import java.util.Optional;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * shadders on 23/05/2017.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "/api", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/vehicles", description = "Operations about vehicles")
public class VehicleResource {

    private final VehicleQueryRepository repository;
    private final CommandGateway commandGateway;

    public VehicleResource(VehicleQueryRepository repository, CommandGateway commandGateway) {
        this.repository = repository;
        this.commandGateway = commandGateway;
    }

    @ApiOperation(value = "Get all the Vehicles held within the Read Model",
            notes = "If no vehicles exist then an empty list will be returned",
            response = VehicleQueryObject.class,
            responseContainer = "List")
    @GetMapping("/vehicles")
    public @ResponseBody List<VehicleQueryObject> findAll() {
        return repository.findAll();
    }

    @ApiOperation(value = "Get a vehicle by vehicle Id from the Read Model",
            notes = "The vehicle ID will be a random UUID created on the @post /vehicles linked to the vrm in the message body." +
                    "To find the UUID a rest call to the JPA enquiry service is required first using VRM.",
                    response = VehicleQueryObject.class)
    @ApiResponse(code = 404, message = "No Vehicle found for given vehicle id")
    @GetMapping("/vehicles/{id}")
    public @ResponseBody VehicleQueryObject find(@ApiParam(value = "UUID vehicle id to be found ", required = true)
                                                     @PathVariable String id) {
        this.validateVehicle(id);
        return repository.findOne(id);
    }

    /**
     * add a new vehicle to the system.
     * The vehicle will have a state of UNTAXED
     * @param request vehicle data to be used to create the vehicle
     */
    @ApiOperation(value = "Register a vehicle within the system.",
            notes = "The vehicle ID will be a random UUID created linked to the vrm in the message body." +
                    "To find the vehicle created @get /vehicles will give a list of vehicles created. The vrm" +
                    "can be used to find the UUID via the JPA query service."
                )
    @ApiResponse(code = 201, message = "Vehicle has been created")
    @PostMapping(value="/vehicles")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void registerVehicle(@RequestBody RegisterRequest request){

        this.validateDuplicateVrmFound(request.getVrm());

        VehicleId vehicleId = new VehicleId();
        RegisterVehicle command = new RegisterVehicle(vehicleId, request.getVrm(), request.getVin(),
                request.getMake(), request.getModel(), request.getColour(), request.getListPrice());
        commandGateway.send(command);
    }

    @ApiOperation(value = "SORN a vehicle by vehicle Id",
            notes = "Change the vehicle state to be SORNED (Statutory Off Road Notification)." +
                    "This operation is idempotent REST HTTP method."
            )
    @ApiResponses({
            @ApiResponse(code = 202, message = "Requested has been accepted and will be processed"),
            @ApiResponse(code = 404, message = "No Vehicle found for given vehicle id")
    }
    )
    @PostMapping(value="/vehicles/{id}/sorn")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void sornVehicle(@ApiParam(value = "UUID vehicle id to be found ", required = true)
                                @PathVariable String id){
        this.validateVehicle(id);

        VehicleId vehicleId = new VehicleId(id);
        SornVehicle command = new SornVehicle(vehicleId);
        commandGateway.send(command);
    }

    @ApiOperation(value = "tax a vehicle by vehicle Id",
            notes = "Change the vehicle state to be TAXED (vehicle taxed and legal to use public roads)." +
                    "This operation is idempotent you can pay tax as many times as you wish.")
    @ApiResponses({
            @ApiResponse(code = 202, message = "Requested has been accepted and will be processed"),
            @ApiResponse(code = 404, message = "No Vehicle found for given vehicle id")
    }
    )
    @PostMapping(value="/vehicles/{id}/tax")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void taxVehicle(@ApiParam(value = "UUID vehicle id to be found ", required = true)@PathVariable String id,
                           @RequestBody TaxRequest request){
        this.validateVehicle(id);

        VehicleId vehicleId = new VehicleId(id);
        TaxVehicle command = new TaxVehicle(vehicleId, request.getDurationInMonths(),
                request.getVedPaid());
        commandGateway.send(command);
    }

    /**
     * check that the vehicleId exists in the query model.
     * @param vehicleId the vehicle key
     * @throws VehicleQueryObjectNotFoundException if vehicle not found
     */
    private void validateVehicle(String vehicleId) throws VehicleQueryObjectNotFoundException {
        Optional<VehicleQueryObject> result = Optional.ofNullable(repository.findOne(vehicleId));
        //if null object then throw exception
        result.orElseThrow(() -> new VehicleQueryObjectNotFoundException(vehicleId));
    }

    /**
     * check that the vrm being used has not been created in the query model.
     * @param vrm the vehicle vrm of
     * @throws VehicleQueryObjectDuplicateVrmFoundException if vehicle was found with an existing vrm
     */
    private void validateDuplicateVrmFound(String vrm) throws VehicleQueryObjectDuplicateVrmFoundException {
        Optional<VehicleQueryObject> result = repository.findByVrm(vrm);

        if (result.isPresent()) {  //if object found then we have a duplicate
            throw new VehicleQueryObjectDuplicateVrmFoundException(vrm);
        }
    }

}

