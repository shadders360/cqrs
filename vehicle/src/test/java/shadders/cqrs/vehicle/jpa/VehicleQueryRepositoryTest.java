package shadders.cqrs.vehicle.jpa;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import shadders.cqrs.vehicle.resource.VehicleQueryObjectNotFoundException;
import shadders.cqrs.vehicle.aggregate.Vehicle;
import shadders.cqrs.vehicle.read.VehicleQueryObject;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

/**
 * shadders on 23/05/2017.
 *
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VehicleQueryRepositoryTest {

    private static final String VALID_VRM_1 = "XP17ABC";
    private static final String VALID_VRM_2 = "XP18ABC";
    private static final String VEHICLE_ID = "10000";

    @Autowired
    private VehicleQueryRepository vehicleQueryRepository;

    VehicleQueryObject q1;
    VehicleQueryObject q2;

    @Before
    public void setup() throws Exception {

        q1 = new VehicleQueryObject(VEHICLE_ID, "ford", "focus",
                "vin-001", 25999.99,
                Vehicle.TaxStatus.UNTAXED, VALID_VRM_1);

        q2 = new VehicleQueryObject("20000", "ford", "ka",
                "vin-002", 15999.99,
                Vehicle.TaxStatus.UNTAXED, VALID_VRM_2);

        this.vehicleQueryRepository.deleteAllInBatch();
        this.vehicleQueryRepository.save(q1);
        this.vehicleQueryRepository.save(q2);

    }

    @Test
    public void findAll() throws Exception {
        List<VehicleQueryObject> results = this.vehicleQueryRepository.findAll();
        assertThat(results.size(), is(2));
    }

    @Test
    public void findById() throws Exception {
        Optional<VehicleQueryObject> result =
                Optional.ofNullable(this.vehicleQueryRepository.findOne(VEHICLE_ID));
        assertThat("Vehicle Not Found", result.isPresent(), is(true));
        assertThat("Found vehicle does not match",result.get(), is(q1));
    }

    @Test
    public void findByVrm() throws Exception {
        Optional<VehicleQueryObject> result =  this.vehicleQueryRepository.findByVrm(VALID_VRM_1);
        assertThat("Vehicle Not Found", result.isPresent(), is(true));
        assertThat("Found vehicle does not match",result.get(), is(q1));
    }

    @Test(expected = VehicleQueryObjectNotFoundException.class )
    public void noRecordFound() throws Exception {
        this.vehicleQueryRepository.findByVrm("XP")
                .orElseThrow(() -> new VehicleQueryObjectNotFoundException("XP"));
    }

}