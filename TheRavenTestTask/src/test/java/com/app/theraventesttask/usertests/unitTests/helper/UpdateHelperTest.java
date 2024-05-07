package com.app.theraventesttask.usertests.unitTests.helper;

import com.app.theraventesttask.helper.UpdateHelper;
import com.app.theraventesttask.model.Customer;
import com.app.theraventesttask.model.dto.CustomerDTO;
import com.app.theraventesttask.model.dto.UpdateCustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UpdateHelperTest {
    @Autowired
    private UpdateHelper updateHelper;

    @Test
    public void testCustomerPatcherWithFullNameChanges_thenEmailIsUnchangedAndFirstNameChanged()
            throws NoSuchFieldException, IllegalAccessException {
        String email = "frank@gmail.com";
        String newFullName = "Barbara Ku";
        Customer customer = new Customer("Frank Sinatra", email, "+380998887766",
                true, "FrSi01", new Date().getTime());
        UpdateCustomerDTO customerDTO = new UpdateCustomerDTO();
        customerDTO.setFullName(newFullName);

        updateHelper.customerPatcher(customer, customerDTO);

        assertEquals(email, customer.getEmail());
        assertEquals(newFullName, customer.getFullName());
    }

    @Test
    public void testCheckIfFieldsAreNonNull_thenReturnsTrue() throws IllegalAccessException {
        UpdateCustomerDTO customerDTO = new UpdateCustomerDTO(1, "Frank Sinatra", "+380998887766");

        boolean resp = updateHelper.checkIfFieldsAreNonNull(customerDTO);

        assertTrue(resp);
    }

    @Test
    public void testCheckIfFieldsAreNonNullWithNullValue_thenReturnsFalse() throws IllegalAccessException {
        UpdateCustomerDTO customerDTO = new UpdateCustomerDTO(1, "Frank Sinatra", "+380998887766");
        customerDTO.setPhone(null);

        boolean resp = updateHelper.checkIfFieldsAreNonNull(customerDTO);

        assertFalse(resp);
    }
}
