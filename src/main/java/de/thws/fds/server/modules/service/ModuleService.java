package de.thws.fds.server.modules.service;

import de.thws.fds.server.modules.model.Module;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Service interface for managing {@link Module} entities.
 * Provides methods for CRUD operations and a filtering method with few querries.
 */

public interface ModuleService {

    /**
     * Retrieves a module by its ID.
     *
     * @param id the ID of the module
     * @return an Optional containing the module if found, or an empty Optional if not found
     */
    Optional<Module> getModuleById(Long id);

    /**
     * Creates a new module for a given university.
     *
     * @param universityId the ID of the university
     * @param module       the module to be created
     * @return the created module
     */
    Module createModuleOfUni(Long universityId, Module module);

    /**
     * Updates an existing module.
     *
     * @param updatedModule the module with updated information
     * @return the updated module
     */
    Module updateModuleOfUni(Module updatedModule);

    /**
     * Deletes a module by its ID.
     *
     * @param id the ID of the module to be deleted
     */
    void deleteModuleOfUni(Long id);

    /**
     * Retrieves all modules of a given university with the use of Pagination.
     *
     * @param universityId the ID of the university
     * @param pageNo       the page number for pagination
     * @param pageSize     the page size for pagination
     * @return a page of modules
     */
    Page<Module> getAllModulesOfUniversity(Long universityId, int pageNo, int pageSize);

    /**
     * Retrieves a module by its ID and the university ID.
     *
     * @param universityId the ID of the university
     * @param moduleId     the ID of the module
     * @return the module if found
     */
    Module getModuleByIdAndUniversityId(Long universityId, Long moduleId);

    /**
     * Filters modules based on optional criteria.
     *
     * @param name         of a module
     * @param semester     of a module
     * @param creditPoints of a module
     * @param pageNo       the page number for pagination
     * @param pageSize     the page size for pagination
     * @return a page of filtered modules
     */
    Page<Module> filterModulesOfUnis(Optional<String> name, Optional<Integer> semester, Optional<Integer> creditPoints, int pageNo, int pageSize);
}
