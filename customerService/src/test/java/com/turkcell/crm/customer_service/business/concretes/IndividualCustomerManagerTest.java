package com.turkcell.crm.customer_service.business.concretes;

import com.turkcell.crm.common.shared.dtos.customers.GetIndividualCustomerInvoiceInfoDto;
import com.turkcell.crm.common.shared.dtos.customers.GetInvoiceInfoByAddressDto;
import com.turkcell.crm.common.shared.exceptions.types.NotFoundException;
import com.turkcell.crm.common.shared.kafka.events.customers.IndividualCustomerCreatedEvent;
import com.turkcell.crm.common.shared.kafka.events.customers.IndividualCustomerDeletedEvent;
import com.turkcell.crm.common.shared.kafka.events.customers.IndividualCustomerUpdatedEvent;
import com.turkcell.crm.customer_service.adapters.mernis.CheckNationalityService;
import com.turkcell.crm.customer_service.business.abstracts.AddressService;
import com.turkcell.crm.customer_service.business.abstracts.CustomerService;
import com.turkcell.crm.customer_service.business.dtos.requests.customers.AddressDto;
import com.turkcell.crm.customer_service.business.dtos.requests.customers.CreateCustomerRequest;
import com.turkcell.crm.customer_service.business.dtos.requests.customers.UpdateCustomerRequest;
import com.turkcell.crm.customer_service.business.dtos.requests.individual_customers.CreateIndividualCustomerRequest;
import com.turkcell.crm.customer_service.business.dtos.requests.individual_customers.UpdateIndividualCustomerRequest;
import com.turkcell.crm.customer_service.business.dtos.responses.customers.CustomerDto;
import com.turkcell.crm.customer_service.business.dtos.responses.individual_customers.*;
import com.turkcell.crm.customer_service.business.mappers.IndividualCustomerMapper;
import com.turkcell.crm.customer_service.business.rules.IndividualCustomerBusinessRules;
import com.turkcell.crm.customer_service.core.business.abstracts.MessageService;
import com.turkcell.crm.customer_service.data_access.abstracts.IndividualCustomerRepository;
import com.turkcell.crm.customer_service.entities.concretes.*;
import com.turkcell.crm.customer_service.entities.enums.Gender;
import com.turkcell.crm.customer_service.kafka.producers.CustomerProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndividualCustomerManagerTest {

    @Mock
    private IndividualCustomerRepository individualCustomerRepository;

    @Mock
    private IndividualCustomerBusinessRules individualCustomerBusinessRules;

    @Mock
    private IndividualCustomerMapper individualCustomerMapper;

    @Mock
    private CustomerService customerService;
    @Mock
    private AddressService addressService;

    @Mock
    private CustomerProducer customerProducer;


    @InjectMocks
    private IndividualCustomerManager individualCustomerManager;

    private CreateIndividualCustomerRequest createIndividualCustomerRequest;
    private UpdateIndividualCustomerRequest updateIndividualCustomerRequest;
    private IndividualCustomer individualCustomer;
    private Customer customer;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {

        individualCustomer = new IndividualCustomer();
        customer = new Customer();
        customerDto = new CustomerDto("test@test.com", "1234567891023");
    }

    @Test
    void add_ShouldAddIndividualCustomer() {
        // Arrange
        AddressDto addressDto = new AddressDto(3, 1, "test street", "546", "test at test at test");
        List<AddressDto> addressDtoList = new ArrayList<>();
        addressDtoList.add(addressDto);
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest("test@test.com", "1234567891023", addressDtoList);
        createIndividualCustomerRequest = new CreateIndividualCustomerRequest(
                "Engin",
                null,
                "Demiroğ",
                "12345678910",
                "12345678910",
                LocalDate.of(2001, 1, 20), // @NotNull
                "Aaaaaa",
                "dsgafsdsfg",
                Gender.MALE, //
                createCustomerRequest //
        );

        when(individualCustomerMapper.toIndividualCustomer(any(CreateIndividualCustomerRequest.class)))
                .thenReturn(individualCustomer);
        when(customerService.add(any())).thenReturn(customer);
        when(individualCustomerRepository.save(any(IndividualCustomer.class))).thenReturn(individualCustomer);
        when(individualCustomerMapper.toCustomerCreatedEvent(any(IndividualCustomer.class)))
                .thenReturn(new IndividualCustomerCreatedEvent());
        when(individualCustomerMapper.toCreatedIndividualCustomerResponse(any(IndividualCustomer.class)))
                .thenReturn(new CreatedIndividualCustomerResponse(1, LocalDateTime.now(), "Engin",
                        null,
                        "Demiroğ",
                        null,
                        "12345678910",
                        LocalDate.of(2001, 1, 20),
                        "Aaaaaa",
                        "dsgafsdsfg",
                        Gender.MALE,
                        customerDto));

        // Act
        CreatedIndividualCustomerResponse response = individualCustomerManager.add(createIndividualCustomerRequest);

        // Assert
        assertNotNull(response);
        verify(individualCustomerBusinessRules).nationalityIdShouldBeUnique(any());
        verify(individualCustomerBusinessRules).nationalityIdShouldBeValid(any());
        verify(individualCustomerRepository).save(any(IndividualCustomer.class));
        verify(customerProducer).send(any(IndividualCustomerCreatedEvent.class));
    }

    @Test
    void getAll_ShouldReturnAllIndividualCustomers() {

        IndividualCustomer individualCustomer1 = new IndividualCustomer();
        individualCustomer1.setId(1);
        individualCustomer1.setFirstName("Engin");

        IndividualCustomer individualCustomer2 = new IndividualCustomer();
        individualCustomer1.setId(2);
        individualCustomer1.setFirstName("Zengin");

        GetAllIndividualCustomersResponse response1 = new GetAllIndividualCustomersResponse(
                1, LocalDateTime.now(), LocalDateTime.now(), "Engin",
                " ",
                "Demiroğ",
                "1234567890123",
                "12345678910",
                LocalDate.now(),
                "Mother",
                "Father",
                Gender.MALE,
                customerDto);

        GetAllIndividualCustomersResponse response2 = new GetAllIndividualCustomersResponse(
                2, LocalDateTime.now(), LocalDateTime.now(), "Zengin",
                " ",
                "Demiroğ",
                "1234567890123",
                "12345678910",
                LocalDate.now(),
                "Mother",
                "Father",
                Gender.MALE,
                customerDto);

        List<IndividualCustomer> customers = Arrays.asList(individualCustomer1, individualCustomer2);
        List<GetAllIndividualCustomersResponse> responses = Arrays.asList(response1, response2);

        when(individualCustomerRepository.findAll()).thenReturn(customers);
        when(individualCustomerMapper.toGetAllIndividualCustomersResponseList(customers)).thenReturn(responses);

        List<GetAllIndividualCustomersResponse> result = individualCustomerManager.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));

        verify(individualCustomerRepository, times(1)).findAll();
        verify(individualCustomerMapper, times(1)).toGetAllIndividualCustomersResponseList(customers);
    }

    @Test
    void getById_ShouldReturnIndividualCustomerForSpecificId() {

        int customerId = 1;
        GetByIdIndividualCustomerResponse getByIdIndividualCustomerResponse = new GetByIdIndividualCustomerResponse(
                customerId, LocalDateTime.now(), LocalDateTime.now(), "Engin",
                " ",
                "Demiroğ",
                "1234567890123",
                "12345678910",
                LocalDate.now(),
                "Mother",
                "Father",
                Gender.MALE,
                customerDto);
        Optional<IndividualCustomer> customerOptional = Optional.of(individualCustomer);

        when(individualCustomerRepository.findById(customerId)).thenReturn(customerOptional);
        doNothing().when(individualCustomerBusinessRules).individualCustomerShouldBeExist(customerOptional);
        when(individualCustomerMapper.toGetByIdIndividualCustomerResponse(individualCustomer)).thenReturn(getByIdIndividualCustomerResponse);

        GetByIdIndividualCustomerResponse response = individualCustomerManager.getById(customerId);

        assertNotNull(response);
        assertEquals(getByIdIndividualCustomerResponse, response);

        verify(individualCustomerRepository, times(1)).findById(customerId);
        verify(individualCustomerBusinessRules, times(1)).individualCustomerShouldBeExist(customerOptional);
        verify(individualCustomerMapper, times(1)).toGetByIdIndividualCustomerResponse(individualCustomer);
    }

    @Test
    void update_ShouldUpdateIndividualCustomerForSpecificId() {
        int customerId = 1;
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest("test@test.com", "1234567891023");
        updateIndividualCustomerRequest = new UpdateIndividualCustomerRequest(
                "Engin",
                null,
                "Demiroğ",
                "12345678910",
                LocalDate.of(2001, 1, 20),
                "sdasdsa",
                "Aaaaaa",
                Gender.MALE,
                updateCustomerRequest
        );

        // Arrange
        when(individualCustomerRepository.findById(customerId)).thenReturn(Optional.of(individualCustomer));
        when(individualCustomerMapper.toCustomerUpdatedEvent(any(IndividualCustomer.class)))
                .thenReturn(new IndividualCustomerUpdatedEvent());
        when(individualCustomerMapper.toUpdatedIndividualCustomerResponse(any(IndividualCustomer.class)))
                .thenReturn(new UpdatedIndividualCustomerResponse(customerId,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "Engin",
                        null,
                        "Demiroğ",
                        null,
                        "12345678910",
                        LocalDate.of(2001, 1, 20),
                        "sdasdsa",
                        "Aaaaaa",
                        Gender.MALE,
                        customerDto
                ));

        // Act
        UpdatedIndividualCustomerResponse response = individualCustomerManager.update(customerId, updateIndividualCustomerRequest);

        // Assert
        assertNotNull(response);
        verify(individualCustomerRepository).save(any(IndividualCustomer.class));
        verify(customerProducer).send(any(IndividualCustomerUpdatedEvent.class));
    }

    @Test
    public void delete_ShouldDeleteIndividualCustomerForSpecificId() {
        int customerId = 1;
        individualCustomer.setId(customerId);
        individualCustomer.setDeletedDate(null);

        DeletedIndividualCustomerResponse deletedIndividualCustomerResponse = new DeletedIndividualCustomerResponse(
                customerId, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), "Engin", "", "Demiroğ",
                "1234567890123", "12345678901", LocalDate.now(), "Mother", "Father", Gender.MALE, customerDto);
        IndividualCustomerDeletedEvent individualCustomerDeletedEvent = new IndividualCustomerDeletedEvent();

        when(individualCustomerRepository.findById(1)).thenReturn(Optional.of(individualCustomer));
        doNothing().when(individualCustomerBusinessRules).individualCustomerShouldBeExist(any());
        when(individualCustomerRepository.save(any(IndividualCustomer.class))).thenReturn(individualCustomer);
        when(individualCustomerMapper.toCustomerDeletedEvent(any(IndividualCustomer.class))).thenReturn(individualCustomerDeletedEvent);
        when(individualCustomerMapper.toDeletedIndividualCustomerResponse(any(IndividualCustomer.class))).thenReturn(deletedIndividualCustomerResponse);

        DeletedIndividualCustomerResponse response = individualCustomerManager.delete(customerId);

        assertNotNull(response);
        assertEquals(deletedIndividualCustomerResponse, response);
        assertNotNull(individualCustomer.getDeletedDate());
        verify(individualCustomerRepository, times(1)).findById(customerId);
        verify(individualCustomerBusinessRules, times(1)).individualCustomerShouldBeExist(any());
        verify(individualCustomerRepository, times(1)).save(any(IndividualCustomer.class));
        verify(individualCustomerMapper, times(1)).toCustomerDeletedEvent(any(IndividualCustomer.class));
        verify(customerProducer, times(1)).send(any(IndividualCustomerDeletedEvent.class));
        verify(individualCustomerMapper, times(1)).toDeletedIndividualCustomerResponse(any(IndividualCustomer.class));
    }

    @Test
    void individualCustomerShouldBeExist_ShouldNotThrowException_WhenCustomerExists() {

        Optional<IndividualCustomer> optionalIndividualCustomer = Optional.of(individualCustomer);

        individualCustomerBusinessRules.individualCustomerShouldBeExist(optionalIndividualCustomer);

    }

    @Test
    void individualCustomerShouldBeExist_ShouldThrowNotFoundException_WhenCustomerDoesNotExist() {

        IndividualCustomerRepository individualCustomerRepository = mock(IndividualCustomerRepository.class);
        CheckNationalityService checkNationalityService = mock(CheckNationalityService.class);
        MessageService messageService = mock(MessageService.class);
        IndividualCustomerBusinessRules individualCustomerBusinessRules1 = new IndividualCustomerBusinessRules(individualCustomerRepository, checkNationalityService, messageService);

        assertThrows(NotFoundException.class, () -> {
            individualCustomerBusinessRules1.individualCustomerShouldBeExist(Optional.empty());
        });
    }

    @Test
    void getInvoiceInfoByAddress_ShouldReturnInvoiceInfo() {
        // Arrange
        int addressId = 1;
        Address address = new Address();
        address.setId(addressId);
        address.setStreet("test street");
        address.setHouseFlatNumber("546");
        address.setDescription("test description");
        address.setCity(new City(1));
        address.setDistrict(new District(1));

        IndividualCustomer individualCustomer = new IndividualCustomer();
        individualCustomer.setFirstName("Engin");
        individualCustomer.setLastName("Demiroğ");
        individualCustomer.setNationalityId("12345678910");

        Customer customer = new Customer();
        customer.setIndividualCustomer(individualCustomer);
        address.setCustomer(customer);

        GetInvoiceInfoByAddressDto addressDto = new GetInvoiceInfoByAddressDto(
                address.getStreet(), address.getHouseFlatNumber(), address.getDescription(), address.getCity().getName(), address.getDistrict().getName());

        GetIndividualCustomerInvoiceInfoDto expectedInvoiceInfo = new GetIndividualCustomerInvoiceInfoDto();
        expectedInvoiceInfo.setFirstName("Engin");
        expectedInvoiceInfo.setLastName("Demiroğ");
        expectedInvoiceInfo.setNationalityId("12345678910");
        expectedInvoiceInfo.setAddress(addressDto);

        when(addressService.getByIdEntity(addressId)).thenReturn(address);
        when(individualCustomerMapper.toGetIndividualCustomerInvoiceInfoDto(individualCustomer)).thenReturn(expectedInvoiceInfo);

        // Act
        GetIndividualCustomerInvoiceInfoDto actualInvoiceInfo = individualCustomerManager.getInvoiceInfoByAddress(addressId);

        // Assert
        assertNotNull(actualInvoiceInfo);
        assertEquals(expectedInvoiceInfo.getFirstName(), actualInvoiceInfo.getFirstName());
        assertEquals(expectedInvoiceInfo.getLastName(), actualInvoiceInfo.getLastName());
        assertEquals(expectedInvoiceInfo.getNationalityId(), actualInvoiceInfo.getNationalityId());
        assertEquals(expectedInvoiceInfo.getAddress().street(), actualInvoiceInfo.getAddress().street());
        assertEquals(expectedInvoiceInfo.getAddress().houseFlatNumber(), actualInvoiceInfo.getAddress().houseFlatNumber());
        assertEquals(expectedInvoiceInfo.getAddress().description(), actualInvoiceInfo.getAddress().description());
        assertEquals(expectedInvoiceInfo.getAddress().cityName(), actualInvoiceInfo.getAddress().cityName());
        assertEquals(expectedInvoiceInfo.getAddress().districtName(), actualInvoiceInfo.getAddress().districtName());

        verify(addressService, times(1)).getByIdEntity(addressId);
        verify(individualCustomerMapper, times(1)).toGetIndividualCustomerInvoiceInfoDto(individualCustomer);
    }


}
