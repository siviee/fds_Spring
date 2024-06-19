package de.thws.fds;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thws.fds.server.modules.controller.UniversityModuleController;
import de.thws.fds.server.modules.model.Module;
import de.thws.fds.server.modules.service.ModuleServiceImpl;
import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import de.thws.fds.server.partner_universities.service.PartnerUniversityServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UniversityModuleController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UniversityModuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ModuleServiceImpl moduleService;

    @MockBean
    private PartnerUniversityServiceImpl universityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddModuleToPartnerUniversity() throws Exception {
        // Mocking the service behavior

        PartnerUniversity university = new PartnerUniversity("Thws","Germany","Department of IT Wuerzburg","www.thws-wuerzburg.de",
                "Dr.Dr. Alex Müller",15,10, LocalDate.of(2020, 1,31),LocalDate.of(2020,9,29));
        Module module = new Module("Algebra", 1, 5, university);
        Module savedModule = new Module("Algebra", 1, 5, university);

        when(universityService.getUniversityById(university.getId())).thenReturn(java.util.Optional.of(university));
        when(moduleService.createModuleOfUni(any(Long.class), any(Module.class))).thenReturn(savedModule);

        // Convert Module object to JSON
        String jsonModule = objectMapper.writeValueAsString(module);

        // Perform the POST request
        mockMvc.perform(post("/api/v1/universities/{universityId}/modules/create", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonModule))
                .andExpect(status().isOk());
    }
}
