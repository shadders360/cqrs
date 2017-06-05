package shadders.cqrs.vehicle.resource;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import shadders.cqrs.vehicle.aggregate.Vehicle;
import shadders.cqrs.vehicle.api.RegisterRequest;
import shadders.cqrs.vehicle.api.TaxRequest;
import shadders.cqrs.vehicle.jpa.VehicleQueryRepository;
import shadders.cqrs.vehicle.read.VehicleQueryObject;
import shadders.cqrs.vehicle.VehicleApplication;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * shadders on 24/05/2017.
 *
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VehicleApplication.class)
@WebAppConfiguration

public class VehicleResourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleResourceTest.class);

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private VehicleQueryRepository repository;

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    private static final String VALID_VRM_1 = "XP17ABC";
    private static final String VALID_VRM_2 = "XP18ABC";
    private static final String VEHICLE_ID = "10000";

    VehicleQueryObject q1;
    VehicleQueryObject q2;

    @Before
    public void setup() throws Exception {

        LOGGER.info("test setup !!");
        q1 = new VehicleQueryObject(VEHICLE_ID, "ford", "focus",
                "vin-001", 25999.99,
                Vehicle.TaxStatus.UNTAXED, VALID_VRM_1);

        q2 = new VehicleQueryObject("20000", "ford", "ka",
                "vin-002", 15999.99,
                Vehicle.TaxStatus.UNTAXED, VALID_VRM_2);

        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.repository.deleteAllInBatch();
        this.repository.save(q1);
        this.repository.save(q2);
    }

    @Test
    public void vehicleNotFound() throws Exception {
        mockMvc.perform(get("/api/vehicles/abcdefg"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAll() throws Exception {

      mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].vehicleId", is(VEHICLE_ID)))
                .andExpect(jsonPath("$[0].vrm", is(VALID_VRM_1)))
                .andExpect(jsonPath("$[0].vin", is("vin-001")))
                .andExpect(jsonPath("$[0].make", is("ford")))
                .andExpect(jsonPath("$[0].model", is("focus")))
                .andExpect(jsonPath("$[0].status", is("UNTAXED")))
                .andExpect(jsonPath("$[0].vedPaid", is(0.0)))
                .andExpect(jsonPath("$[0].listPrice", is(25999.99)))
                .andExpect(jsonPath("$[0].vedDuration", is(0)));
    }

    @Test
    public void find() throws Exception {
        mockMvc.perform(get("/api/vehicles/"+ VEHICLE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.vehicleId", is(VEHICLE_ID)))
                .andExpect(jsonPath("$.vrm", is(VALID_VRM_1)))
                .andExpect(jsonPath("$.vin", is("vin-001")))
                .andExpect(jsonPath("$.make", is("ford")))
                .andExpect(jsonPath("$.model", is("focus")))
                .andExpect(jsonPath("$.status", is("UNTAXED")))
                .andExpect(jsonPath("$.vedPaid", is(0.0)))
                .andExpect(jsonPath("$.listPrice", is(25999.99)))
                .andExpect(jsonPath("$.vedDuration", is(0)));
    }

    @Test
    public void registerVehicle() throws Exception {
        RegisterRequest postContent
                = new RegisterRequest.Builder("cp02zxy","VIN123456789")
                .make("ford")
                .model("focus")
                .colour("red")
                .listPrice(21500.99).build();

        this.mockMvc.perform(post("/api/vehicles")
                .contentType(contentType)
                .content(this.json(postContent)))
                .andExpect(status().isCreated());

        Optional<VehicleQueryObject> result = this.repository.findByVrm("cp02zxy");
        assertThat("Vehicle not found", result.isPresent(), CoreMatchers.is(true));
        assertThat("vrm mismatch", result.get().getVrm() , is ("cp02zxy"));
        assertThat("vin mismatch", result.get().getVin() , is ("VIN123456789"));
        assertThat("expected untaxed", result.get().getStatus() , is (Vehicle.TaxStatus.UNTAXED));
    }

    @Test
    public void duplicateRegisterVehicle() throws Exception {
        RegisterRequest postContent
                = new RegisterRequest.Builder("cp03zxy","VIN123456789")
                .make("ford")
                .model("ka")
                .colour("blue")
                .listPrice(1500.99).build();

        this.mockMvc.perform(post("/api/vehicles")
                .contentType(contentType)
                .content(this.json(postContent)))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post("/api/vehicles")
                .contentType(contentType)
                .content(this.json(postContent)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void sornVehicle() throws Exception {

        RegisterRequest postContent
                = new RegisterRequest.Builder("cp05zxy","VIN123456789")
                .make("ford")
                .model("maxi")
                .colour("green")
                .listPrice(3400.99).build();

        //register
        this.mockMvc.perform(post("/api/vehicles")
                .contentType(contentType)
                .content(this.json(postContent)))
                .andExpect(status().isCreated());

        Optional<VehicleQueryObject> result = this.repository.findByVrm("cp05zxy");
        assertThat("Vehicle not found", result.isPresent(), CoreMatchers.is(true));
        assertThat("vrm mismatch", result.get().getVrm() , is ("cp05zxy"));
        assertThat("vin mismatch", result.get().getVin() , is ("VIN123456789"));
        assertThat("expected untaxed", result.get().getStatus() , is (Vehicle.TaxStatus.UNTAXED));

        //sorn
        this.mockMvc.perform(post("/api/vehicles/" + result.get().getVehicleId() + "/sorn")
                .contentType(contentType))
                .andExpect(status().isAccepted());
        result = this.repository.findByVrm("cp05zxy");
        assertThat("expected untaxed", result.get().getStatus() , is (Vehicle.TaxStatus.SORNED));
    }

    @Test
    public void taxVehicle() throws Exception {
        RegisterRequest postContent
                = new RegisterRequest.Builder("cp25zxy","VIN123456789")
                .make("ford")
                .model("pickup")
                .colour("green")
                .listPrice(43400.99).build();

        //register
        this.mockMvc.perform(post("/api/vehicles")
                .contentType(contentType)
                .content(this.json(postContent)))
                .andExpect(status().isCreated());

        Optional<VehicleQueryObject> result = this.repository.findByVrm("cp25zxy");
        assertThat("Vehicle not found", result.isPresent(), CoreMatchers.is(true));
        assertThat("vrm mismatch", result.get().getVrm() , is ("cp25zxy"));
        assertThat("vin mismatch", result.get().getVin() , is ("VIN123456789"));
        assertThat("expected untaxed", result.get().getStatus() , is (Vehicle.TaxStatus.UNTAXED));

        //tax
        TaxRequest taxRequest = new TaxRequest(12,140.0);
        this.mockMvc.perform(post("/api/vehicles/" + result.get().getVehicleId() + "/tax")
                .contentType(contentType)
                .content(this.json(taxRequest)))
                .andExpect(status().isAccepted());
        result = this.repository.findByVrm("cp25zxy");
        assertThat("expected taxed", result.get().getStatus() , is (Vehicle.TaxStatus.TAXED));
    }

    @Test
    public void taxUnknownVehicle() throws Exception {

        //tax
        TaxRequest taxRequest = new TaxRequest(12,140.0);
        this.mockMvc.perform(post("/api/vehicles/xxxxxxxx/tax")
                .contentType(contentType)
                .content(this.json(taxRequest)))
                .andExpect(status().isNotFound());

    }

    @Test
    public void sornUnknownVehicle() throws Exception {

        //tax
        TaxRequest taxRequest = new TaxRequest(12,140.0);
        this.mockMvc.perform(post("/api/vehicles/xxxxxxxx/sorn")
                .contentType(contentType)
                .content(this.json(taxRequest)))
                .andExpect(status().isNotFound());

    }

    protected String json(Object obj) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                obj, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}