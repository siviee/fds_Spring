package de.thws.fds.partner_universities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/universities")
public class PartnerUniversityController {

    @Autowired
    private PartnerUniversityService service;

    public PartnerUniversityController(PartnerUniversityService service) {
        this.service = service;
    }

    @GetMapping
    public List<PartnerUniversity> getAllUniversities() {
        return service.getAllUniversities();
    }

    @GetMapping("/filter")
    public List<PartnerUniversity> filterUniversities(
            @RequestParam Optional<String> country,
            @RequestParam Optional<String> name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> spring,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> autumn,
            @RequestParam Optional<String> contactPerson) {
        return service.filterUniversities(country, name, spring, autumn, contactPerson);
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
