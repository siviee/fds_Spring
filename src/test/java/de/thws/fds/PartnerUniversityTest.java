package de.thws.fds;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PartnerUniversityTest {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Checks the Serialization and Deserialization of PartnerUniversity
     *
     * @throws Exception if De-/Serialization is incorrect
     */
    @Test
    public void testSerialization() throws Exception {
        PartnerUniversity university = new PartnerUniversity();
        university.setName("Thws");
        university.setCountry("Germany");
        university.setDepartmentName("Department of IT Wuerzburg");
        university.setDepartmentUrl("www.thws-wuerzburg.de");
        university.setContactPerson("Dr.Dr. Alex Müller");
        university.setOutboundStudents(15);
        university.setInboundStudents(10);
        university.setNextSpringSemesterStart(LocalDate.of(2020, 1, 31));
        university.setNextAutumnSemesterStart(LocalDate.of(2020, 9, 28));

        String json = objectMapper.writeValueAsString(university);
        assertNotNull(json);
        System.out.println("Serialized JSON: " + json);
    }

    @Test
    public void testDeserialization() throws Exception {
        String json = "{\"id\":null,\"name\":\"Thws\",\"country\":\"Germany\",\"departmentName\":\"Department of IT Wuerzburg\",\"departmentUrl\":\"www.thws-wuerzburg.de\",\"contactPerson\":\"Dr.Dr. Alex Müller\",\"outboundStudents\":15,\"inboundStudents\":10,\"nextSpringSemesterStart\":\"2020-01-31\",\"nextAutumnSemesterStart\":\"2020-09-28\",\"modules\":null}";

        PartnerUniversity university = objectMapper.readValue(json, PartnerUniversity.class);
        assertNotNull(university);
        assertEquals("Thws", university.getName());
        assertEquals("Germany", university.getCountry());
        System.out.println("Deserialized Object: " + university);
    }
}
