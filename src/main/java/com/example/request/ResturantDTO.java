package com.example.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResturantDTO {
    private Long id;
    private String name;
    private String address;
    private String about;
    private String contact;
    private List<MenuItemDTO> menuItems;

    // getters and setters
}