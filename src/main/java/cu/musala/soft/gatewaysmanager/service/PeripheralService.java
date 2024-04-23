package cu.musala.soft.gatewaysmanager.service;

import cu.musala.soft.gatewaysmanager.domain.PeripheralStatus;
import cu.musala.soft.gatewaysmanager.core.exception.AlreadyExistException;
import cu.musala.soft.gatewaysmanager.core.exception.NotFoundException;
import cu.musala.soft.gatewaysmanager.persistence.model.Gateway;
import cu.musala.soft.gatewaysmanager.persistence.model.Peripheral;
import cu.musala.soft.gatewaysmanager.persistence.repositories.PeripheralRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@Service
public class PeripheralService {

    private final PeripheralRepository peripheralRepository;

    public PeripheralService(PeripheralRepository peripheralRepository) {
        this.peripheralRepository = peripheralRepository;
    }

    /**
     * Adds new peripheral if validation is successful
     *
     * @return
     */
    public Peripheral add(int number, String vendor, PeripheralStatus status, Gateway gateway) {

        Optional<Peripheral> existent = peripheralRepository.findByUid(number);

        if (existent.isPresent())
            throw new AlreadyExistException("Peripheral UID number in use already!");

        Peripheral peripheral = new Peripheral(number, vendor, status);
        peripheral.setGateway(gateway);
        peripheral.setCreatedDate(new Date());
        return peripheralRepository.save(peripheral);

    }

    /**
     * Deletes a specified peripheral
     *
     * @param deviceId
     */
    public void delete(Long deviceId) {
        if (exists(deviceId))
            peripheralRepository.deleteById(deviceId);
        else
            throw new NotFoundException(Peripheral.class);
        if (exists(deviceId))
            throw new AlreadyExistException("Peripheral not deleted.");
    }

    /**
     * Returns true if a device exists given an id.
     *
     * @param id
     * @return
     */
    public boolean exists(Long id) {
        return peripheralRepository.existsById(id);
    }

    /**
     * Returns updated peripheral.
     *
     * @param peripheral
     * @param gateway
     * @return
     */
    public Peripheral update(Peripheral peripheral, Gateway gateway) {
        if (!exists(peripheral.getId()))
            throw new NotFoundException(Peripheral.class);

        peripheral.setGateway(gateway);
        return peripheralRepository.save(peripheral);
    }
}
