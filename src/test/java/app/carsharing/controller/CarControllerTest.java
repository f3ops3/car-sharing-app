package app.carsharing.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.carsharing.dto.car.CarDetailedResponseDto;
import app.carsharing.dto.car.CarResponseDto;
import app.carsharing.dto.car.CarUpdateRequestDto;
import app.carsharing.dto.car.CreateCarRequestDto;
import app.carsharing.util.TestUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = {
        "classpath:database/car/add-cars.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/car/remove-cars.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarControllerTest {
    protected static MockMvc mockMvc;
    private static final Long VALID_CAR_ID = 2L;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DisplayName("Save a new car with valid data")
    public void saveCar_ValidRequest_Success() throws Exception {
        CreateCarRequestDto requestDto = TestUtil.createFirstCarRequestDto();
        CarDetailedResponseDto expected = TestUtil.getFirstCarDetailedResponseDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/cars")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CarDetailedResponseDto actualCarDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CarDetailedResponseDto.class);

        Assertions.assertNotNull(actualCarDto);
        Assertions.assertNotNull(actualCarDto.getId());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actualCarDto, "id"));
    }

    @Test
    @DisplayName("Get all cars")
    @WithMockUser(username = "user")
    void getAllCars_ShouldReturnAllCars() throws Exception {
        List<CarResponseDto> cars = new ArrayList<>();
        cars.add(TestUtil.getFirstCarResponseDto());
        cars.add(TestUtil.getSecondCarResponseDto());

        MvcResult result = mockMvc.perform(
                        get("/cars")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode contentNode = jsonNode.get("content");
        CarResponseDto[] actualCars =
                objectMapper.treeToValue(contentNode, CarResponseDto[].class);
        Assertions.assertEquals(cars.size(), actualCars.length);
    }

    @Test
    @DisplayName("Get car by id")
    @WithMockUser(username = "user")
    void getCarById_ValidId_Success() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/cars/{id}", VALID_CAR_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CarDetailedResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarDetailedResponseDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                TestUtil.getSecondCarDetailedResponseDto(), actual));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DisplayName("Update car by id")
    void update_ValidRequest_ShouldReturnUpdatedCarDto() throws Exception {
        CreateCarRequestDto updateCarRequestDto = TestUtil.updateCarRequestDto();
        CarDetailedResponseDto expected = TestUtil.updatedCarResponseDto();

        String jsonRequest = objectMapper.writeValueAsString(updateCarRequestDto);
        MvcResult result = mockMvc.perform(
                        put("/cars/{id}", VALID_CAR_ID)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CarDetailedResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarDetailedResponseDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DisplayName("Update car inventory by id")
    void updateCarInventory_ValidRequest_Success() throws Exception {
        CarUpdateRequestDto requestDto = new CarUpdateRequestDto()
                .setInventory(5);

        CarDetailedResponseDto expected = TestUtil.getSecondCarDetailedResponseDto()
                .setInventory(requestDto.getInventory());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                        patch("/cars/{id}", VALID_CAR_ID)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CarDetailedResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CarDetailedResponseDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getInventory(), actual.getInventory());
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DisplayName("Delete car by id")
    void delete_ValidId_Success() throws Exception {
        mockMvc.perform(delete("/cars/{id}", VALID_CAR_ID))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
