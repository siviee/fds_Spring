package de.thws.fds.partner_universities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/universities")
public class PartnerUniversityController {


    private final PartnerUniversityServiceImpl service;
    private final PartnerUniversityAssembler universityAssembler;

    @Autowired
    public PartnerUniversityController(PartnerUniversityServiceImpl service, PartnerUniversityAssembler universityAssembler) {
        this.service = service;
        this.universityAssembler = universityAssembler;
    }

    @GetMapping
    public Page<EntityModel<PartnerUniversity>> getAllUniversities(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        Page<PartnerUniversity> universities = service.getAllUniversities(pageNo, pageSize);
        return universities.map(universityAssembler::toModel);
    }

    @GetMapping("/filter")
    public Page<EntityModel<PartnerUniversity>> filterUniversities(
            @RequestParam Optional<String> country,
            @RequestParam Optional<String> name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> spring,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> autumn,
            @RequestParam Optional<String> contactPerson,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<PartnerUniversity> filteredUniversities = service.filterUniversities(country, name, spring, autumn, contactPerson, pageNo, pageSize);
        return filteredUniversities.map(universityAssembler::toModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PartnerUniversity>> getUniversityById(@PathVariable Long id) {
        Optional<PartnerUniversity> university = service.getUniversityById(id);
        return university.map(universityAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public PartnerUniversity addUniversity(@RequestBody PartnerUniversity university) {
        return service.createUniversity(university);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PartnerUniversity>> updateUniversity(@PathVariable Long id, @RequestBody PartnerUniversity university) {
        if (service.getUniversityById(id).isPresent()) {
            university.setId(id);
            return ResponseEntity.ok(universityAssembler.toModel(service.updateUniversity(university)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        if (service.getUniversityById(id).isPresent()) {
            service.deleteUniversity(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
