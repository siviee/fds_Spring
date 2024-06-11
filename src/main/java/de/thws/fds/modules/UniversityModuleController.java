package de.thws.fds.modules;

import de.thws.fds.partner_universities.PartnerUniversity;
import de.thws.fds.partner_universities.PartnerUniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/universities/{universityId}/modules")
public class UniversityModuleController {

    private final PartnerUniversityService universityService;
    private final ModuleService moduleService;

    @Autowired
    public UniversityModuleController(PartnerUniversityService universityService, ModuleService moduleService) {
        this.universityService = universityService;
        this.moduleService = moduleService;
    }

//    @GetMapping
//    public ResponseEntity<List<Module>> getAllModules(@PathVariable Long universityId) {
//        Optional<PartnerUniversity> universityOptional = universityService.getUniversityById(universityId);
//        if (universityOptional.isPresent()) {
//            //PartnerUniversity university = universityOptional.get();
//            Module modules = moduleService.getAllModulesOfUniversity(universityId);
//            return ResponseEntity.ok(Collections.singletonList(modules));
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//
//    }
@GetMapping
public ResponseEntity<List<Module>> getAllModules(@PathVariable Long universityId) {
    Optional<PartnerUniversity> universityOptional = universityService.getUniversityById(universityId);
    if (universityOptional.isPresent()) {
        List<Module> modules = universityOptional.get().getModules();
        return ResponseEntity.ok(modules);
    } else {
        return ResponseEntity.notFound().build();
    }
}
    @GetMapping("/filter")
    public List<Module> filterModules(
            @RequestParam Optional<String> name,
            @RequestParam Optional<Integer> semester,
            @RequestParam Optional<Integer>creditPoints)
    {
        return moduleService.filterModulesOfUnis(name, semester, creditPoints);
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long universityId, @PathVariable Long moduleId) {
        Optional<PartnerUniversity> universityOptional = universityService.getUniversityById(universityId);
        if (universityOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Module module = moduleService.getModuleByIdAndUniversityId(universityId, moduleId);
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
            Module savedModule = moduleService.createModuleOfUni(universityId, module);
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
                Module updatedModule = moduleService.updateModuleOfUni(module);
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
            Optional<Module> moduleOptional = moduleService.getModuleById(moduleId);
            if (moduleOptional.isPresent()) {
                Module module = moduleOptional.get();
                if (module.getPartnerUniversity().getId().equals(universityId)) {
                    moduleService.deleteModuleOfUni(moduleId);
                    return ResponseEntity.noContent().build();
                }
            }
        }
        return ResponseEntity.notFound().build();
    }
}
