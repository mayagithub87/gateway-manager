package cu.musala.soft.gatewaysmanager.controller;

import cu.musala.soft.gatewaysmanager.persistence.model.Gateway;
import cu.musala.soft.gatewaysmanager.service.GatewayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cu.musala.soft.gatewaysmanager.utils.UtilityHelper.generateUUID;

@Api("Gateways information are manage here")
@RestController
@RequestMapping(value = "/gateways")
public class GatewayController {

    private GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @ApiOperation(value = "Returns list of gateways in existence")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Gateway> listGateways() {
        return gatewayService.retrieveAll();
    }

    @ApiOperation(value = "Adds a new gateway to database", response = Gateway.class)
    @PostMapping
    public Gateway addGateway(
            @RequestParam("name") String name,
            @RequestParam("ip") String ip
    ) {
        return gatewayService.add(generateUUID(), name, ip);
    }

    @ApiOperation(value = "Updates an existing gateway", response = Gateway.class)
    @PutMapping
    public Gateway updateGateway(
            @RequestParam("id") Long id,
            @RequestParam("serial") String serial,
            @RequestParam("name") String name,
            @RequestParam("ip") String ip) {
        return gatewayService.update(id, serial, name, ip);
    }

    @ApiOperation(value = "Deletes an existing gateway by its id")
    @DeleteMapping(value = "{id}")
    public void deleteGateway(@PathVariable("id") Long id) {
        gatewayService.delete(id);
    }

}
