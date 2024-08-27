package com.example.repository;

import com.example.model.MenuItem;
import com.example.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    Optional<MenuItem> findByIdAndRestaurant(Long id, Restaurant restaurant);

}

