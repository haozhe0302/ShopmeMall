package com.shopme.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {
    @Test
    public void testEncodePassword(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "haozhe2022";
        String encoderPassword = passwordEncoder.encode(rawPassword);

        System.out.println(encoderPassword);

        boolean matches = passwordEncoder.matches(rawPassword, encoderPassword);

        assertThat(matches).isTrue();
    }
}
