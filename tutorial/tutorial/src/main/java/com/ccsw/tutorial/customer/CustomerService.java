package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;

import java.util.List;

/**
 * @author ccsw
 *
 */
public interface CustomerService {

    /**
     * Recupera una {@link Customer} a partir de su ID
     *
     * @param id PK de la entidad
     * @return {@link Customer}
     */
    Customer get(Long id);

    /**
     * Método para recuperar todas las {@link Customer}
     *
     * @return {@link List} de {@link Customer}
     */
    List<Customer> findAll();

    /**
     * Método para crear o actualizar una {@link Customer}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, CustomerDto dto);

    /**
     * Método para borrar una {@link Customer}
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

}