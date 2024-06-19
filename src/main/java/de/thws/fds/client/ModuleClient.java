package de.thws.fds.client;

import de.thws.fds.server.modules.model.Module;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Client class for interacting with Module resources via REST API.
 * Client only needs to know the dispatcher URL(see constructor).
 */
public class ModuleClient {
    private final String DISPATCHER_URL;
    private final RestTemplate restTemplate;

    public ModuleClient(String dispatcherUrl) {
        this.DISPATCHER_URL = dispatcherUrl;
        restTemplate = new RestTemplate();
    }


    /**
     * Creates a module and adds it to an existing partnerUniversity.
     *
     * @param module       the module that to be created and assigned to the university
     * @param universityId the ID of the partner university where the module will be added
     * @return the created module assigned to the specified PartnerUniversity
     */
    public Module addModuleToPartnerUniversity(Module module, Long universityId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Module> request = new HttpEntity<>(module, headers);

        ResponseEntity<Module> response = restTemplate.postForEntity(
                DISPATCHER_URL + universityId + "/modules/create", request, Module.class
        );
        return response.getBody();
    }


    /**
     * Retrieves a specific module assigned to a partner university by its ID.
     *
     * @param universityId The ID of the partner university that has the module assigned
     * @param moduleId     The ID of the module to retrieve
     * @return The Module object with the specified ID assigned to the partner university
     */
    public Module getModuleOfUniById(Long universityId, Long moduleId) {
        ResponseEntity<Module> response = restTemplate.getForEntity(
                DISPATCHER_URL + universityId + "/modules/"+moduleId, Module.class, moduleId
        );
        return response.getBody();
    }


    /**
     * Retrieves all modules of a specific partner university
     *
     * @param universityId the ID of the specific university that has the modules assigned
     * @return a list containing all modules of partner university
     */
    public List<Module> getAllModulesOfUni(Long universityId) {
        try {
            ResponseEntity<ModuleCollectionModel> response = restTemplate.exchange(
                    DISPATCHER_URL + universityId + "/modules",
                    HttpMethod.GET,
                    null,
                    ModuleCollectionModel.class
            );
            ModuleCollectionModel collectionModel = response.getBody();
            if (collectionModel != null && collectionModel.getEmbedded() != null) {
                return collectionModel.getEmbedded().getModuleList().stream()
                        .filter(Objects::nonNull) // Filter out null values
                        .map(EntityModel::getContent) // Get content from EntityModel
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.err.println("Error fetching modules: " + ex.getMessage());
            return new ArrayList<>();
        }
    }


    /**
     * Deletes a module of a partner university
     *
     * @param uniId    ID of the university with the module to delete
     * @param moduleId ID of the module to delete
     */
    public void deleteModuleOfPartnerUniversity(Long uniId, Long moduleId) {
        restTemplate.delete(DISPATCHER_URL + uniId+"/modules/"+moduleId+"/delete");
    }


    /**
     * Updates a module of a partner university
     *
     * @param uni_id         ID of the university with the module to update
     * @param moduleToUpdate ID of the module to be updated
     */
    public void updateModuleOfPartnerUniversity(Long uni_id, Module moduleToUpdate) {
        // Setzen Sie die Headers für die Anfrage
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Setzen Sie das zu aktualisierende Modul im HttpEntity
        HttpEntity<Module> requestEntity = new HttpEntity<>(moduleToUpdate, headers);

        // Führen Sie den PUT-Request aus, um das Modul zu aktualisieren
        restTemplate.exchange(
                DISPATCHER_URL + uni_id + "/modules/" + moduleToUpdate.getId() + "/update",
                HttpMethod.PUT,
                requestEntity,
                Void.class
        );
    }
}
