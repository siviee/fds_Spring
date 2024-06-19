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

public class ModuleClient {
    private final String DISPATCHER_URL;
    private final RestTemplate restTemplate;

    public ModuleClient(String dispatcherUrl) {
        this.DISPATCHER_URL = dispatcherUrl;
        restTemplate = new RestTemplate();
    }

    public Module addModuleToPartnerUniversity(Module module, Long universityId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Module> request = new HttpEntity<>(module, headers);

        ResponseEntity<Module> response = restTemplate.postForEntity(
                DISPATCHER_URL + universityId + "/modules/create", request, Module.class
        );
        return response.getBody();
    }

    public Module getModuleOfUniById(Long universityId, Long moduleId) {
        ResponseEntity<Module> response = restTemplate.getForEntity(
                DISPATCHER_URL + universityId + "/modules/{moduleId}", Module.class, moduleId
        );
        return response.getBody();
    }
    public List<Module> getAllModulesOfUni(Long universityId) {
        try {
            ResponseEntity<ModuleCollectionModel> response = restTemplate.exchange(
                    DISPATCHER_URL +  universityId + "/modules",
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

    public void deleteModuleOfPartnerUniversity(Long uniId,Long moduleId) {
        restTemplate.delete(DISPATCHER_URL +"{uniId}/modules/{moduleId}/delete",uniId, moduleId);
    }

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
