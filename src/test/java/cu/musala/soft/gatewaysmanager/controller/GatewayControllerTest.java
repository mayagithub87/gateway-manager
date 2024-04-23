package cu.musala.soft.gatewaysmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cu.musala.soft.gatewaysmanager.persistence.model.Gateway;
import cu.musala.soft.gatewaysmanager.persistence.repositories.GatewayRepository;
import cu.musala.soft.gatewaysmanager.utils.AppUrls;
import cu.musala.soft.gatewaysmanager.utils.UtilityHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GatewayControllerTest {

    private JacksonTester<List<Gateway>> listResponse;

    private JacksonTester<Gateway> gatewayResponse;

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private MockMvc mvc;

//    @BeforeAll
//    void setUp() throws Exception {
//        JacksonTester.initFields(this, new ObjectMapper());
//    }

    public GatewayControllerTest() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @SneakyThrows
    @Test
    public void shouldReturn_GatewayList() {
//        when
        MockHttpServletResponse response = mvc
                .perform(get(AppUrls.GATEWAYS_URL))
                .andExpect(status().isOk())
                .andReturn().getResponse();
//        then
        List<Gateway> gatewayList = listResponse.parse(response.getContentAsString()).getObject();
        Assert.notNull(gatewayList, "Gateway list should not be null.");
    }

    @Test
    public void addGateway_ShouldReturnNewGatewaySuccessfully() throws Exception {
        // given
        String name = String.format("Gateway Test %s", UtilityHelper.generateUUID()),
                ip = "100.50.2.16";

        // when
        MockHttpServletResponse response = mvc
                .perform(post(AppUrls.GATEWAYS_URL)
                        .queryParam("name", name)
                        .queryParam("ip", ip)
                )
                .andExpect(status().isOk())
                .andReturn().getResponse();

        // then
        Gateway gateway = gatewayResponse.parse(response.getContentAsString()).getObject();
        Assert.notNull(gateway.getId(), "New added gateway should be returned.");
    }

    @Test
    public void addGateway_ShouldReturnInvalidIp() throws Exception {
        // given
        String name = "Gateway Test #2",
                ip = "1er00.50.2.1a6";

        // when
        MockHttpServletResponse response = mvc.perform(post(AppUrls.GATEWAYS_URL)
                .queryParam("name", name)
                .queryParam("ip", ip)
        )
                .andExpect(status().is5xxServerError())
                .andReturn().getResponse();

        // then
        Assert.isTrue(response.getContentAsString().contains("IPv4 not valid"), "Should have message 'IPv4 not valid'");
    }

    @Test
    public void updateGateway_ShouldReturnNotFound() throws Exception {
        // given
        String name = "Gateway Test #3",
                ip = "100.50.2.16", serial = "324324", id = "-46";

        // when
        MockHttpServletResponse response = mvc.perform(put(AppUrls.GATEWAYS_URL)
                .queryParam("name", name)
                .queryParam("ip", ip)
                .queryParam("serial", serial)
                .queryParam("id", id)
        )
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();

        // then
        Assert.isTrue(response.getContentAsString().contains("Gateway not found"), "Should have message 'IPv4 not valid'");
    }

    @Test
    public void updateGateway_ShouldReturnSerialExistAlready() throws Exception {
        // given
        List<Gateway> gateways = (List<Gateway>) gatewayRepository.findAll();
        Gateway gatewayRef = gateways.get(0);
        Gateway gatewayToUpdate = gateways.get(1);
        String name = "Gateway Test #Update2",
                ip = "210.10.1.96",
                serial = gatewayRef.getSerialNumber();

        // when
        MockHttpServletResponse response = mvc.perform(put(AppUrls.GATEWAYS_URL)
                .queryParam("name", name)
                .queryParam("ip", ip)
                .queryParam("serial", serial)
                .queryParam("id", gatewayToUpdate.getId().toString())
        )
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();

        // then
        Assert.isTrue(response.getContentAsString().contains("Gateway Serial in use already"), "Should have message 'Gateway Serial in use already'");
    }

    @Test
    public void updateGateway_ShouldDoItSuccessfully() throws Exception {
        // given
        List<Gateway> gateways = (List<Gateway>) gatewayRepository.findAll();
        Gateway gateway = gateways.get(0);
        String name = "Gateway Test #Update",
                ip = "10.10.1.96";

        // when
        MockHttpServletResponse response = mvc.perform(put(AppUrls.GATEWAYS_URL)
                .queryParam("name", name)
                .queryParam("ip", ip)
                .queryParam("serial", gateway.getIp4vAddress())
                .queryParam("id", gateway.getId().toString())
        )
                .andExpect(status().isOk())
                .andReturn().getResponse();

        // then
        gateway = gatewayResponse.parse(response.getContentAsString()).getObject();
        Assert.isTrue(gateway.getName().equalsIgnoreCase(name), "Gateway should have updated name field.");
        Assert.isTrue(gateway.getIp4vAddress().equalsIgnoreCase(ip), "Gateway should have updated ip field.");
    }

    @Test
    public void deleteGateway_ShouldDoItSuccessfully() throws Exception {
        // given
        List<Gateway> gateways = (List<Gateway>) gatewayRepository.findAll();
        Gateway gateway = gateways.get(0);
        String gatewayId = gateway.getId().toString();

        // when
        mvc.perform(delete(AppUrls.GATEWAYS_URL + "/" + gatewayId))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteGateway_ShouldReturnGatewayNotFound() throws Exception {
        // given non existing gateway
        String gatewayId = "-1";

        // when
        MockHttpServletResponse response = mvc.perform(delete(AppUrls.GATEWAYS_URL
                + "/" + gatewayId))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();

        // then
        Assert.isTrue(response.getContentAsString().contains("Gateway not found"), "Should have message 'Gateway not found'");
    }


}