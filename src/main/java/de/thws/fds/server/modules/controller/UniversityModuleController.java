package de.thws.fds.server.modules.controller;

import de.thws.fds.server.modules.model.Module;
import de.thws.fds.server.modules.service.ModuleServiceImpl;
import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import de.thws.fds.server.partner_universities.service.PartnerUniversityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller for managing modules of a partner university.
 * This controller provides endpoints to create, read, update, delete, and filter modules of a university.
 * Controller class also contains HATEOAS by providing links of further possible states.
 */
@RestController
@RequestMapping("/api/v1/universities/{universityId}/modules")
public class UniversityModuleController {

    private final PartnerUniversityServiceImpl universityService;
    private final ModuleServiceImpl moduleServiceImpl;

    /**
     * Constructor for UniversityModuleController.
     *
     * @param universityService Service for managing partner universities.
     * @param moduleServiceImpl Service for managing modules.
     */

    @Autowired
    public UniversityModuleController(PartnerUniversityServiceImpl universityService, ModuleServiceImpl moduleServiceImpl) {
        this.universityService = universityService;
        this.moduleServiceImpl = moduleServiceImpl;
    }

    /**
     * Get all modules of a university by using the identifier of a specific university.
     *
     * @param universityId The ID of the university.
     * @param pageNo       The page number for pagination.
     * @param pageSize     The page size for pagination.
     * @return A paginated list of modules with related links: getSingle, postModule, filterModules.
     */
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Module>>> getAllModules(
            @PathVariable Long universityId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {

        Page<Module> modulesPage = moduleServiceImpl.getAllModulesOfUniversity(universityId, pageNo, pageSize);

        // Convert Page<Module> to List<EntityModel<Module>> with self links
        List<EntityModel<Module>> moduleModels = modulesPage.getContent().stream()
                .map(module -> EntityModel.of(module,
                        // Add self-link for each module -->"getSingleUni"
                        linkTo(methodOn(UniversityModuleController.class).getModuleById(universityId, module.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());


        PagedModel<EntityModel<Module>> pagedModel = PagedModel.of(moduleModels,
                new PagedModel.PageMetadata(modulesPage.getSize(), modulesPage.getNumber(), modulesPage.getTotalElements(), modulesPage.getTotalPages())
        );

        //link to post new module of a Uni
        Link postLink = linkTo(methodOn(UniversityModuleController.class).addModule(universityId, null))
                .withRel("add-module")
                .withType("POST");
        //link to filter modules of a Uni
        Link filterLink = linkTo(methodOn(UniversityModuleController.class).filterModules(null, null, null, 0, 10))
                .withRel("filter-modules")
                .withType("GET");
        pagedModel.add(postLink, filterLink);

        return ResponseEntity.ok(pagedModel);
    }

    /**
     * Filter modules of universities based on given criteria.
     *
     * @param name         The name of the module.
     * @param semester     The semester of the module.
     * @param creditPoints The credit points of the module.
     * @param pageNo       The page number for pagination.
     * @param pageSize     The page size for pagination.
     * @return A paginated list of filtered modules with direct link to the module that was found via filtering.
     */
    @GetMapping("/filter")
    public ResponseEntity<PagedModel<EntityModel<Module>>> filterModules(
            @RequestParam Optional<String> name,
            @RequestParam Optional<Integer> semester,
            @RequestParam Optional<Integer> creditPoints,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {

        Page<Module> filteredPage = moduleServiceImpl.filterModulesOfUnis(name, semester, creditPoints, pageNo, pageSize);

        List<EntityModel<Module>> moduleModels = filteredPage.getContent().stream()
                .map(module -> {
                    Long universityId = module.getPartnerUniversity().getId();
                    return EntityModel.of(module,
                            linkTo(methodOn(UniversityModuleController.class).getModuleById(universityId, module.getId())).withSelfRel()
                    );
                })
                .collect(Collectors.toList());

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                filteredPage.getSize(),
                filteredPage.getNumber(),
                filteredPage.getTotalElements(),
                filteredPage.getTotalPages()
        );

        PagedModel<EntityModel<Module>> pagedModel = PagedModel.of(moduleModels, pageMetadata);

        return ResponseEntity.ok(pagedModel);
    }

    /**
     * Get a specific module by its ID and university ID (as module exists only just with a university).
     *
     * @param universityId The ID of the university.
     * @param moduleId     The ID of the module.
     * @return The module with related links: putModule, deleteModule, getAllModules.
     */
    @GetMapping("/{moduleId}")
    public ResponseEntity<EntityModel<Module>> getModuleById(@PathVariable Long universityId, @PathVariable Long moduleId) {
        Optional<PartnerUniversity> universityOptional = universityService.getUniversityById(universityId);
        if (universityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Module module = moduleServiceImpl.getModuleByIdAndUniversityId(universityId, moduleId);
        EntityModel<Module> moduleModel = EntityModel.of(module);

        // link to update module of a Uni
        moduleModel.add(
                linkTo(methodOn(UniversityModuleController.class).updateModule(universityId, moduleId, null))
                        .withRel("update")
                        .withType("PUT")
        );

        // link to delete module of a Uni
        moduleModel.add(
                linkTo(methodOn(UniversityModuleController.class).deleteModule(universityId, moduleId))
                        .withRel("delete")
                        .withType("DELETE")
        );

        //link to get all modules of a Uni
        moduleModel.add(
                linkTo(methodOn(UniversityModuleController.class).getAllModules(universityId, 0, 10))
                        .withRel("all-modules")
        );

        return ResponseEntity.ok(moduleModel);
    }

    /**
     * Add a new module to an existing university.
     *
     * @param universityId The ID of the university.
     * @param module       The module that has to be added.
     * @return The created module with link to getAllModules.
     */

    @PostMapping("/create")
    public ResponseEntity<EntityModel<Module>> addModule(@PathVariable Long universityId, @RequestBody Module module) {
        Optional<PartnerUniversity> universityOptional = universityService.getUniversityById(universityId);
        if (universityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            PartnerUniversity university = universityOptional.get();
            module.setPartnerUniversity(university);
            Module savedModule = moduleServiceImpl.createModuleOfUni(universityId, module);

            //link to get all modules of a Uni
            Link getAllModulesLink = linkTo(methodOn(UniversityModuleController.class).getAllModules(universityId, 0, 10))
                    .withRel("all-modules").withType("GET");

            EntityModel<Module> moduleModel = EntityModel.of(savedModule, getAllModulesLink);

            return ResponseEntity.ok(moduleModel);
        }
    }

    /**
     * Update an existing module of the respective university.
     *
     * @param universityId  The ID of the university.
     * @param moduleId      The ID of the module to be updated.
     * @param moduleDetails The module which has to be updated.
     * @return The updated module with redirection to the module itself, to view updated module: getSingleModule.
     */

    @PutMapping("/{moduleId}/update")
    public ResponseEntity<EntityModel<Module>> updateModule(@PathVariable Long universityId, @PathVariable Long moduleId, @RequestBody Module moduleDetails) {
        Optional<PartnerUniversity> universityOptional = universityService.getUniversityById(universityId);
        if (universityOptional.isPresent()) {
            PartnerUniversity university = universityOptional.get();
            Optional<Module> moduleOptional = university.getModules().stream()
                    .filter(module -> module.getId().equals(moduleId))
                    .findFirst();
            if (moduleOptional.isPresent()) {
                Module module = moduleOptional.get();
                module.setName(moduleDetails.getName());
                module.setSemester(moduleDetails.getSemester());
                module.setCreditPoints(moduleDetails.getCreditPoints());
                Module updatedModule = moduleServiceImpl.updateModuleOfUni(module);

                //link to get module by the id after updating an existing module of a Uni
                Link getModuleLink = linkTo(methodOn(UniversityModuleController.class).getModuleById(universityId, moduleId))
                        .withRel("module").withType("GET");

                EntityModel<Module> moduleModel = EntityModel.of(updatedModule, getModuleLink);

                return ResponseEntity.ok(moduleModel);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete a module from a university.
     *
     * @param universityId The ID of the university.
     * @param moduleId     The ID of the module to be deleted.
     * @return A response indicating the result of the delete operation without any further link.
     */

    //No TransitionLink for Delete
    @DeleteMapping("/{moduleId}/delete")
    public ResponseEntity<Void> deleteModule(@PathVariable Long universityId, @PathVariable Long moduleId) {
        Optional<PartnerUniversity> universityOptional = universityService.getUniversityById(universityId);
        if (universityOptional.isPresent()) {
            Optional<Module> moduleOptional = moduleServiceImpl.getModuleById(moduleId);
            if (moduleOptional.isPresent()) {
                Module module = moduleOptional.get();
                if (module.getPartnerUniversity().getId().equals(universityId)) {
                    moduleServiceImpl.deleteModuleOfUni(moduleId);
                    return ResponseEntity.noContent().build();
                }
            }
        }
        return ResponseEntity.notFound().build();
    }
}
