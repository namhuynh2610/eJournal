package com.bcd.ejournal.domain.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.bcd.ejournal.domain.enums.JournalReviewPolicy;
import com.bcd.ejournal.domain.enums.JournalStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Journal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer journalId;
    private String name;
    private String introduction;
    private String organization;
    private String issn;
    private Integer numberOfRound;
    private Integer numberOfReviewer;
    
    @Enumerated(EnumType.STRING)
    private JournalStatus status;
    private String slug;
    private Integer price;
    private String policy;
    @Enumerated(EnumType.STRING)
    private JournalReviewPolicy reviewPolicy;

    @ManyToMany
    @JoinTable(name = "JournalField", joinColumns = @JoinColumn(name = "JournalId", referencedColumnName = "JournalId"), inverseJoinColumns = @JoinColumn(name = "FieldId", referencedColumnName = "FieldId"))
    private List<Field> fields;

    @OneToOne(mappedBy = "journal")
    private Account manager;

    @OneToMany(mappedBy = "journal", cascade = CascadeType.MERGE)
    private List<Issue> issues;

    @OneToMany(mappedBy = "journal", cascade = CascadeType.MERGE)
    private List<Paper> papers;
    
    @OneToMany(mappedBy = "journal", cascade = CascadeType.MERGE)
    private List<Invoice> invoices;
}
