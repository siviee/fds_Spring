package de.thws.fds.partner_universities;

import de.thws.fds.modules.UniversityModuleController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class PartnerUniversityAssembler implements RepresentationModelAssembler<PartnerUniversity, EntityModel<PartnerUniversity>> {
    @Override
    public EntityModel<PartnerUniversity> toModel(PartnerUniversity university) {
        Long universityId = university.getId();

        EntityModel<PartnerUniversity> universityModel = EntityModel.of(university);

        // Add self-link for university
        universityModel.add(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PartnerUniversityController.class).getUniversityById(universityId))
                        .withSelfRel()
        );
         universityModel.add(
                 WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UniversityModuleController.class).getAllModules(universityId, 0, 10))
                         .withRel("module")
         );
        return universityModel;
    }

}
