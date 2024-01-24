package com.app.theraventesttask.config.jwt;

import com.app.theraventesttask.model.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class responsible for creating JwtUser instances for JWT authentication.
 *
 * This class provides a static method to create JwtUser instances based on customer details.
 */
public class JwtUserFactory {

    /**
     * Private constructor to prevent instantiation as all methods are static.
     */
    public JwtUserFactory() {
    }

    /**
     * Creates a JwtUser instance based on customer details.
     *
     * @param customer Customer entity containing user details.
     * @return JwtUser instance representing the user for JWT authentication.
     */
    public static JwtUser createJwtUser(Customer customer){
        return new JwtUser(customer.getId(),
                customer.getEmail(),
                customer.getPassword(),
                true,
                mapRoleToGrantedAuthorities());
    }

    /**
     * Maps a user role to a list of GrantedAuthority.
     *
     * Currently, it assigns the "Admin" role to the user.
     * Modify this method as needed to reflect actual roles and permissions.
     *
     * @return List of GrantedAuthority representing the user's roles.
     */
    private static List<GrantedAuthority> mapRoleToGrantedAuthorities(){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("Admin"));

        return authorities;
    }
}
