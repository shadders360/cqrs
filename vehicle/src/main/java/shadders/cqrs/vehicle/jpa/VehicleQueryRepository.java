package shadders.cqrs.vehicle.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import shadders.cqrs.vehicle.read.VehicleQueryObject;

import java.util.Optional;

/**
 * shadders on 09/05/2017.
 *
 * @since 1.0.0
 */
public interface VehicleQueryRepository extends JpaRepository<VehicleQueryObject, String> {

    Optional<VehicleQueryObject> findByVrm(@Param("vrm") String vrm);

}
