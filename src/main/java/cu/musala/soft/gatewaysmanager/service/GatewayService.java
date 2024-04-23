package cu.musala.soft.gatewaysmanager.service;

import cu.musala.soft.gatewaysmanager.core.exception.AlreadyExistException;
import cu.musala.soft.gatewaysmanager.core.exception.NotFoundException;
import cu.musala.soft.gatewaysmanager.core.exception.NotValidException;
import cu.musala.soft.gatewaysmanager.persistence.model.Gateway;
import cu.musala.soft.gatewaysmanager.persistence.repositories.GatewayRepository;
import cu.musala.soft.gatewaysmanager.utils.UtilityHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class GatewayService {

    private final GatewayRepository gatewayRepository;

    public GatewayService(GatewayRepository gatewayRepository) {
        this.gatewayRepository = gatewayRepository;
    }

    /**
     * Adds new gateway if validation is successful
     *
     * @param serial
     * @param name
     * @param ip
     * @return
     */
    public Gateway add(String serial, String name, String ip) {

        Optional<Gateway> existentGateway = gatewayRepository.findBySerialNumber(serial);

        if (existentGateway.isPresent())
            throw new AlreadyExistException("Gateway Serial in use already!");

        if (!UtilityHelper.validateIPv4Address(ip))
            throw new NotValidException("IPv4 not valid.");

        Gateway gateway = new Gateway(serial, name, ip);
        return gatewayRepository.save(gateway);

    }

    /**
     * Returns all existing gateways.
     *
     * @return
     */
    public List<Gateway> retrieveAll() {
        return (List<Gateway>) gatewayRepository.findAll();
    }

    /**
     * Delete gateway given its id.
     *
     * @param id
     */
    public void delete(Long id) {
        if (exists(id))
            gatewayRepository.deleteById(id);
        else
            throw new NotFoundException(Gateway.class);
        if (exists(id))
            throw new AlreadyExistException("Gateway not deleted.");
    }

    /**
     * Returns true if a gateway exists given an id.
     *
     * @param id
     * @return
     */
    public boolean exists(Long id) {
        return gatewayRepository.existsById(id);
    }

    /**
     * Updates the conent of a previously existing gateway.
     *
     * @param id
     * @param serial
     * @param name
     * @param ip
     * @return
     */
    public Gateway update(Long id, String serial, String name, String ip) {

        Optional<Gateway> gatewayOpt = gatewayRepository.findById(id);
        if (!gatewayOpt.isPresent())
            throw new NotFoundException(Gateway.class);

        Optional<Gateway> gatewaySerialOpt = gatewayRepository.findBySerialNumber(serial);
        if (gatewaySerialOpt.isPresent() && gatewayOpt.get().getId() != gatewaySerialOpt.get().getId())
            throw new AlreadyExistException("Gateway Serial in use already!");

        Gateway gateway = gatewayOpt.get();
        gateway.setSerialNumber(serial);
        gateway.setName(name);
        gateway.setIp4vAddress(ip);

        return gatewayRepository.save(gateway);
    }

    /**
     * Returns a gateway given its corresponding id
     *
     * @param gatewayId
     * @return
     */
    public Gateway getById(Long gatewayId) {
        Optional<Gateway> gateway = gatewayRepository.findById(gatewayId);
        if (gateway.isPresent())
            return gateway.get();
        else
            throw new NotFoundException(Gateway.class);
    }

}
