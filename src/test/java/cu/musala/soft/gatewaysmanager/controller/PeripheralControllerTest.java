package cu.musala.soft.gatewaysmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cu.musala.soft.gatewaysmanager.domain.PeripheralStatus;
import cu.musala.soft.gatewaysmanager.persistence.model.Gateway;
import cu.musala.soft.gatewaysmanager.persistence.model.Peripheral;
import cu.musala.soft.gatewaysmanager.persistence.repositories.GatewayRepository;
import cu.musala.soft.gatewaysmanager.persistence.repositories.PeripheralRepository;
import cu.musala.soft.gatewaysmanager.utils.AppUrls;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

import static cu.musala.soft.gatewaysmanager.utils.AppUrls.PERIPHERAL_URL;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PeripheralControllerTest {

    @Autowired
    private PeripheralRepository peripheralRepository;

    @Autowired
    private GatewayRepository gatewayRepository;

    private JacksonTester<List<Peripheral>> listResponse;

    private JacksonTester<Peripheral> peripheralResponse;

    @Autowired
    private MockMvc mvc;

    public PeripheralControllerTest() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    @SneakyThrows
    public void givenGateway_shouldReturnPeripheralList() {
//        given
        List<Peripheral> peripherals = (List<Peripheral>) peripheralRepository.findAll();
        Long gatewayId = peripherals.get(0).getGateway().getId();

//        when
        MockHttpServletResponse response = mvc
                .perform(get(PERIPHERAL_URL)
                        .queryParam("gatewayId", gatewayId.toString())
                )
                .andExpect(status().isOk())
                .andReturn().getResponse();

//        then
        List<Peripheral> peripheralList = listResponse.parse(response.getContentAsString()).getObject();
        Assert.notNull(peripheralList, "Peripheral list should not be null.");
    }


    @Test
    public void addPeripheralToGateway_ShouldReturnNewPeripheralSuccessfully() throws Exception {
        // given
        List<Gateway> gateways = (List<Gateway>) gatewayRepository.findAll();
        Gateway gateway = gateways.get(0);
        String gatewayId = gateway.getId().toString();
        long uidL = UUID.randomUUID().getMostSignificantBits();
        String vendor = "Gateway Test #1",
                uid = String.valueOf(uidL).substring(13),
                status = PeripheralStatus.ONLINE.name();

        // when
        MockHttpServletResponse response = mvc
                .perform(post(AppUrls.PERIPHERAL_URL)
                        .queryParam("gatewayId", gatewayId)
                        .queryParam("uid", uid)
                        .queryParam("status", status)
                        .queryParam("vendor", vendor)
                )
                .andExpect(status().isOk())
                .andReturn().getResponse();

        // then
        Peripheral peripheral = peripheralResponse.parse(response.getContentAsString()).getObject();
        Assert.notNull(peripheral.getId(), "New added gateway should be returned.");
    }

    @Test
    public void addPeripheralToGateway_givenWrongStatus_ShouldReturnNotValid() throws Exception {
        // given
        List<Gateway> gateways = (List<Gateway>) gatewayRepository.findAll();
        Gateway gateway = gateways.get(0);
        String gatewayId = gateway.getId().toString();
        String vendor = "Gateway Test #1",
                uid = "788991",
                status = "WHATEVER";

        // when
        mvc.perform(post(AppUrls.PERIPHERAL_URL)
                .queryParam("gatewayId", gatewayId)
                .queryParam("uid", uid)
                .queryParam("status", status)
                .queryParam("vendor", vendor)
        )
                // then
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void deletePeripheral_ShouldReturnGatewayNotFound() throws Exception {
        // given non existing gateway
        String gatewayId = "-1", peripheralId = "0";

        // when
        MockHttpServletResponse response = mvc.perform(delete(PERIPHERAL_URL)
                .queryParam("gatewayId", gatewayId)
                .queryParam("peripheralId", peripheralId))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();

        // then
        Assert.isTrue(response.getContentAsString().contains("Gateway not found"), "Should have message 'Gateway not found'");
    }

    @Test
    public void deletePeripheralGivenGateway_ShouldDoItSuccessfully() throws Exception {

        // given
        List<Peripheral> peripherals = (List<Peripheral>) peripheralRepository.findAll();
        Peripheral peripheral = peripherals.get(0);
        String gatewayId = peripheral.getGateway().getId().toString();

        // when
        mvc.perform(delete(PERIPHERAL_URL)
                .queryParam("gatewayId", gatewayId)
                .queryParam("peripheralId", peripheral.getId().toString()))
                // then
                .andExpect(status().isOk());

    }

    @Test
    public void updatePeripheral_ShouldReturnNotFound() throws Exception {
        // given non existing peripheral
        Peripheral peripheral = new Peripheral(56723, "Intel", PeripheralStatus.ONLINE);
        peripheral.setId((long) -1);
        String json = new ObjectMapper().writeValueAsString(peripheral);
        List<Gateway> gateways = (List<Gateway>) gatewayRepository.findAll();
        Gateway gateway = gateways.get(0);
        String gatewayId = gateway.getId().toString();

        // when
        MockHttpServletResponse response = mvc.perform(put(PERIPHERAL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .queryParam("gatewayId", gatewayId)
        )
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();

        // then
        Assert.isTrue(response.getContentAsString().contains("Peripheral not found"), "Should have message 'IPv4 not valid'");
    }

    @Test
    public void updatePeripheral_ShouldUpdateSuccessfully() throws Exception {
        // given
        List<Peripheral> peripherals = (List<Peripheral>) peripheralRepository.findAll();
        Peripheral peripheral = peripherals.get(0);
        String gatewayId = peripheral.getGateway().getId().toString();
        peripheral.setVendor("Apple");
        peripheral.setStatus(PeripheralStatus.OFFLINE);
        peripheral.setUid(999);
        String json = new ObjectMapper().writeValueAsString(peripheral);

        // when
        MockHttpServletResponse response = mvc.perform(put(PERIPHERAL_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .queryParam("gatewayId", gatewayId)
        )
                .andExpect(status().isOk())
                .andReturn().getResponse();

        // then
        Peripheral peripheralUpdated = peripheralResponse.parse(response.getContentAsString()).getObject();
        Assert.isTrue(peripheralUpdated.getVendor().equalsIgnoreCase(peripheral.getVendor()), "Peripheral should have updated vendor field.");
        Assert.isTrue(peripheralUpdated.getUid() == peripheral.getUid(), "Peripheral should have updated uid field.");
        Assert.isTrue(peripheralUpdated.getStatus() == peripheral.getStatus(), "Peripheral should have updated status field.");
    }

}