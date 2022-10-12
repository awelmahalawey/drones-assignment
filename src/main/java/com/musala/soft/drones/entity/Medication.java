package com.musala.soft.drones.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "medication")
public class Medication extends BaseEntityUUID {

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "code", length = 255, nullable = false)
    private String code;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "image_url")
    private String imageUrl;
}
