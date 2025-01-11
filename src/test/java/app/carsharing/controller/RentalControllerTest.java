package app.carsharing.controller;

import static app.carsharing.util.TestUtil.getRentalSearchParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.carsharing.dto.car.CarDetailedResponseDto;
import app.carsharing.dto.car.CarResponseDto;
import app.carsharing.dto.rental.CreateRentalRequestDto;
import app.carsharing.dto.rental.RentalActualReturnDateDto;
import app.carsharing.dto.rental.RentalDetailedDto;
import app.carsharing.dto.rental.RentalResponseDto;
import app.carsharing.dto.user.UserResponseDto;
import app.carsharing.model.Car;
import app.carsharing.model.Rental;
import app.carsharing.model.User;
import app.carsharing.model.enums.Role;
import app.carsharing.util.TestUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = {
        "classpath:database/car/add-cars.sql",
        "classpath:database/user/add-users.sql",
        "classpath:database/rental/add-rentals.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/rental/remove-rentals.sql",
        "classpath:database/car/remove-cars.sql",
        "classpath:database/user/remove-users.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RentalControllerTest {
    protected static MockMvc mockMvc;
    private static final Role USER_ROLE = Role.CUSTOMER;
    private static final User USER = TestUtil.getUser(USER_ROLE);
    private static final Car CAR = TestUtil.getFirstCar();
    private static final CarDetailedResponseDto CAR_RESPONSE_DTO =
            TestUtil.getFirstCarDetailedResponseDto();
    private static final UserResponseDto USER_RESPONSE_DTO = TestUtil.userResponseDto(USER);
    private static final Rental ACTIVE_RENTAL = TestUtil.getActiveRental(USER, CAR);
    private static final RentalDetailedDto ACTIVE_RENTAL_RESPONSE_DTO =
            TestUtil.rentalDetailedResponseDto(ACTIVE_RENTAL, CAR_RESPONSE_DTO, USER_RESPONSE_DTO);
    private static final RentalDetailedDto ACTIVE_RENTAL_RESPONSE_DTO_AFTER_RENTAL =
            TestUtil.rentalDetailedResponseDto(ACTIVE_RENTAL, CAR_RESPONSE_DTO.setInventory(14),
                    USER_RESPONSE_DTO);
    private static final RentalResponseDto ACTIVE_RENTAL_SHORT_RESPONSE_DTO =
            TestUtil.rentalResponseDto(ACTIVE_RENTAL, CAR, USER);
    private static final CreateRentalRequestDto RENTAL_REQUEST_DTO =
            new CreateRentalRequestDto()
                    .setCarId(1L)
                    .setRentalDate(ACTIVE_RENTAL.getRentalDate())
                    .setReturnDate(ACTIVE_RENTAL.getReturnDate());
    private static final RentalActualReturnDateDto RETURN_DATE_DTO =
            new RentalActualReturnDateDto()
                    .setActualReturnDate(LocalDate.of(2024, 8, 1));
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithUserDetails(TestUtil.DEFAULT_USER_EMAIL)
    @Test
    @DisplayName("Create a new rental")
    public void createRental_ValidRequest_WillReturnRentalDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(RENTAL_REQUEST_DTO);
        MvcResult result = mockMvc.perform(
                        post("/rentals")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        RentalDetailedDto actualResponseDto = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        RentalDetailedDto.class);

        Assertions.assertNotNull(actualResponseDto);
        Assertions.assertNotNull(actualResponseDto.getId());
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(
                ACTIVE_RENTAL_RESPONSE_DTO_AFTER_RENTAL, actualResponseDto, "id"));
    }

    @WithUserDetails(TestUtil.DEFAULT_USER_EMAIL)
    @Test
    @DisplayName("Get all rentals by active status")
    public void getAllRentalsByActiveStatus_ShouldReturnListOfRentals() throws Exception {
        List<RentalResponseDto> expectedList = Arrays.asList(ACTIVE_RENTAL_SHORT_RESPONSE_DTO);
        String jsonRequest = objectMapper.writeValueAsString(getRentalSearchParameters());
        MvcResult result = mockMvc.perform(
                        get("/rentals")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode contentNode = jsonNode.get("content");
        CarResponseDto[] actual =
                objectMapper.treeToValue(contentNode, CarResponseDto[].class);
        Assertions.assertEquals(expectedList.size(), actual.length);
    }

    @WithUserDetails(TestUtil.DEFAULT_USER_EMAIL)
    @Test
    @DisplayName("Get rental by id")
    public void getRentalById_ValidId_ShouldReturnRentalDto() throws Exception {
        Long id = 1L;
        MvcResult result = mockMvc.perform(
                        get("/rentals/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        RentalDetailedDto actualResponseDto = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        RentalDetailedDto.class);
        Assertions.assertNotNull(actualResponseDto);
        Assertions.assertEquals(ACTIVE_RENTAL_RESPONSE_DTO.getId(), actualResponseDto.getId());
    }

    @WithUserDetails(TestUtil.DEFAULT_USER_EMAIL)
    @Test
    @DisplayName("Return rental by id")
    public void returnRental_ValidId_ShouldReturnRentalDto() throws Exception {
        Long id = 1L;
        String jsonRequest = objectMapper.writeValueAsString(RETURN_DATE_DTO);
        MvcResult result = mockMvc.perform(
                        post("/rentals/{id}/return", id)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        RentalResponseDto actualResponseDto = objectMapper
                .readValue(result.getResponse().getContentAsString(), RentalResponseDto.class);
        Assertions.assertNotNull(actualResponseDto);
        Assertions.assertEquals(ACTIVE_RENTAL_SHORT_RESPONSE_DTO.getId(),
                actualResponseDto.getId());
    }
}
