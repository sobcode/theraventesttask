package com.app.theraventesttask.repository;

import com.app.theraventesttask.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing and managing Customer entities in the database.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    /**
     * Finds a customer by their email address.
     *
     * @param email The email address of the customer to search for.
     * @return The Customer entity if found, otherwise null.
     */
    Customer findCustomerByEmail(String email);

    /**
     * Finds a customer by their ID.
     *
     * @param id The ID of the customer to search for.
     * @return The Customer entity if found, otherwise null.
     */
    Customer findCustomerById(Long id);

    /**
     * Finds all customers matching the specified criteria and returns a paginated result.
     *
     * @param fullName The full name of the customer to search for (partial match).
     * @param email    The email address of the customer to search for (partial match).
     * @param phone    The phone number of the customer to search for (partial match).
     * @param pageable Pagination information for the result set.
     * @return A Page containing the list of customers matching the specified criteria.
     */
    Page<Customer> findAllByFullNameContainsAndEmailContainsAndPhoneContainsAndIsActiveIsTrue(String fullName,
                                                                                              String email, String phone,
                                                                                              Pageable pageable);
}
