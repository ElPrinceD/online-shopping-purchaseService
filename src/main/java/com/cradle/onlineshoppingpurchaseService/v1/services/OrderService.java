package com.cradle.onlineshoppingpurchaseService.v1.services;

import com.cradle.onlineshoppingpurchaseService.v1.entities.*;
import com.cradle.onlineshoppingpurchaseService.v1.repositories.OrderRepository;
import com.cradle.onlineshoppingpurchaseService.v1.repositories.ShoppingCartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final CustomerService customerService;

    public OrderService(OrderRepository orderRepository, ShoppingCartRepository shoppingCartRepository, UserService userService, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.userService = userService;
        this.customerService = customerService;
        Objects.requireNonNull(customerService, "customer service is required");
        Objects.requireNonNull(shoppingCartRepository, "shopping cart repository is required");
        Objects.requireNonNull(userService, "user service is required");
        Objects.requireNonNull(orderRepository, "order repository is required");
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }


    public Order save(Order order, String requestId) {
        log.info("[" + requestId + "] is about to process request to add order with id " + order.getId());
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            log.warn("an error occurred while persisting order. message: " + e.getMessage());
            throw new IllegalArgumentException(e);

        }
        return order;
    }

    public Order makeAnOrder(Order order, String requestId, List<CartItem> items, Customer customer){
        if(order != null){
            Producer owner = null;
            for (CartItem item: items) {
               owner = item.getProduct().getProductOwner();
                userService.processAndSendOrderInfo(owner,order);
            }

        }

        return order;
    }

    public Double getTotalPrice(List<CartItem> cartItems){
        double price = 0.00;
        for (CartItem item: cartItems) {
            Integer quantity = item.getQuantity();
            price += item.getProduct().getPrice() * quantity;

        }

        price = Math.round(price * 100.0) / 100.0;
        return price;
    }

    public void updateOnChildEntities(Order order){
        if (order != null){
            ShoppingCart shoppingCart = null;
            Producer owner = null;
            Customer customer = null;
            List<CartItem> cartItems = order.getCartItems();
            for (CartItem item : cartItems) {

                //Removing the cartItem from the shopping cart as soon as an order is made
                shoppingCart = item.getShoppingCart();
                if(shoppingCart != null){
                    List<CartItem> shoppingCartCartItems = shoppingCart.getCartItems();
                    shoppingCartCartItems.remove(item);
                    shoppingCart.setCartItems(shoppingCartCartItems);
                    try {
                        shoppingCartRepository.save(shoppingCart);
                    } catch (Exception e){
                        log.warn("an error occurred while persisting shopping cart. message: " + e.getMessage());
                    }

                }

                //Add the entire order to the owner's orders
                Product product = item.getProduct();
                owner = product.getProductOwner();
                if(owner != null){
                    List<Order> orders = owner.getOrders();
                    orders.add(order);
                    owner.setOrders(orders);
                    userService.saveUser(owner,null);


                }



            }

            customer = order.getCustomer();

            List<Order> customerOrders = customer.getOrders();
            customerOrders.add(order);
            customer.setOrders(customerOrders);
            customerService.save(customer, null);


        }
    }


}
