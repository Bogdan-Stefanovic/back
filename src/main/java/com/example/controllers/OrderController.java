package com.example.controllers;

import com.example.model.Order;
import com.example.model.User;
import com.example.request.OrderItemDTO;
import com.example.services.OrderService;
import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(@RequestBody List<OrderItemDTO> orderItems, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        Order order = orderService.createOrder(orderItems, currentUser);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getUserOrders(Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        List<Order> orders = orderService.getOrdersByUser(currentUser);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
