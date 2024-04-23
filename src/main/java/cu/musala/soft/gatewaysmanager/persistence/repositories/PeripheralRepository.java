package cu.musala.soft.gatewaysmanager.persistence.repositories;

import cu.musala.soft.gatewaysmanager.persistence.model.Peripheral;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeripheralRepository extends CrudRepository<Peripheral, Long> {

    /**
     * Returns all {@link Peripheral}s.
     *
     * @return
     */
    Optional<Peripheral> findByUid(int uid);

}
