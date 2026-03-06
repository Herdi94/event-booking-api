package com.event.booking.mgmt;

import com.event.booking.mgmt.dto.UserProfileResponse;
import com.event.booking.mgmt.dto.UserRegistrationRequest;
import com.event.booking.mgmt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=local")
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRegistration(){
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Jaka");
        request.setEmail("jaka@gmail.com");
        request.setPassword("user@1");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRegistrationRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/user/registration", entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(
            username = "jaka@gmail.com"
    )
    void getProfile() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/user/profile"))
                    .andExpect(status().isOk());
        }
}
