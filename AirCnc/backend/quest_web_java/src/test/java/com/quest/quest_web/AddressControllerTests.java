package com.quest.quest_web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jayway.jsonpath.JsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTests {

        @Autowired
        protected MockMvc mockMvc;

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @BeforeEach
        public void setUp() {

                // clean seeders
                jdbcTemplate.execute(
                                "DELETE from user where id = 10000 or id = 10001 or id = 10002 or username = 'test';");

                Date startDate = Date.valueOf("2022-01-01");
                jdbcTemplate.execute(
                                "INSERT INTO user (id, username, password, role, creation_date, updated_date) VALUES (10000, 'userTest', '$2a$10$96XvyoHmgaFsN0fwyvKPm.VI7I1HBGHVE1obpLjIzxxF4pLuEgsEi', 'ROLE_USER', '"
                                                + startDate + "', '" + startDate + "')");
                jdbcTemplate.execute(
                                "INSERT INTO user (id, username, password, role, creation_date, updated_date) VALUES (10001, 'userTest2', '$2a$10$96XvyoHmgaFsN0fwyvKPm.VI7I1HBGHVE1obpLjIzxxF4pLuEgsEi', 'ROLE_USER', '"
                                                + startDate + "', '" + startDate + "')");
                jdbcTemplate.execute(
                                "INSERT INTO user (id, username, password, role, creation_date, updated_date) VALUES (10002, 'userTestAdmin', '$2a$10$96XvyoHmgaFsN0fwyvKPm.VI7I1HBGHVE1obpLjIzxxF4pLuEgsEi', 'ROLE_ADMIN', '"
                                                + startDate + "', '" + startDate + "')");

                // clean seeders
                jdbcTemplate.execute("DELETE from address where id = 10000 or id = 10001;");

                jdbcTemplate.execute(
                                "INSERT INTO address (id, name, city, country, postal_code, street, price, user_id, creation_date, updated_date) VALUES (10000, 'testUserCity', 'testUserCity', 'testCountry', '75000', 'streetTest', 222, 10000,'"
                                                + startDate + "', '" + startDate + "')");

                jdbcTemplate.execute(
                                "INSERT INTO address (id, name, city, country, postal_code, street, price, user_id, creation_date, updated_date) VALUES (10001, 'testUserCity', 'testUser2City', 'testCountry', '75000', 'streetTest', 222, 10001,'"
                                                + startDate + "', '" + startDate + "')");
        }

        @AfterEach
        public void cleanup() {
                // Delete the rows created during the test
                jdbcTemplate.execute(
                                "DELETE FROM user WHERE id = 10000 or id = 10001 or id = 10002 or username = 'test';");
                jdbcTemplate.execute("DELETE FROM address WHERE id = 10000 or id = 10001;");
                jdbcTemplate.execute("DELETE FROM booking WHERE id = 10000 or id = 10001 or id_user = 10000;");
        }

        @Test
        public void testCreate() throws Exception {
                String requestBody = "{\"username\": \"userTest\", \"password\": \"123\"}";
                String requestBodyCreate = "{\"name\": \"TestTest\", \"street\": \"testUserStreet\", \"city\": \"Paris\", \"country\": \"France\", \"postalCode\": \"75000\", \"price\": \"200\"}";

                this.mockMvc.perform(MockMvcRequestBuilders.post("/address")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyCreate))
                                .andDo(print())
                                .andExpect(status().isUnauthorized());

                String response = this.mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andReturn().getResponse().getContentAsString();

                String token = JsonPath.read(response, "$.token");

                this.mockMvc.perform(MockMvcRequestBuilders.post("/address")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyCreate))
                                .andDo(print())
                                .andExpect(status().isCreated());

        }

        @Test
        public void testUpdate() throws Exception {
                String requestBody = "{\"username\": \"userTest\", \"password\": \"123\"}";
                String requestBodyUpdate = "{\"name\": \"UpdatedAddress\", \"street\": \"testUserStreetUpdated\", \"city\": \"ParisUpdated\", \"country\": \"FranceUpdated\", \"postalCode\": \"12313\", \"price\": \"111\"}";

                this.mockMvc.perform(MockMvcRequestBuilders.put("/address/10000")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isUnauthorized());

                String response = this.mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andReturn().getResponse().getContentAsString();

                String token = JsonPath.read(response, "$.token");

                this.mockMvc.perform(MockMvcRequestBuilders.put("/address/10001")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyUpdate))
                                .andDo(print())
                                .andExpect(status().isForbidden());

                this.mockMvc.perform(MockMvcRequestBuilders.put("/address/10000")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyUpdate))
                                .andDo(print())
                                .andExpect(status().isOk());
        }

        @Test
        public void testGet() throws Exception {

                this.mockMvc.perform(MockMvcRequestBuilders.get("/address")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk());

                this.mockMvc.perform(MockMvcRequestBuilders.get("/address/20000000")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isNotFound());

                this.mockMvc.perform(MockMvcRequestBuilders.get("/address/10000")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk());

                this.mockMvc.perform(MockMvcRequestBuilders.get("/address/user/10000")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk());

                this.mockMvc.perform(MockMvcRequestBuilders.get("/address/user/20000")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isNotFound());
        }

        @Test
        public void testDelete() throws Exception {

                String requestBody = "{\"username\": \"userTest2\", \"password\": \"123\"}";

                this.mockMvc.perform(MockMvcRequestBuilders.delete("/address/10001"))
                                .andDo(print())
                                .andExpect(status().isUnauthorized());

                String response = this.mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andReturn().getResponse().getContentAsString();

                String token = JsonPath.read(response, "$.token");

                this.mockMvc.perform(MockMvcRequestBuilders.delete("/address/102201")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                                .andDo(print())
                                .andExpect(status().isNotFound());

                this.mockMvc.perform(MockMvcRequestBuilders.delete("/address/10000")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                                .andDo(print())
                                .andExpect(status().isForbidden());

                this.mockMvc.perform(MockMvcRequestBuilders.delete("/address/10001")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                                .andDo(print())
                                .andExpect(status().isOk());

        }
}
