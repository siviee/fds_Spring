package de.thws.fds.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.thws.fds.server.partner_universities.model.PartnerUniversity;
import org.springframework.hateoas.EntityModel;

import java.util.List;

/**
 * wrapper class to handle the HATEOAS CollectionModel in PartnerUniversityController
 */

public class PartnerUniversityCollectionModel {

    @JsonProperty("_embedded")
    private Embedded embedded;

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

    public static class Embedded {
        @JsonProperty("partnerUniversityList")
        private List<EntityModel<PartnerUniversity>> partnerUniversityList;

        public List<EntityModel<PartnerUniversity>> getPartnerUniversityList() {
            return partnerUniversityList;
        }

        public void setPartnerUniversityList(List<EntityModel<PartnerUniversity>> partnerUniversityList) {
            this.partnerUniversityList = partnerUniversityList;
        }
    }
}
