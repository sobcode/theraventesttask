package com.app.theraventesttask.repository;

import com.app.theraventesttask.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findCustomerByEmail(String email);
    Customer findCustomerById(Long id);
    Page<Customer> findAllByFullNameContainsAndEmailContainsAndPhoneContains(String fullName,
                                                                             String email, String phone,
                                                                             Pageable pageable);
}
