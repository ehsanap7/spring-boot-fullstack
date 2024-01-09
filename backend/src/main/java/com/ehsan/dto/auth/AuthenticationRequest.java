package com.ehsan.dto.auth;

public record AuthenticationRequest(
    String username,
    String password
) {

}
