package de.thws.fds.server.modules.service;

import de.thws.fds.server.modules.model.Module;
import org.springframework.data.domain.Page;

import java.util.Optional;
/**
 * Service interface for managing {@link Module} entities.
 * Provides methods for CRUD operations and a filtering method with few querries.
 */

public interface ModuleService {

    Optional<Module> getModuleById(Long id);

    Module createModuleOfUni(Long universityId, Module module);

    Module updateModuleOfUni(Module updatedModule);

    void deleteModuleOfUni(Long id);

    Page<Module> getAllModulesOfUniversity(Long universityId, int pageNo, int pageSize);

    Module getModuleByIdAndUniversityId(Long universityId, Long moduleId);

    Page<Module> filterModulesOfUnis(Optional<String> name, Optional<Integer> semester, Optional<Integer> creditPoints, int pageNo, int pageSize);
}
