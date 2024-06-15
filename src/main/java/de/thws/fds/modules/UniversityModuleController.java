package de.thws.fds.modules;

import de.thws.fds.partner_universities.PartnerUniversity;
import de.thws.fds.partner_universities.PartnerUniversityServiceImpl;
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

@RestController
@RequestMapping("/api/v1/universities/{universityId}/modules")
public class UniversityModuleController {

    private final PartnerUniversityServiceImpl universityService;
    private final ModuleServiceImpl moduleServiceImpl;


    @Autowired
    public UniversityModuleController(PartnerUniversityServiceImpl universityService, ModuleServiceImpl moduleServiceImpl) {
        this.universityService = universityService;
        this.moduleServiceImpl = moduleServiceImpl;
    }

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
