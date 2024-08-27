package com.example.request;

import com.example.model.MenuItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MenuItemDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;

    public MenuItemDTO(MenuItem menuItem) {
        this.id = menuItem.getId();
        this.name = menuItem.getName();
        this.description = menuItem.getDescription();
        this.price = menuItem.getPrice();
        this.imageUrl = menuItem.getImage_url();
    }

    // getters and setters
}
