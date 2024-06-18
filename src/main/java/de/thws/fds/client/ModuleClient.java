package de.thws.fds.client;

import de.thws.fds.server.modules.model.Module;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ModuleClient {
    private final String DISPATCHER_URL;
    private final RestTemplate restTemplate;

    public ModuleClient(String dispatcherUrl) {
        this.DISPATCHER_URL = dispatcherUrl;
        restTemplate = new RestTemplate();
    }
    public Module addModuleToPartnerUniversity(Module module,Long universityId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Module> request = new HttpEntity<>(module, headers);

        ResponseEntity<Module> response = restTemplate.postForEntity(
                DISPATCHER_URL + universityId+"/modules/create", request, Module.class
        );
        return response.getBody();
    }

}
