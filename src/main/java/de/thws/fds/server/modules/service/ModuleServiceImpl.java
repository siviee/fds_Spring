package de.thws.fds.server.modules.service;

import de.thws.fds.server.modules.model.Module;
import de.thws.fds.server.modules.repository.ModuleRepo;
import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import de.thws.fds.server.partner_universities.repository.PartnerUniversityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepo moduleRepo;
    private final PartnerUniversityRepo uniRepo;


    @Autowired
    public ModuleServiceImpl(ModuleRepo moduleRepo, PartnerUniversityRepo uniRepo) {
        this.moduleRepo = moduleRepo;
        this.uniRepo = uniRepo;
    }

    @Override
    public Optional<Module> getModuleById(Long id) {
        return moduleRepo.findById(id);
    }

    @Override
    public Module createModuleOfUni(Long universityId, Module module) {
        Optional<PartnerUniversity> uniOptional = uniRepo.findById(universityId);
        if (uniOptional.isEmpty()) {
            return null;
        }
        PartnerUniversity uni = uniOptional.get();
        module.setPartnerUniversity(uni);
        return moduleRepo.save(module);
    }

    @Override
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

    @Override
    public void deleteModuleOfUni(Long id) {
        moduleRepo.deleteById(id);
    }

    @Override
    public Page<Module> getAllModulesOfUniversity(Long universityId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return moduleRepo.findByPartnerUniversityId(universityId, pageable);
    }

    public Module getModuleByIdAndUniversityId(Long universityId, Long moduleId) {
        return moduleRepo.findByPartnerUniversityIdAndId(universityId, moduleId);
    }

    //Filtering
    @Override
    public Page<Module> filterModulesOfUnis(Optional<String> name, Optional<Integer> semester, Optional<Integer> creditPoints, int pageNo,
                                            int pageSize) {
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
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return moduleRepo.findAll(spec, pageable);
    }
}


