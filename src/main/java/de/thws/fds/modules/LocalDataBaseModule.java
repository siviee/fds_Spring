package de.thws.fds.modules;

import de.thws.fds.partner_universities.PartnerUniversity;
import de.thws.fds.partner_universities.PartnerUniversityRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

//@Component
public class LocalDataBaseModule {

//    private final ModuleRepo moduleRepository;
//    private final PartnerUniversityRepo universityRepository;
//
//    @Autowired
//    public LocalDataBaseModule(ModuleRepo moduleRepository, PartnerUniversityRepo universityRepository) {
//        this.moduleRepository = moduleRepository;
//        this.universityRepository = universityRepository;
//    }
//
//    @PostConstruct
//    @Order (2)
//    public void load() {
//        Optional<PartnerUniversity> christUniversityOptional = universityRepository.findById(1L);
//        if (christUniversityOptional.isPresent()) {
//            PartnerUniversity christUniversity = christUniversityOptional.get();
//            Module module1 = new Module("Algorithm and Data Structure 1", 1, 5,christUniversity);
//            Module module2 = new Module("Analysis", 2, 4,christUniversity);
//
//            //module2.setPartnerUniversity(christUniversity);
//            //module1.setPartnerUniversity(christUniversity);
//            moduleRepository.saveAll(List.of(module1, module2));
//        }
//        Optional<PartnerUniversity> harvardUniversityOptional = universityRepository.findById(2L);
//        if (harvardUniversityOptional.isPresent()) {
//            PartnerUniversity harvardUniversity = harvardUniversityOptional.get();
//            Module module3 = new Module("Data Science", 3, 10,harvardUniversity);
//            Module module4 = new Module("Foundation of Distributed Systems", 4, 20,harvardUniversity);
//
//           // module3.setPartnerUniversity(harvardUniversity);
//            //module4.setPartnerUniversity(harvardUniversity);
//            moduleRepository.saveAll(List.of(module3, module4));
//        }
//    }
}