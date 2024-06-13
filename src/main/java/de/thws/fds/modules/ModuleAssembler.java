package de.thws.fds.modules;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModuleAssembler implements RepresentationModelAssembler<Module, EntityModel<Module>> {

    @Override
    public EntityModel<Module> toModel(Module module) {
        Long universityId = module.getPartnerUniversity().getId();
        Long moduleId = module.getId();

        EntityModel<Module> moduleModel = EntityModel.of(module);

        // Self link for module
        moduleModel.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UniversityModuleController.class).getModuleById(universityId, moduleId))
                        .withSelfRel()
        );


        return moduleModel;
    }

    public PagedModel<EntityModel<Module>> toPagedModel(Page<Module> modules, Long universityId) {
        List<EntityModel<Module>> moduleModels = modules.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return PagedModel.of(moduleModels, new PagedModel.PageMetadata(
                modules.getSize(), modules.getNumber(), modules.getTotalElements(), modules.getTotalPages()));
    }
}
