package com.app.theraventesttask.helper.impl;

import com.app.theraventesttask.helper.UpdateHelper;
import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.model.dto.UpdateCustomerDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Helper implementation to assist with updating Customer entities based on UpdateCustomerDTO objects.
 */
@Component
public class UpdateHelperImpl implements UpdateHelper {
    /**
     * Updates fields of an existing Customer object with non-null values from a UpdateCustomerDTO object.
     * The 'id' field is excluded from the update.
     *
     * @param existingCustomer The existing Customer object to be updated.
     * @param newCustomer The UpdateCustomerDTO object containing new values.
     * @throws IllegalAccessException If access to a field is denied.
     * @throws NoSuchFieldException   If a field is not found.
     */
    @Override
    public void customerPatcher(Customer existingCustomer, UpdateCustomerDTO newCustomer) throws IllegalAccessException, NoSuchFieldException {
        Class<?> customerDTOClass = UpdateCustomerDTO.class;
        Field[] customerDTOFields = customerDTOClass.getDeclaredFields();

        for(Field field : customerDTOFields) {
            field.setAccessible(true);

            Object value = field.get(newCustomer);
            if (value != null && !field.getName().equals("id")) {
                Field customerField = Customer.class.getDeclaredField(field.getName());
                customerField.setAccessible(true);

                customerField.set(existingCustomer, value);

                customerField.setAccessible(false);
            }

            field.setAccessible(false);
        }
    }

    /**
     * Checks if all fields of a UserDTO object are non-null, except for the 'id' field.
     *
     * @param customerDTO The UpdateCustomerDTO object to be checked.
     * @return true if all fields (except 'id') are non-null, false otherwise.
     * @throws IllegalAccessException If access to a field is denied.
     */
    @Override
    public boolean checkIfFieldsAreNonNull(UpdateCustomerDTO customerDTO) throws IllegalAccessException {
        Class<?> customerDTOClass = UpdateCustomerDTO.class;
        Field[] customerDTOFields = customerDTOClass.getDeclaredFields();

        for(Field field : customerDTOFields) {
            field.setAccessible(true);

            Object value = field.get(customerDTO);
            if(value == null && !field.getName().equals("id")) {
                return false;
            }

            field.setAccessible(false);
        }
        return true;
    }
}
