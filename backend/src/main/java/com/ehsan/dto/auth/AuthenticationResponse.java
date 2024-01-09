package com.ehsan.dto.auth;

import com.ehsan.dto.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO) {

}
