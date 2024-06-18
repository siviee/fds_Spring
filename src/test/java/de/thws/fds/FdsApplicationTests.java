package de.thws.fds;

import de.thws.fds.client.ModuleClient;
import de.thws.fds.client.PartnerUniversityClient;
import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("text")
class FdsApplicationTests {
    @LocalServerPort
    private int port;

    private String BASE_URL;
    private String BASE_URL_MODULE;

    private PartnerUniversityClient partnerUniversityClient;
    private ModuleClient moduleClient;
    TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        BASE_URL = "http://localhost:" + port + "/api/v1";
        partnerUniversityClient = new PartnerUniversityClient(BASE_URL);

        BASE_URL_MODULE = BASE_URL + "/universities/";
        moduleClient = new ModuleClient(BASE_URL_MODULE);
        restTemplate = new TestRestTemplate();

    }

    @Test
    void testGetUniById() {
        Long universityId = 1L;
        PartnerUniversity retrievedUniversity = partnerUniversityClient.getPartnerUniversityById(universityId);
        assertNotNull(retrievedUniversity);
        assertNotNull(retrievedUniversity.getId());
        assertEquals("Christ University", retrievedUniversity.getName());
        assertEquals("India", retrievedUniversity.getCountry());
        assertEquals("www.christ-university.com", retrievedUniversity.getDepartmentUrl());
        assertEquals("Dr. Ravi Kumar", retrievedUniversity.getContactPerson());
        //For Debugging purpose only ----> System.out.println(partnerUniversityClient.getPartnerUniversityById(2L).getName());
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
    void testDeleteUni() {
        // Retrieve all partner universities
        List<PartnerUniversity> universities = partnerUniversityClient.getAllPartnerUniversities();
        // Get the new created university from List (assumption: the new created unis are at the bottom of the list)
        PartnerUniversity uni = universities.get(3);
        Long id = uni.getId();
        partnerUniversityClient.deletePartnerUniversity(id);
        // check for a specific status code namely 404
        assertThrows(HttpClientErrorException.NotFound.class, () -> partnerUniversityClient.getPartnerUniversityById(id), "Expected a 404 Not Found exception to be thrown");
    }

    @Test
    void testGetAllUniversities() {
        // Get all partner universities
        List<PartnerUniversity> universities = partnerUniversityClient.getAllPartnerUniversities();

        // check if universities are called correctly by testing notNull
        assertNotNull(universities);

        // In-memory has exactly 3 universities, check if the size corresponds
        assertEquals(3, universities.size());

        // Check first University
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
    void testUpdateUni() {
        PartnerUniversity uni = null;
        Long id = 2L;
        try {
            uni = partnerUniversityClient.getPartnerUniversityById(id);
            uni.setName("Update");
            partnerUniversityClient.updatePartnerUniversity(id, uni);
            assertEquals("Update", uni.getName());
        } finally {
            if (uni != null) {
                // Reset the name field back to origin name
                uni.setName("Lucian Blaga University");
                partnerUniversityClient.updatePartnerUniversity(id, uni);
            }
        }
    }


    //---------------------------------------CRUD FOR MODULES-----------------------------------------------------------

    //TODO: CRUD Testing for Modules
//    @Test
//    void testAddModuleToPartnerUniversity() {
//        // Create or retrieve an existing university
//        Long universityId = 2L;
//        PartnerUniversity existingUniversity = restTemplate.getForObject(BASE_URL_MODULE  + universityId, PartnerUniversity.class);
//        assertNotNull(existingUniversity);
//
//        // Create a module
//        Module module = new Module();
//        module.setName("Algebra");
//        module.setSemester(1);
//        module.setCreditPoints(1);
//
//        // Add the module using the client
//        Module createdModule = moduleClient.addModuleToPartnerUniversity(module, universityId);
//
//        // Verify response
//        assertNotNull(createdModule);
//        assertNotNull(createdModule.getId());
//        assertEquals(module.getName(), createdModule.getName());
//        assertEquals(module.getSemester(), createdModule.getSemester());
//        assertEquals(module.getCreditPoints(), createdModule.getCreditPoints());
//        assertEquals(universityId, createdModule.getPartnerUniversity().getId());
//    }


    @Test
    void contextLoads() {
    }


}
