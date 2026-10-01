package com.hospitality.platform.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "amenities")
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String icon;
    private String category;

    public Amenity() {}

    public Amenity(String name, String category, String icon) {
        this.name = name;
        this.category = category;
        this.icon = icon;
    }

    public Amenity(Long id, String name, String icon, String category) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.category = category;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
