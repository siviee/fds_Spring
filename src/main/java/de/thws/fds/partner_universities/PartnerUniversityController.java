package de.thws.fds.partner_universities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/universities")
public class PartnerUniversityController {


    private final PartnerUniversityServiceImpl service;

    @Autowired
    public PartnerUniversityController(PartnerUniversityServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public Page<PartnerUniversity> getAllUniversities(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return service.getAllUniversities(pageNo, pageSize);
    }

    @GetMapping("/filter")
    public Page<PartnerUniversity> filterUniversities(
            @RequestParam Optional<String> country,
            @RequestParam Optional<String> name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> spring,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> autumn,
            @RequestParam Optional<String> contactPerson,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return service.filterUniversities(country, name, spring, autumn, contactPerson, pageNo, pageSize);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PartnerUniversity> getUniversityById(@PathVariable Long id) {
        Optional<PartnerUniversity> university = service.getUniversityById(id);
        return university.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public PartnerUniversity addUniversity(@RequestBody PartnerUniversity university) {
        return service.createUniversity(university);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnerUniversity> updateUniversity(@PathVariable Long id, @RequestBody PartnerUniversity university) {
        if (service.getUniversityById(id).isPresent()) {
            university.setId(id);
            return ResponseEntity.ok(service.updateUniversity(university));
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
