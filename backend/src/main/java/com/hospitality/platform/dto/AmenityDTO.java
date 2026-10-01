package com.hospitality.platform.dto;

import com.hospitality.platform.entity.Amenity;

public class AmenityDTO {
    private Long id;
    private String name;
    private String icon;
    private String category;

    public AmenityDTO() {}

    public AmenityDTO(Amenity amenity) {
        if (amenity != null) {
            this.id = amenity.getId();
            this.name = amenity.getName();
            this.icon = amenity.getIcon();
            this.category = amenity.getCategory();
        }
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
