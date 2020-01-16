package com.kaka.webgm.security;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {
    private final Md5PasswordEncoder encoder = new Md5PasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return encoder.encodePassword(rawPassword.toString(), "gm");
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encoder.isPasswordValid(encodedPassword, rawPassword.toString(), "gm");
    }
}
