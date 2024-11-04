package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;
    private Long EXISTS_Customer_ID;

    @Test
    public void findAllShouldReturnAllCategories() {

        List<Customer> list = new ArrayList<>();
        list.add(mock(Customer.class));

        when(customerRepository.findAll()).thenReturn(list);

        List<Customer> categories = customerService.findAll();

        assertNotNull(categories);
        assertEquals(1, categories.size());
    }

    public static final String Customer_NAME = "CAT1";

    @Test
    public void saveNotExistsCustomerIdShouldInsert() {

        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(Customer_NAME);

        ArgumentCaptor<Customer> customer = ArgumentCaptor.forClass(Customer.class);

        customerService.save(null, customerDto);

        verify(customerRepository).save(customer.capture());

        assertEquals(Customer_NAME, customer.getValue().getName());
    }

    public static final Long NOT_EXISTS_Customer_ID = 0L;

    @Test
    public void getExistsCustomerIdShouldReturnCustomer() {

        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(EXISTS_Customer_ID);
        when(customerRepository.findById(EXISTS_Customer_ID)).thenReturn(Optional.of(customer));

        Customer customerResponse = customerService.get(EXISTS_Customer_ID);

        assertNotNull(customerResponse);
        assertEquals(EXISTS_Customer_ID, customer.getId());
    }

    @Test
    public void getNotExistsCustomerIdShouldReturnNull() {

        when(customerRepository.findById(NOT_EXISTS_Customer_ID)).thenReturn(Optional.empty());

        Customer customer = customerService.get(NOT_EXISTS_Customer_ID);

        assertNull(customer);
    }
}