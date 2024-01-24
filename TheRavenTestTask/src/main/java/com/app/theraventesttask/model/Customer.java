package com.app.theraventesttask.model;

import com.app.theraventesttask.model.dto.CustomerDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class Customer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "created")
    private long created;

    @Column(name = "updated")
    private long updated;

    @Column(name = "full_name")
    @NonNull
    private String fullName;

    @Column(name = "email", unique = true)
    @NonNull
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "password")
    @NonNull
    private String password;

    public Customer(@NonNull String fullName, @NonNull String email,
                    String phone, boolean isActive,
                    @NonNull String password, long created) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.isActive = isActive;
        this.password = password;
        this.created = created;
    }

    public static Customer getCustomerFromCustomerDTO(CustomerDTO customerDTO) {
        return new Customer(customerDTO.getFullName(), customerDTO.getEmail(),
                customerDTO.getPhone(), true, customerDTO.getPassword(),
                new Date().getTime());
    }

    public boolean getIsActive() {
        return isActive;
    }
}