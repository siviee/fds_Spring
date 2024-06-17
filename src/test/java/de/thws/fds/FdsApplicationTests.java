package de.thws.fds;

import de.thws.fds.client.PartnerUniversityClient;
import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("text")
class FdsApplicationTests {
    @LocalServerPort
    private int port;

    private String BASE_URL;

    private PartnerUniversityClient partnerUniversityClient;

    @BeforeEach
    void setUp() {
        BASE_URL = "http://localhost:" + port + "/api/v1";
        partnerUniversityClient = new PartnerUniversityClient(BASE_URL);
    }

    @Test
    void testAddPartnerUniversity() {
        PartnerUniversity partnerUniversity = new PartnerUniversity(
                "Thws",
                "Germany",
                "Department of IT Wuerzburg",
                "www.thws-wuerzburg.de",
                "Dr.Dr. Alex Müller",
                15,
                10,
                LocalDate.of(2020, 1, 31),
                LocalDate.of(2020, 9, 28)
        );

        PartnerUniversity createdUniversity = partnerUniversityClient.addPartnerUniversity(partnerUniversity);

        assertNotNull(createdUniversity);
        assertNotNull(createdUniversity.getId());
        assertEquals(partnerUniversity.getName(), createdUniversity.getName());
        assertEquals(partnerUniversity.getCountry(), createdUniversity.getCountry());
        assertEquals(partnerUniversity.getDepartmentName(), createdUniversity.getDepartmentName());
        assertEquals(partnerUniversity.getContactPerson(), createdUniversity.getContactPerson());
        assertEquals(partnerUniversity.getOutboundStudents(), createdUniversity.getOutboundStudents());
        assertEquals(partnerUniversity.getInboundStudents(), createdUniversity.getInboundStudents());
        assertEquals(partnerUniversity.getNextSpringSemesterStart(), createdUniversity.getNextSpringSemesterStart());
        assertEquals(partnerUniversity.getNextAutumnSemesterStart(), createdUniversity.getNextAutumnSemesterStart());
    }

    @Test
    void testGetAllUniversities() {
        // Get all partner universities
        List<PartnerUniversity> universities = partnerUniversityClient.getAllPartnerUniversities();

        // check if universities are called correctly by testing notNull
        assertNotNull(universities);

        // In-memory has exactly 3 universities, check if the size corresponds
        assertEquals(3, universities.size());

        // Check first University (few attributes are also sufficient)
        PartnerUniversity retrievedUniversity1 = universities.get(0);
        assertNotNull(retrievedUniversity1.getId());
        assertEquals("Christ University", retrievedUniversity1.getName());
        assertEquals("India", retrievedUniversity1.getCountry());
        assertEquals("www.christ-university.com", retrievedUniversity1.getDepartmentUrl());
        assertEquals("Dr. Ravi Kumar", retrievedUniversity1.getContactPerson());

        // Check second university
        PartnerUniversity retrievedUniversity2 = universities.get(1);
        assertNotNull(retrievedUniversity2.getId());
        assertEquals("Lucian Blaga University", retrievedUniversity2.getName());
        assertEquals("Romania", retrievedUniversity2.getCountry());
        assertEquals("www.uni-lucian-blaga.ro", retrievedUniversity2.getDepartmentUrl());
        assertEquals("Dr. Ioan Alexandru Baciu", retrievedUniversity2.getContactPerson());

        // Check third university
        PartnerUniversity retrievedUniversity3 = universities.get(2);
        assertNotNull(retrievedUniversity3.getId());
        assertEquals("University Of Sheffield", retrievedUniversity3.getName());
        assertEquals("UK", retrievedUniversity3.getCountry());
        assertEquals("www.uni-sheffield.uk", retrievedUniversity3.getDepartmentUrl());
        assertEquals("Dr. Rose Smith", retrievedUniversity3.getContactPerson());
    }
    @Test
    void contextLoads() {
    }


}
