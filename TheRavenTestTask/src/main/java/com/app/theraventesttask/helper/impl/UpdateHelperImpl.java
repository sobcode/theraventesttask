package com.app.theraventesttask.helper.impl;

import com.app.theraventesttask.helper.UpdateHelper;
import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.model.dto.UpdateCustomerDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class UpdateHelperImpl implements UpdateHelper {

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
