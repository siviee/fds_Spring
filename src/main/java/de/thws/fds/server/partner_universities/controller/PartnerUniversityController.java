package de.thws.fds.server.partner_universities.controller;

import de.thws.fds.server.modules.controller.UniversityModuleController;
import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import de.thws.fds.server.partner_universities.service.PartnerUniversityServiceImpl;
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
     * @return a collection of partner universities with pagination with further links,
     * in response-body: getSingle (self-link), getAllModules, pagination-links (next/previous)
     * in response header: postUni, filterUni
     */
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PartnerUniversity>>> getAllUniversities(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        Page<PartnerUniversity> universitiesPage = service.getAllUniversities(pageNo, pageSize, sortDirection);

        List<EntityModel<PartnerUniversity>> universityModels = universitiesPage.getContent().stream()
                .map(university -> EntityModel.of(university,
                        linkTo(methodOn(PartnerUniversityController.class).getUniversityById(university.getId())).withSelfRel(),
                        linkTo(methodOn(UniversityModuleController.class).getAllModules(university.getId(), 0, 10)).withRel("all-modules").withType("GET")
                ))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<PartnerUniversity>> collectionModel = CollectionModel.of(universityModels);

        // Pagination links
        if (universitiesPage.hasPrevious()) {
            Link previousLink = linkTo(methodOn(PartnerUniversityController.class).getAllUniversities(pageNo - 1, pageSize, sortDirection)).withRel("previous").withType("GET");
            collectionModel.add(previousLink);
        }
        if (universitiesPage.hasNext()) {
            Link nextLink = linkTo(methodOn(PartnerUniversityController.class).getAllUniversities(pageNo + 1, pageSize, sortDirection)).withRel("next").withType("GET");
            collectionModel.add(nextLink);
        }

        // Header links
        Link postLink = linkTo(methodOn(PartnerUniversityController.class).addUniversity(null)).withRel("add-university").withType("POST");
        Link filterLink = linkTo(methodOn(PartnerUniversityController.class).filterUniversities(null, null, null, null, null, 0, 10)).withRel("filter-universities").withType("GET");


        return ResponseEntity.ok()
                .header("Link", postLink.getHref() + "; rel=\"add-university\"")
                .header("Link", filterLink.getHref() + "; rel=\"filter-universities\"")
                .body(collectionModel);
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
     * @return a paginated list of filtered universities with further link (self-link) to the partner university that was found via filtering.
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
     * @return the partner university with the given ID and further links in response header for updateUni, deleteUni, getAllUnis,
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PartnerUniversity>> getUniversityById(@PathVariable Long id) {
        Optional<PartnerUniversity> university = service.getUniversityById(id);

        return university.map(u -> {
            EntityModel<PartnerUniversity> universityModel = EntityModel.of(u,
                    linkTo(methodOn(PartnerUniversityController.class).getUniversityById(id)).withSelfRel()
            );

            return ResponseEntity.ok()
                    .header("Link", linkTo(methodOn(PartnerUniversityController.class).updateUniversity(id, u)).withRel("update").withType("PUT").getHref() + "; rel=\"update\"")
                    .header("Link", linkTo(methodOn(PartnerUniversityController.class).deleteUniversity(id)).withRel("delete").withType("DELETE").getHref() + "; rel=\"delete\"")
                    .header("Link", linkTo(methodOn(UniversityModuleController.class).getAllModules(id, 0, 10)).withRel("all-modules").withType("GET").getHref() + "; rel=\"all-modules\"")
                    .body(universityModel);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new partner university.
     *
     * @param university the university to create
     * @return the created university with further link in response header to getAllUniversities
     */
    @PostMapping("/create")
    public ResponseEntity<EntityModel<PartnerUniversity>> addUniversity(@RequestBody PartnerUniversity university) {
        PartnerUniversity createdUniversity = service.createUniversity(university);

        EntityModel<PartnerUniversity> universityModel = EntityModel.of(createdUniversity,
                linkTo(methodOn(PartnerUniversityController.class).getUniversityById(createdUniversity.getId())).withSelfRel()
        );

        return ResponseEntity.created(linkTo(PartnerUniversityController.class).slash(createdUniversity.getId()).toUri())
                .header("Link", linkTo(methodOn(PartnerUniversityController.class).getAllUniversities(0, 10,"asc")).withRel("all-universities").getHref() + "; rel=\"all-universities\"")
                .body(universityModel);
    }


    /**
     * Updates an existing partner university.
     *
     * @param id         the university ID
     * @param university the updated university details
     * @return the updated university with further link to view the updated partner university: getSingleUni (self-link)
     */
    @PutMapping("/{id}/update")
    public ResponseEntity<EntityModel<PartnerUniversity>> updateUniversity(@PathVariable Long id, @RequestBody PartnerUniversity university) {
        Optional<PartnerUniversity> existingUniversity = service.getUniversityById(id);
        if (existingUniversity.isPresent()) {
            university.setId(id);
            PartnerUniversity updatedUniversity = service.updateUniversity(university);

            EntityModel<PartnerUniversity> universityModel = EntityModel.of(updatedUniversity,
                    linkTo(methodOn(PartnerUniversityController.class).getUniversityById(id)).withSelfRel()
            );

            return ResponseEntity.ok()
                    .header("Link", linkTo(methodOn(PartnerUniversityController.class).getUniversityById(id)).withRel("university").getHref() + "; rel=\"university\"")
                    .body(universityModel);
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
