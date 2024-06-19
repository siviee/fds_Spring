package de.thws.fds.client;

import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Client class for interacting with PartnerUniversity resources via REST API.
 * Client only needs to know the dispatcher URL(see constructor).
 */
public class PartnerUniversityClient {
    private final String DISPATCHER_URL;
    private final RestTemplate restTemplate;

    public PartnerUniversityClient(String dispatcherUrl) {
        this.DISPATCHER_URL = dispatcherUrl;
        restTemplate = new RestTemplate();
    }


    /**
     * Adds a new partner university.
     *
     * @param partnerUniversity the PartnerUniversity object to add
     * @return the created PartnerUniversity object
     */
    public PartnerUniversity addPartnerUniversity(PartnerUniversity partnerUniversity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PartnerUniversity> request = new HttpEntity<>(partnerUniversity, headers);

        ResponseEntity<PartnerUniversity> response = restTemplate.postForEntity(
                DISPATCHER_URL + "/universities/create", request, PartnerUniversity.class
        );
        return response.getBody();
    }


    /**
     * Retrieves all partner universities.
     *
     * @return a list containing all partner universities.
     */
    public List<PartnerUniversity> getAllPartnerUniversities() {
        try {
            ResponseEntity<PartnerUniversityCollectionModel> response = restTemplate.exchange(
                    DISPATCHER_URL + "/universities",
                    HttpMethod.GET,
                    null,
                    PartnerUniversityCollectionModel.class
            );
            PartnerUniversityCollectionModel collectionModel = response.getBody();
            if (collectionModel != null && collectionModel.getEmbedded() != null) {
                return collectionModel.getEmbedded().getPartnerUniversityList().stream()
                        .filter(Objects::nonNull) // Filter out null values
                        .map(entityModel -> entityModel.getContent()) // Get content from EntityModel
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.err.println("Error fetching universities: " + ex.getMessage());
            return new ArrayList<>();
        }
    }


    /**
     * Retrieves a partner university by its ID.
     *
     * @param id the ID of the partner university to retrieve
     * @return the PartnerUniversity object corresponding to the ID
     */
    public PartnerUniversity getPartnerUniversityById(Long id) {
        ResponseEntity<PartnerUniversity> response = restTemplate.getForEntity(
                DISPATCHER_URL + "/universities/{id}", PartnerUniversity.class, id
        );
        return response.getBody();
    }


    /**
     * Updates an existing partner university.
     *
     * @param id                the ID of the partner university to update
     * @param partnerUniversity the updated PartnerUniversity object containing new information
     */
    public void updatePartnerUniversity(Long id, PartnerUniversity partnerUniversity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PartnerUniversity> request = new HttpEntity<>(partnerUniversity, headers);

        restTemplate.put(DISPATCHER_URL + "/universities/{id}/update", request, id);
    }

    /**
     * Deletes an existing partner university.
     *
     * @param id the ID of the partner university to delete
     */
    public void deletePartnerUniversity(Long id) {
        restTemplate.delete(DISPATCHER_URL + "/universities/{id}/delete", id);
    }
}