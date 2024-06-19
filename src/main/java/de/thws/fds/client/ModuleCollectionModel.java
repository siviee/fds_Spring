package de.thws.fds.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import de.thws.fds.server.modules.model.Module;

import java.util.List;

/**
 * wrapper class to handle the HATEOAS CollectionModel in UniversityModuleController
 */

public class ModuleCollectionModel extends RepresentationModel<ModuleCollectionModel> {
    @JsonProperty("_embedded")
    private EmbeddedModuleList _embedded;

    public EmbeddedModuleList getEmbedded() {
        return _embedded;
    }

    public void setEmbedded(EmbeddedModuleList _embedded) {
        this._embedded = _embedded;
    }

    public static class EmbeddedModuleList {
        private List<EntityModel<Module>> moduleList;

        public List<EntityModel<Module>> getModuleList() {
            return moduleList;
        }

        public void setModuleList(List<EntityModel<Module>> moduleList) {
            this.moduleList = moduleList;
        }
    }
}
