package de.thws.fds.modules;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.thws.fds.partner_universities.PartnerUniversity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;


@Entity
@Table
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int semester;
    private int creditPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uni-id", nullable = false)
    @JsonBackReference
    private PartnerUniversity partnerUniversity;


    // Constructors
    public Module() {
    }

    //PartnerUni only in one constructor with id --> to map it properly
    public Module(Long id, String name, int semester, int creditPoints, PartnerUniversity partnerUniversity) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.creditPoints = creditPoints;
        this.partnerUniversity = partnerUniversity;
    }

    public Module(String name, int semester, int creditPoints, PartnerUniversity partnerUniversity) {
        this.name = name;
        this.semester = semester;
        this.creditPoints = creditPoints;
        this.partnerUniversity = partnerUniversity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(int creditPoints) {
        this.creditPoints = creditPoints;
    }

    public PartnerUniversity getPartnerUniversity() {
        return partnerUniversity;
    }

    public void setPartnerUniversity(PartnerUniversity partnerUniversity) {
        this.partnerUniversity = partnerUniversity;
    }
}
