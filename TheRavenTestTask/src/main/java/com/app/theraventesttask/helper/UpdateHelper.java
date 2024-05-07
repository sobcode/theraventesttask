package com.app.theraventesttask.helper;

import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.model.dto.UpdateCustomerDTO;

/**
 * Helper interface to assist with updating Customer entities based on UpdateCustomerDTO objects.
 */
public interface UpdateHelper {
    void customerPatcher(Customer existingCustomer, UpdateCustomerDTO newCustomer)
            throws IllegalAccessException, NoSuchFieldException;

    boolean checkIfFieldsAreNonNull(UpdateCustomerDTO newCustomer) throws IllegalAccessException;
}
