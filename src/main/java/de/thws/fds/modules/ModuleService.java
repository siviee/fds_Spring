package de.thws.fds.modules;

import de.thws.fds.partner_universities.PartnerUniversity;
import de.thws.fds.partner_universities.PartnerUniversityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {
    private final ModuleRepo moduleRepo;
    private final PartnerUniversityRepo uniRepo;


    @Autowired
    public ModuleService(ModuleRepo moduleRepo, PartnerUniversityRepo uniRepo) {
        this.moduleRepo = moduleRepo;
        this.uniRepo = uniRepo;
    }

    public List<Module> getAllModulesOfUnis() {
        return moduleRepo.findAll();
    }

    public Optional<Module> getModuleById(Long id) {
        return moduleRepo.findById(id);
    }

    public Module createModuleOfUni(Long universityId, Module module) {
        Optional<PartnerUniversity> uniOptional = uniRepo.findById(universityId);
        if (uniOptional.isEmpty()) {
            return null;
        }
        PartnerUniversity uni = uniOptional.get();
        module.setPartnerUniversity(uni);
        return moduleRepo.save(module);
    }

    public Module updateModuleOfUni(Module updatedModule) {
        //find updatedModule by non-changeable ID
        Optional<Module> optionalModule = moduleRepo.findById(updatedModule.getId());
        if (optionalModule.isPresent()) {
            Module oldModule = optionalModule.get();
            //update Module
            oldModule.setName(updatedModule.getName());
            oldModule.setSemester(updatedModule.getSemester());
            oldModule.setCreditPoints(updatedModule.getCreditPoints());

            return moduleRepo.save(oldModule);
        } else {

            return new Module();

        }
    }

    public void deleteModuleOfUni(Long id) {
        moduleRepo.deleteById(id);
    }

    public Module getAllModulesOfUniversity(Long universityId) {
        return moduleRepo.findByPartnerUniversityId(universityId);
    }

    public Module getModuleByIdAndUniversityId(Long universityId, Long moduleId) {
        return moduleRepo.findByPartnerUniversityIdAndId(universityId, moduleId);
    }

    public List<Module> filterModulesOfUnis(Optional<String> name, Optional<Integer> semester, Optional<Integer> creditPoints) {
        Specification<Module> spec = Specification.where(null);

        if (name.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name.get()));
        }
        if (semester.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("semester"), semester.get()));
        }
        if (creditPoints.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("creditPoints"), creditPoints.get()));
        }
        return moduleRepo.findAll(spec);
    }
}


