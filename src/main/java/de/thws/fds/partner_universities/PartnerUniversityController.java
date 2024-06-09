package de.thws.fds.partner_universities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/universities")
public class PartnerUniversityController {

    @Autowired
    private PartnerUniversityService service;

    @GetMapping
    public List<PartnerUniversity> getAllUniversities() {
        return service.getAllUniversities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerUniversity> getUniversityById(@PathVariable Long id) {
        Optional<PartnerUniversity> university = service.getUniversityById(id);
        return university.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public PartnerUniversity addUniversity(@RequestBody PartnerUniversity university) {
        return service.addOrUpdateUniversity(university);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnerUniversity> updateUniversity(@PathVariable Long id, @RequestBody PartnerUniversity university) {
        if (service.getUniversityById(id).isPresent()) {
            university.setId(id);
            return ResponseEntity.ok(service.addOrUpdateUniversity(university));
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
