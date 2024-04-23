package cu.musala.soft.gatewaysmanager.controller;

import cu.musala.soft.gatewaysmanager.core.exception.NotFoundException;
import cu.musala.soft.gatewaysmanager.core.exception.NotValidException;
import cu.musala.soft.gatewaysmanager.domain.PeripheralStatus;
import cu.musala.soft.gatewaysmanager.persistence.model.Gateway;
import cu.musala.soft.gatewaysmanager.persistence.model.Peripheral;
import cu.musala.soft.gatewaysmanager.service.GatewayService;
import cu.musala.soft.gatewaysmanager.service.PeripheralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api("Peripheral information are manage here")
@Validated
@RestController
@RequestMapping(value = "/gateways/peripherals")
public class PeripheralController {

    private GatewayService gatewayService;
    private PeripheralService peripheralService;

    public PeripheralController(GatewayService gatewayService, PeripheralService peripheralService) {
        this.gatewayService = gatewayService;
        this.peripheralService = peripheralService;
    }

    @ApiOperation(value = "Returns list of peripheral of an existing gateway")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Peripheral> listGatewayDevices(@RequestParam(value = "gatewayId") Long gatewayId) {
        Gateway gateway = gatewayService.getById(gatewayId);
        return gateway.getPeripherals();
    }

    @ApiOperation(value = "Adds a new peripheral to an existing gateway", response = Peripheral.class)
    @PostMapping
    public Peripheral addDevice(
            @RequestParam("gatewayId") Long gatewayId,
            @RequestParam("uid") int uid,
            @RequestParam("vendor") String vendor,
            @RequestParam("status") PeripheralStatus status
    ) {

        if (gatewayService.exists(gatewayId)) {

            Gateway gateway = gatewayService.getById(gatewayId);

            if (gateway.getPeripherals().size() == 10)
                throw new NotValidException("A gateway can only have up to 10 devices");

            return peripheralService.add(uid, vendor, status, gateway);

        } else
            throw new NotFoundException(Gateway.class);

    }

    @ApiOperation(value = "Updates an existing peripheral", response = Peripheral.class)
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Peripheral updateDevice(
            @Valid @RequestBody Peripheral peripheral
            , @RequestParam("gatewayId") Long gatewayId
    ) {

        if (gatewayService.exists(gatewayId)) {

            Gateway gateway = gatewayService.getById(gatewayId);

            if (gateway.getPeripherals().size() == 10)
                throw new NotValidException("A gateway can only have up to 10 devices");

            return peripheralService.update(peripheral, gateway);

        } else
            throw new NotFoundException(Gateway.class);

    }

    @ApiOperation(value = "Deletes an existing peripheral by peripheral and gateway id")
    @DeleteMapping
    public void deleteDevice(
            @RequestParam("gatewayId") Long gatewayId,
            @RequestParam("peripheralId") Long deviceId
    ) {

        gatewayService.getById(gatewayId);
        peripheralService.delete(deviceId);

    }


}
