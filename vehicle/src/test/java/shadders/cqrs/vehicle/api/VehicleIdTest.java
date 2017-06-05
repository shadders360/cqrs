package shadders.cqrs.vehicle.api;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;


/**
 * shadders on 03/05/2017.
 *
 * @since 1.0.0
 */
public class VehicleIdTest {
    @Test
    public void ObjectMatch() throws Exception {
        VehicleId id1 = new VehicleId();
        VehicleId id2 = new VehicleId(id1.getIdentifier());
        assertThat(id1, is(id2));
    }

    @Test
    public void ObjectHashMatach() throws Exception {
        VehicleId id1 = new VehicleId();
        VehicleId id2 = new VehicleId(id1.getIdentifier());
        assertThat(id1.hashCode(), is(id2.hashCode()));
    }

    @Test
    public void ObjectNoMatach() throws Exception {
        VehicleId id1 = new VehicleId();
        VehicleId id2 = new VehicleId();
        assertThat(id1, CoreMatchers.not(id2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidArgs() throws Exception {
        VehicleId id = new VehicleId(null);

    }
}