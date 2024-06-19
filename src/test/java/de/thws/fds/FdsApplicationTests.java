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
import de.thws.fds.server.modules.model.Module;

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

    //---------------------------Testing Endpoint "Partner University" ------------------------------------------------

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


    //----------------------------------Testing Endpoint "Modules" -----------------------------------------------------
    @Test
    void testGetModuleOfUniById() {
        PartnerUniversity existingUniversity = partnerUniversityClient.getPartnerUniversityById(3L);
        assertNotNull(existingUniversity);

        Long moduleId = 4L;
        Module retrievedModule = moduleClient.getModuleOfUniById(existingUniversity.getId(), moduleId);

        // Verify response
        assertNotNull(retrievedModule);
        assertNotNull(retrievedModule.getId());
        assertEquals(moduleId, retrievedModule.getId());
        assertEquals("Operating Systems", retrievedModule.getName());
        assertEquals(3, retrievedModule.getSemester());
        assertEquals(10, retrievedModule.getCreditPoints());

    }
    @Test
    void testAddModuleToPartnerUniversity() {
        // Create or retrieve an existing university
        PartnerUniversity existingUniversity = partnerUniversityClient.getPartnerUniversityById(1L);
        assertNotNull(existingUniversity);

        // Create a module
        Module module = new Module("Algebra", 1, 1, existingUniversity);

        // Add the module using the client
        Module createdModule = moduleClient.addModuleToPartnerUniversity(module, existingUniversity.getId());

        // Verify response
        assertNotNull(createdModule);
        assertNotNull(createdModule.getId());
        assertEquals(module.getName(), createdModule.getName());
        assertEquals(module.getSemester(), createdModule.getSemester());
        assertEquals(module.getCreditPoints(), createdModule.getCreditPoints());
        System.out.println(existingUniversity.getModules());
    }

    @Test
    void getAllModulesOfAUni() {

        Long universityId = 1L;

        // Retrieve all modules for the university
        List<Module> modules = moduleClient.getAllModulesOfUni(universityId);

        assertNotNull(modules);
        Module module1 = modules.get(0);
        assertNotNull(module1.getId());
        assertEquals("Quantum Computing", module1.getName());
        assertEquals(1, module1.getSemester());
        assertEquals(2, module1.getCreditPoints());
        Module module2 = modules.get(1);
        assertNotNull(module2.getId());
        assertEquals("Foundation of Distributed Systems", module2.getName());
        assertEquals(4, module2.getSemester());
        assertEquals(15, module2.getCreditPoints());
        Module module3 = modules.get(2);
        assertNotNull(module3.getId());
        assertEquals("Algebra", module3.getName());
        assertEquals(1, module3.getSemester());
        assertEquals(1, module3.getCreditPoints());

        System.out.println(modules.size() + " " + module3);
    }


    @Test
    void testDeleteModuleOfPartnerUniversity() {
        PartnerUniversity uni = null;
        Module module = null;
        Module originalState = null;

        try {
            uni = partnerUniversityClient.getPartnerUniversityById(3L);
            List<Module> modules = moduleClient.getAllModulesOfUni(3L);

            module = modules.get(0);
            originalState = module;

            // Modul löschen
            moduleClient.deleteModuleOfPartnerUniversity(uni.getId(), module.getId());
            assertThrows(HttpClientErrorException.NotFound.class, () -> moduleClient.getModuleOfUniById(3L, 4L), "Expected a 404 Not Found exception to be thrown");


        } finally {
            //restore deleted module
            if (originalState != null) {
                try {
                    // Versuche, das ursprüngliche Modul wiederherzustellen
                    moduleClient.addModuleToPartnerUniversity(originalState, 3L);
                } catch (Exception e) {
                    // Falls hier ein Fehler auftritt, kannst du ihn loggen oder entsprechend behandeln
                    System.err.println("Error restoring original module state: " + e.getMessage());
                }
            }
        }
    }
    //Encountered Issue: I debugged the deletion test, and it seems like the field partnerUniversity inside a module
    //is null, even though this crud operation is functioning in Postman. Adding however seems to have no problems.
    //Breakpoint set here for  in line 237


    @Test
    void testUpdateModuleOfAUni2() {
        PartnerUniversity uni = null;
        Module module = null;
        String originalName = "";

        try {
            uni = partnerUniversityClient.getPartnerUniversityById(2L);
            List<Module> modules = uni.getModules();
            module = modules.get(0);
            originalName = module.getName();

            // Setzen der neuen Werte für das Modul
            module.setName("Computer Architecture");

            // Aktualisierung des Moduls über den Client
            moduleClient.updateModuleOfPartnerUniversity(uni.getId(), module);

            // Überprüfung, ob das Modul erfolgreich aktualisiert wurde
            assertEquals("Computer Architecture", module.getName());
        } finally {
            // Wiederherstellen des ursprünglichen Namens für das Modul
            if (module != null) {
                module.setName(originalName);
                moduleClient.updateModuleOfPartnerUniversity(uni.getId(), module);
            }
        }
    }


    @Test
    void contextLoads() {
    }


}
