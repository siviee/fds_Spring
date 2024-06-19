package de.thws.fds.server.partner_universities.controller;

import de.thws.fds.server.modules.controller.UniversityModuleController;
import de.thws.fds.server.partner_universities.service.PartnerUniversityServiceImpl;
import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller for managing partner universities.
 * This controller provides endpoints to create, read, update, delete, and filter universities.
 * Controller class also contains HATEOAS by providing links of further possible states.
 */
@RestController
@RequestMapping("/api/v1/universities")
public class PartnerUniversityController {


    private final PartnerUniversityServiceImpl service;

    /**
     * Constructor for PartnerUniversityController.
     *
     * @param service Service for managing partner universities.
     */
    @Autowired
    public PartnerUniversityController(PartnerUniversityServiceImpl service) {
        this.service = service;

    }


    /**
     * Retrieves all partner universities with the help of pagination.
     *
     * @param pageNo   the page number
     * @param pageSize the page size
     * @return a collection of partner universities with pagination with further links: getSingle (self-link), postUni, filterUni
     */
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PartnerUniversity>>> getAllUniversities(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        Page<PartnerUniversity> universities = service.getAllUniversities(pageNo, pageSize);

        // Convert Page<PartnerUniversity> to List<EntityModel<PartnerUniversity>>
        List<EntityModel<PartnerUniversity>> universityModels = universities.getContent().stream()
                .map(university -> EntityModel.of(university,
                        // Add self-link for each university
                        linkTo(methodOn(PartnerUniversityController.class).getUniversityById(university.getId()))
                                .withSelfRel()
                ))
                .collect(Collectors.toList());

        //link to post a new Uni
        Link postLink = linkTo(methodOn(PartnerUniversityController.class).addUniversity(null))
                .withRel("add-university")
                .withType("POST");
        //link to filter the Unis
        Link filterLink = linkTo(methodOn(PartnerUniversityController.class).filterUniversities(null, null, null, null, null, 0, 10))
                .withRel("filter-universities")
                .withType("GET");

        CollectionModel<EntityModel<PartnerUniversity>> collectionModel = CollectionModel.of(universityModels, postLink, filterLink);

        return ResponseEntity.ok(collectionModel);
    }


    /**
     * Filters universities based on various criteria.
     *
     * @param country       of the university
     * @param name          of the university
     * @param spring        of the university
     * @param autumn        of the university
     * @param contactPerson of the university
     * @param pageNo        the page number
     * @param pageSize      the page size
     * @return a paginated list of filtered universities with further link to the partner university that was found via filtering.
     */
    @GetMapping("/filter")
    public ResponseEntity<PagedModel<EntityModel<PartnerUniversity>>> filterUniversities(
            @RequestParam Optional<String> country,
            @RequestParam Optional<String> name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> spring,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> autumn,
            @RequestParam Optional<String> contactPerson,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {

        Page<PartnerUniversity> filteredPage = service.filterUniversities(country, name, spring, autumn, contactPerson, pageNo, pageSize);

        List<EntityModel<PartnerUniversity>> universityModels = filteredPage.getContent().stream()
                .map(university -> EntityModel.of(university,
                        linkTo(methodOn(PartnerUniversityController.class).getUniversityById(university.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                filteredPage.getSize(),
                filteredPage.getNumber(),
                filteredPage.getTotalElements(),
                filteredPage.getTotalPages()
        );

        PagedModel<EntityModel<PartnerUniversity>> pagedModel = PagedModel.of(universityModels, pageMetadata);

        return ResponseEntity.ok(pagedModel);
    }


    /**
     * Retrieves a specific partner university by its ID.
     *
     * @param id the university ID
     * @return the partner university with the given ID and further links for updateUni, deleteUni, getAllUnis, getAllModulesOfUni,
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PartnerUniversity>> getUniversityById(@PathVariable Long id) {
        Optional<PartnerUniversity> university = service.getUniversityById(id);

        return university.map(u -> {
            EntityModel<PartnerUniversity> universityModel = EntityModel.of(u);

            //link to update a Uni
            universityModel.add(
                    linkTo(methodOn(PartnerUniversityController.class).updateUniversity(id, u))
                            .withRel("update")
                            .withType("PUT")
            );

            //link to delete a Uni
            universityModel.add(
                    linkTo(methodOn(PartnerUniversityController.class).deleteUniversity(id))
                            .withRel("delete")
                            .withType("DELETE")
            );


            //link to get all Unis
            universityModel.add(
                    linkTo(methodOn(PartnerUniversityController.class).getAllUniversities(0, 10))
                            .withRel("all-universities")
            );

            //link to get all modules for this Uni
            universityModel.add(
                    linkTo(methodOn(UniversityModuleController.class).getAllModules(id, 0, 10))
                            .withRel("all-modules").withType("GET")
            );

            return ResponseEntity.ok(universityModel);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * Creates a new partner university.
     *
     * @param university the university to create
     * @return the created university with further link to getAllUniversities
     */
    @PostMapping("/create")
    public ResponseEntity<EntityModel<PartnerUniversity>> addUniversity(@RequestBody PartnerUniversity university) {
        PartnerUniversity createdUniversity = service.createUniversity(university);

        EntityModel<PartnerUniversity> universityModel = EntityModel.of(createdUniversity);

        //link to get all Unis
        universityModel.add(
                linkTo(methodOn(PartnerUniversityController.class).getAllUniversities(0, 10))
                        .withRel("all-universities")
        );
        return ResponseEntity.created(linkTo(PartnerUniversityController.class).slash(createdUniversity.getId()).toUri())
                .body(universityModel);
    }


    /**
     * Updates an existing partner university.
     *
     * @param id         the university ID
     * @param university the updated university details
     * @return the updated university with further link to view the updated partner university: getSingleUni
     */
    @PutMapping("/{id}/update")
    public ResponseEntity<EntityModel<PartnerUniversity>> updateUniversity(@PathVariable Long id, @RequestBody PartnerUniversity university) {
        Optional<PartnerUniversity> existingUniversity = service.getUniversityById(id);
        if (existingUniversity.isPresent()) {
            university.setId(id);
            PartnerUniversity updatedUniversity = service.updateUniversity(university);

            EntityModel<PartnerUniversity> universityModel = EntityModel.of(updatedUniversity);

            //link to get all Unis
            universityModel.add(
                    linkTo(methodOn(PartnerUniversityController.class).getUniversityById(id))
                            .withRel("university").withType("GET")
            );

            return ResponseEntity.ok(universityModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Deletes a partner university.
     *
     * @param id the university ID
     * @return a response entity indicating the result delete operation without any further link.
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        if (service.getUniversityById(id).isPresent()) {
            service.deleteUniversity(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
