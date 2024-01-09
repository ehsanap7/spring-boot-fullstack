package com.ehsan.service.auth;

import com.ehsan.dto.CustomerDTO;
import com.ehsan.dto.auth.AuthenticationRequest;
import com.ehsan.dto.auth.AuthenticationResponse;
import com.ehsan.jwt.JWTUtil;
import com.ehsan.mapper.CustomerDTOMapper;
import com.ehsan.model.customer.Customer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomerDTOMapper customerDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationService(AuthenticationManager authenticationManager, CustomerDTOMapper customerDTOMapper, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.customerDTOMapper = customerDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        Customer principal = (Customer) authenticate.getPrincipal();
        CustomerDTO customerDTO = customerDTOMapper.apply(principal);

        String token = jwtUtil.issueToken(customerDTO.username(), customerDTO.roles());
        return new AuthenticationResponse(token, customerDTO);
    }

}
