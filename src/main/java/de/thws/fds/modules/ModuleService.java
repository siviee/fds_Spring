package de.thws.fds.modules;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ModuleService {
//    Page<Module> getAllModulesOfUnis(int pageNo, int pageSize);

    Optional<Module> getModuleById(Long id);

    Module createModuleOfUni(Long universityId, Module module);

    Module updateModuleOfUni(Module updatedModule);

    void deleteModuleOfUni(Long id);

    Page<Module> getAllModulesOfUniversity(Long universityId, int pageNo, int pageSize);

    Module getModuleByIdAndUniversityId(Long universityId, Long moduleId);

    Page<Module> filterModulesOfUnis(Optional<String> name, Optional<Integer> semester, Optional<Integer> creditPoints, int pageNo, int pageSize);
}
