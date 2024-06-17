package de.thws.fds;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PartnerUniversityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
}
