package com.example.services;

import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.User;
import com.example.repository.MenuItemRepository;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.request.OrderItemDTO;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public Order createOrder(List<OrderItemDTO> orderItems, User user) {
        Order order = new Order();
        order.setCustomer(user);

        // Save the order first so it gets an ID
        order = orderRepository.save(order);

        Order finalOrder = order;
        List<OrderItem> orderItemList = orderItems.stream().map(itemDTO -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItemRepository.findById(itemDTO.getMenuItemId()).orElseThrow());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setOrder(finalOrder); // Set the Order in the OrderItem after the Order is saved
            return orderItemRepository.save(orderItem); // Save each OrderItem
        }).collect(Collectors.toList());

        order.setOrderItems(orderItemList);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(User user) {
        List<Order> orders = orderRepository.findByCustomer(user);
        orders.forEach(order -> {
            Hibernate.initialize(order.getOrderItems());
            order.getOrderItems().forEach(orderItem -> Hibernate.initialize(orderItem.getMenuItem()));
        });
        return orders;
    }


}
