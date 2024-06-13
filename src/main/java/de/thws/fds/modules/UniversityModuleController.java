package de.thws.fds.modules;

import de.thws.fds.partner_universities.PartnerUniversity;
import de.thws.fds.partner_universities.PartnerUniversityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/universities/{universityId}/modules")
public class UniversityModuleController {

    private final PartnerUniversityServiceImpl universityService;
    private final ModuleServiceImpl moduleServiceImpl;
    private final ModuleAssembler moduleAssembler;

    @Autowired
    public UniversityModuleController(PartnerUniversityServiceImpl universityService, ModuleServiceImpl moduleServiceImpl, ModuleAssembler moduleAssembler) {
        this.universityService = universityService;
        this.moduleServiceImpl = moduleServiceImpl;
        this.moduleAssembler = moduleAssembler;
    }


//@GetMapping
//public ResponseEntity<Page<Module>> getAllModules(
//        @PathVariable Long universityId,
//        @RequestParam(defaultValue = "0") int pageNo,
//        @RequestParam(defaultValue = "10") int pageSize) {
////getAllModulesofUniID brauchen wir hier!
//    Page<Module> modulesPage = moduleServiceImpl.getAllModulesOfUnis(pageNo, pageSize);
//    return ResponseEntity.ok(modulesPage);
//}
@GetMapping
public ResponseEntity<PagedModel<EntityModel<Module>>> getAllModules(
        @PathVariable Long universityId,
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "10") int pageSize) {

    Page<Module> modulesPage = moduleServiceImpl.getAllModulesOfUniversity(universityId, pageNo, pageSize);
    PagedModel<EntityModel<Module>> entityModels = moduleAssembler.toPagedModel(modulesPage, universityId);
    return ResponseEntity.ok(entityModels);
}

    @GetMapping("/filter")
    public Page<Module> filterModules(
            @RequestParam Optional<String> name,
            @RequestParam Optional<Integer> semester,
            @RequestParam Optional<Integer>creditPoints,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize)
    {
        return moduleServiceImpl.filterModulesOfUnis(name, semester, creditPoints,pageNo,pageSize);
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long universityId, @PathVariable Long moduleId) {
        Optional<PartnerUniversity> universityOptional = universityService.getUniversityById(universityId);
        if (universityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Module module = moduleServiceImpl.getModuleByIdAndUniversityId(universityId, moduleId);
        return ResponseEntity.ok(module);

    }


    @PostMapping
    public ResponseEntity<Module> addModule(@PathVariable Long universityId, @RequestBody Module module) {
        Optional<PartnerUniversity> universityOptional = universityService.getUniversityById(universityId);
        if (universityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            PartnerUniversity university = universityOptional.get();
            module.setPartnerUniversity(university);
            Module savedModule = moduleServiceImpl.createModuleOfUni(universityId, module);
            university.setModules(module);
            return ResponseEntity.ok(savedModule);

        }
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<Module> updateModule(@PathVariable Long universityId, @PathVariable Long moduleId, @RequestBody Module moduleDetails) {
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
                return ResponseEntity.ok(updatedModule);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{moduleId}")
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
