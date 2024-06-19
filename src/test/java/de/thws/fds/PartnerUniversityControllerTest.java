package de.thws.fds;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests some functionalities of the UniversityController Class
 * Kept for Debugging
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PartnerUniversityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //Checks the functionality of the UniversityController Class
    @Test
    public void testAddUniversity() throws Exception {
        String json = "{\"name\":\"Thws\",\"country\":\"Germany\",\"departmentName\":\"Department of IT Wuerzburg\",\"departmentUrl\":\"www.thws-wuerzburg.de\",\"contactPerson\":\"Dr.Dr. Alex Müller\",\"outboundStudents\":15,\"inboundStudents\":10,\"nextSpringSemesterStart\":\"2020-01-31\",\"nextAutumnSemesterStart\":\"2020-09-28\"}";

        mockMvc.perform(post("/api/v1/universities/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
        //Test helped identify problems with JSON serialisation. Test passed successfully
    }

    @Test
    public void testGetUniById() throws Exception {
        long id = 1L;
        mockMvc.perform(get("/api/v1/universities/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}
