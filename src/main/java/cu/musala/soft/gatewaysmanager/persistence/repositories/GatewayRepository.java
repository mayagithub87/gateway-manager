package cu.musala.soft.gatewaysmanager.persistence.repositories;

import cu.musala.soft.gatewaysmanager.persistence.model.Gateway;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatewayRepository extends CrudRepository<Gateway, Long> {

    void deleteById(Long id);

    Optional<Gateway> findBySerialNumber(String serial);
}
