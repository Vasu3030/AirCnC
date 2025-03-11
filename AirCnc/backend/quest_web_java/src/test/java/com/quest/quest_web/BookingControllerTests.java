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
public class BookingControllerTests {

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

                // clean seeders
                jdbcTemplate.execute("DELETE from booking where id = 10000 or id = 10001 or id_user = 10000;");

                jdbcTemplate.execute(
                                "INSERT INTO booking (id, id_address, id_user, status, price, from_date, to_date, created_at, updated_at) VALUES (10000, 10000, 10001, 'pending', 444,  '"
                                                + startDate + "',  '" + startDate + "','"
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

                this.mockMvc.perform(MockMvcRequestBuilders.post("/booking")
                                .param("to", "2051-12-31")
                                .param("from", "2050-12-31")
                                .param("id_address", "10001")
                                .param("price", "441")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isUnauthorized());

                String response = this.mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andReturn().getResponse().getContentAsString();

                String token = JsonPath.read(response, "$.token");

                this.mockMvc.perform(MockMvcRequestBuilders.post("/booking")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .param("to", "2051-12-31")
                                .param("from", "2050-12-31")
                                .param("id_address", "10001")
                                .param("price", "441")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isCreated());

                this.mockMvc.perform(MockMvcRequestBuilders.post("/booking")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .param("to", "2051-12-31")
                                .param("from", "2050-12-31")
                                .param("id_address", "10001")
                                .param("price", "441")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isConflict());
        }

        @Test
        public void testUpdate() throws Exception {
                String requestBody = "{\"username\": \"userTest2\", \"password\": \"123\"}";
                String requestBody2 = "{\"username\": \"userTest\", \"password\": \"123\"}";
                this.mockMvc.perform(MockMvcRequestBuilders.put("/booking/10000")
                                .param("status", "true"))
                                .andDo(print())
                                .andExpect(status().isUnauthorized());

                String response = this.mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andReturn().getResponse().getContentAsString();

                String token = JsonPath.read(response, "$.token");

                this.mockMvc.perform(MockMvcRequestBuilders.put("/booking/10000")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .param("status", "true"))
                                .andDo(print())
                                .andExpect(status().isForbidden());

                this.mockMvc.perform(MockMvcRequestBuilders.put("/booking/103000")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .param("status", "true"))
                                .andDo(print())
                                .andExpect(status().isNotFound());

                String response2 = this.mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody2))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andReturn().getResponse().getContentAsString();

                String token2 = JsonPath.read(response2, "$.token");

                this.mockMvc.perform(MockMvcRequestBuilders.put("/booking/10000")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token2)
                                .param("status", "true"))
                                .andDo(print())
                                .andExpect(status().isOk());
        }

        @Test
        public void testGet() throws Exception {

                this.mockMvc.perform(MockMvcRequestBuilders.get("/booking/user/10000")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isUnauthorized());

                String requestBody = "{\"username\": \"userTest2\", \"password\": \"123\"}";
                String response = this.mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andReturn().getResponse().getContentAsString();

                String token = JsonPath.read(response, "$.token");

                this.mockMvc.perform(MockMvcRequestBuilders.get("/booking/user/10000")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                                .andDo(print())
                                .andExpect(status().isForbidden());

                String requestBody2 = "{\"username\": \"userTest\", \"password\": \"123\"}";
                String response2 = this.mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody2))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andReturn().getResponse().getContentAsString();

                String token2 = JsonPath.read(response2, "$.token");

                this.mockMvc.perform(MockMvcRequestBuilders.get("/booking/user/10000")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token2))
                                .andDo(print())
                                .andExpect(status().isOk());

                this.mockMvc.perform(MockMvcRequestBuilders.get("/booking/address/10000")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                                .andDo(print())
                                .andExpect(status().isForbidden());

                this.mockMvc.perform(MockMvcRequestBuilders.get("/booking/address/10000")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token2))
                                .andDo(print())
                                .andExpect(status().isOk());
        }

        @Test
        public void testDelete() throws Exception {

                String requestBody = "{\"username\": \"userTest2\", \"password\": \"123\"}";

                this.mockMvc.perform(MockMvcRequestBuilders.delete("/booking/10000"))
                                .andDo(print())
                                .andExpect(status().isUnauthorized());

                String response = this.mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andReturn().getResponse().getContentAsString();

                String token = JsonPath.read(response, "$.token");

                this.mockMvc.perform(MockMvcRequestBuilders.delete("/booking/102201")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                                .andDo(print())
                                .andExpect(status().isNotFound());

                this.mockMvc.perform(MockMvcRequestBuilders.delete("/booking/10000")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                                .andDo(print())
                                .andExpect(status().isOk());
        }
}
