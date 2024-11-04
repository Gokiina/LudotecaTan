package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer get(Long id) {
        return this.customerRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Customer> findAll() {

        return (List<Customer>) this.customerRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, CustomerDto dto) {

        Customer customer;
        // Recorrer la lista de Customer para verificar si hay otro usuario con el mismo nombre
        if (id == null) {
            if (customerRepository.existsByName(dto.getName())) {
                throw new RuntimeException("El nombre de usuario ya existe");
            }
            customer = new Customer();
        } else {
            //customer = this.get(id);
            customer = this.get(id);
            if (customer == null) {
                throw new RuntimeException("El cliente no fue encontrado");
            }
        }
        customer.setName(dto.getName());
        this.customerRepository.save(customer);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        this.customerRepository.deleteById(id);
    }

}