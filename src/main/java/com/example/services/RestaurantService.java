package com.example.services;

import com.example.model.MenuItem;
import com.example.model.Restaurant;
import com.example.repository.MenuItemRepository;
import com.example.repository.RestaurantRepository;
import com.example.request.MenuItemDTO;
import com.example.request.ResturantDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public List<ResturantDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(restaurant -> {
                    ResturantDTO dto = new ResturantDTO();
                    dto.setId(restaurant.getId());
                    dto.setName(restaurant.getName());
                    dto.setAddress(restaurant.getAddress());
                    dto.setAbout(restaurant.getAbout());
                    dto.setContact(restaurant.getContact());
                    dto.setMenuItems(restaurant.getMenuItems().stream().map(menuItem -> {
                        MenuItemDTO itemDTO = new MenuItemDTO(menuItem);
                        itemDTO.setId(menuItem.getId());
                        itemDTO.setName(menuItem.getName());
                        itemDTO.setDescription(menuItem.getDescription());
                        itemDTO.setPrice(menuItem.getPrice());
                        itemDTO.setImageUrl(menuItem.getImage_url());
                        return itemDTO;
                    }).collect(Collectors.toList()));
                    return dto;
                }).collect(Collectors.toList());
    }

    public ResturantDTO getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + id));

        ResturantDTO dto = new ResturantDTO();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setAddress(restaurant.getAddress());
        dto.setAbout(restaurant.getAbout());
        dto.setContact(restaurant.getContact());
        dto.setMenuItems(restaurant.getMenuItems().stream().map(menuItem -> {
            MenuItemDTO itemDTO = new MenuItemDTO(menuItem);
            itemDTO.setId(menuItem.getId());
            itemDTO.setName(menuItem.getName());
            itemDTO.setDescription(menuItem.getDescription());
            itemDTO.setPrice(menuItem.getPrice());
            itemDTO.setImageUrl(menuItem.getImage_url());
            return itemDTO;
        }).collect(Collectors.toList()));

        return dto;
    }


    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurant(Long id, Restaurant restaurantDetails) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + id));

        restaurant.setName(restaurantDetails.getName());
        restaurant.setAddress(restaurantDetails.getAddress());
        // Update other fields as necessary

        return restaurantRepository.save(restaurant);
    }

    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + id));

        restaurantRepository.delete(restaurant);
    }

    public MenuItemDTO getMenuItemById(Long restaurantId, Long menuItemId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        MenuItem menuItem = menuItemRepository.findByIdAndRestaurant(menuItemId, restaurant)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        return new MenuItemDTO(menuItem); // Assuming you have a MenuItemDTO class
    }

}

