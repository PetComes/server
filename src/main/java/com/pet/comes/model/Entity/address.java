package com.pet.comes.model.Entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "address")
public class address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String buildingName;

    @Column(columnDefinition = "TEXT")
    private String x;

    @Column(columnDefinition = "TEXT")
    private String y;

    @Column(columnDefinition = "TEXT")
    private String detail;



}
