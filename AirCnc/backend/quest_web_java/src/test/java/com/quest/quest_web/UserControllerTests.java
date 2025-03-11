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
public class UserControllerTests {

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
        }

        @AfterEach
        public void cleanup() {
                // Delete the rows created during the test
                jdbcTemplate.execute(
                                "DELETE FROM user WHERE id = 10000 or id = 10001 or id = 10002 or username = 'test';");
                jdbcTemplate.execute(
                                "DELETE FROM address WHERE id = 10000 or id = 10001 or user_id = 10000 or user_id = 10001;");
                jdbcTemplate.execute("DELETE FROM booking WHERE id = 10000 or id = 10001 or id_user = 10000;");
        }

        @Test
        public void testRegister() throws Exception {
                String requestBody = "{\"username\": \"test\", \"password\": \"test\"}";

                this.mockMvc.perform(MockMvcRequestBuilders.post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isCreated());

                this.mockMvc.perform(MockMvcRequestBuilders.post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andDo(print())
                                .andExpect(status().isConflict());

                String response = this.mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andReturn().getResponse().getContentAsString();

                String token = JsonPath.read(response, "$.token");

                this.mockMvc.perform(MockMvcRequestBuilders.get("/me")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                                .andDo(print())
                                .andExpect(jsonPath("$.username").exists())
                                .andExpect(jsonPath("$.role").exists())
                                .andExpect(status().isOk());
        }

        @Test
        public void testUpdate() throws Exception {
                String requestBody = "{\"username\": \"userTest\", \"password\": \"123\"}";
                String requestBodyUpdate = "{\"username\": \"userTest2\"}";

                this.mockMvc.perform(MockMvcRequestBuilders.put("/user/10000")
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

                this.mockMvc.perform(MockMvcRequestBuilders.put("/user/10000")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyUpdate))
                                .andDo(print())
                                .andExpect(status().isConflict());

                requestBodyUpdate = "{\"username\": \"userTest3\"}";

                this.mockMvc.perform(MockMvcRequestBuilders.put("/user/10000")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyUpdate))
                                .andDo(print())
                                .andExpect(status().isOk());
        }

}
