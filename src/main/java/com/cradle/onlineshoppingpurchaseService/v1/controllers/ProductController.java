package com.cradle.onlineshoppingpurchaseService.v1.controllers;

import com.cradle.onlineshoppingpurchaseService.v1.entities.*;
import com.cradle.onlineshoppingpurchaseService.v1.enums.Role;
import com.cradle.onlineshoppingpurchaseService.v1.models.*;
import com.cradle.onlineshoppingpurchaseService.v1.models.Error;
import com.cradle.onlineshoppingpurchaseService.v1.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.*;

@RestController
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final ShoppingCartService shoppingCartService;
    private final CustomerService customerService;


    public ProductController(ProductService productService, UserService userService, CategoryService categoryService, OrderService orderService, ShoppingCartService shoppingCartService, CustomerService customerService) {
        this.productService = productService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.shoppingCartService = shoppingCartService;
        this.customerService = customerService;
        Objects.requireNonNull(orderService, "order service is required");
        Objects.requireNonNull(customerService, "customer service is required");
        Objects.requireNonNull(shoppingCartService,"shopping cart service is required");
        Objects.requireNonNull(productService, "product service is required");
        Objects.requireNonNull(userService,"user service is required");
        Objects.requireNonNull(categoryService, "category service is required");
    }


    @PostMapping("/api/v1/{ownerId}/product/add")
    public ApiResponse<?> addProduct(@RequestBody AddProductModel model,
                                     @PathVariable Long ownerId,
                                     HttpServletResponse response,
                                     HttpServletRequest request){
        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to add product with name " + model.getName());

        Optional<Producer> optionalUser = userService.findById(ownerId);
        if(optionalUser.isEmpty()){
            log.info("[ " + requestId + " ] request to add Product failed, user cannot be found");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "User cannot be found"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);
        }

        Producer producer = optionalUser.get();

        if(producer.getRole() != Role.PRODUCER){
            log.info("[ " + requestId + " ] request to add Product failed, user cannot add product");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "User is not a producer"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);
        }

        Product product = new Product();

        product.setName(model.getName());
        product.setProductOwner(producer);
        product.setImageURL(model.getImageURL());
        product.setPrice(model.getPrice());

        List<Category> categoryList = new ArrayList<>();
        Category category = null;
        if(!model.getCategoryIds().isEmpty()){
            for(Long id : model.getCategoryIds()){
                Optional<Category> optionalCategory = categoryService.findById(id);
                if(optionalCategory.isPresent()){
                    category = optionalCategory.get();
                    categoryList.add(category);

                }
            }

        }

        product.setCategories(categoryList);

        ZonedDateTime currentDate = ZonedDateTime.now();
        product.setCreatedOn(currentDate);
        product.setUpdatedOn(currentDate);




        product = productService.save(product, requestId);
        productService.updateOnChildEntities(product);

        ProductResponseDto responseDto = null;
        if(product != null){
            responseDto = product.toResponse();
        }


        response.setStatus(status);
        log.info("[ " + requestId + " ] request to add Product " + model.getName() + " successful");
        return new ApiResponse<>(request, HttpStatus.CREATED,responseDto , null);
    }

    @PutMapping("/api/v1/{customerId}/cart/add")
    public ApiResponse<?> addProductToCart(@PathVariable Long customerId,
                                           @RequestParam Long productId,
                                           @RequestBody OrderRequestDto model,
                                           HttpServletRequest request,
                                           HttpServletResponse response){
        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to add product to cart for user with id " + customerId);

        Optional<Product> optionalProduct = productService.findById(productId);

        if(optionalProduct.isEmpty()){
            log.info("[ " + requestId + " ] request to add Product failed, product cannot be found");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "product cannot be found"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);
        }

        Optional<Customer> optionalCustomer = customerService.findById(customerId);
        if(optionalCustomer.isEmpty()){
            log.info("[ " + requestId + " ] request to add Product failed, user cannot be found");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "user cannot be found"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);
        }

        Product product = optionalProduct.get();
        Customer user = optionalCustomer .get();
        ShoppingCart shoppingCart = null;

        if(user.getShoppingCart() != null) {
            shoppingCart = user.getShoppingCart();
        } else {
            shoppingCart = new ShoppingCart();
        }

        CartItem cartItem = new CartItem();

        cartItem.setProduct(product);
        cartItem.setQuantity(model.getQuantity());
        cartItem.setShoppingCart(shoppingCart);

        shoppingCart.setCustomer(user);
        List<CartItem> cartItems = shoppingCart.getCartItems();


        cartItems.add(cartItem);
        shoppingCart.setCartItems(cartItems);

        shoppingCart =shoppingCartService.save(shoppingCart, requestId);
        productService.saveCartItem(cartItem, requestId);


        CartResponseDto responseDto = null;
        if (shoppingCart != null){
            responseDto = shoppingCart.toResponseDto();
        }

        response.setStatus(status);
        log.info("[ " + requestId + " ] request to add Product " + product.getName() + " successful");
        return new ApiResponse<>(request, HttpStatus.OK, responseDto, null);


    }

    @PostMapping("/api/v1/{customerId}")
    public ApiResponse<?> sendRecommendation(@RequestParam(required = true) Long categoryId,
                                             @PathVariable(required = true) Long customerId,
                                             @RequestBody(required = true) SendEmailDto model ,
                                             HttpServletResponse response,
                                             HttpServletRequest request){
        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to send recommendation from user with id" + customerId);

        Optional<Customer> optionalCustomer = customerService.findById(customerId);
        if(optionalCustomer.isEmpty()){
            log.info("[ " + requestId + " ] request to add to send recommendation, user cannot be found");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "user cannot be found"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);
        }

        Customer customer= optionalCustomer.get();

        Optional<Category> optionalCategory = categoryService.findById(categoryId);
        if(optionalCategory.isEmpty()){
            log.info("[ " + requestId + " ] request to add to send recommendation, category cannot be found");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "category cannot be found"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);
        }



        Category category = optionalCategory.get();
        userService.processAndSendRecommendation(customer, category, model.getEmail());





        response.setStatus(status);
        log.info("[ " + requestId + " ] request to send recommendation to email " +model.getEmail() + " successful");
        return new ApiResponse<>(request, HttpStatus.OK, null, null);
    }


    @GetMapping("/api/v1/product/category")
    public ApiResponse<?> listOfProductsByCategory(@RequestParam Long categoryId,
                                                   @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                   @RequestParam(value = "sortBy", required = false, defaultValue = "createdOn") String sortBy,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response){
        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to get list of products for category with id " + categoryId);

        if(categoryId == null){
            log.info("[ " + requestId + " ] request to get products failed, category is null");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "category is null"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);
        }

        Optional<Category> optionalCategory = categoryService.findById(categoryId);

        if(optionalCategory.isEmpty()){
            log.info("[ " + requestId + " ] request to get products failed, category cannot be found");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "category cannot be found"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);
        }

        Category category = optionalCategory.get();

        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page,pageSize, sort);

        Page<Product> productsByCategory = productService.getProductByCategory(category, pageable);


        response.setStatus(status);
        log.info("[ " + requestId + " ] request to get all by category with name " + category.getName() + " successful");
        return new ApiResponse<>(request, HttpStatus.OK, productsByCategory, null);



    }

    @PostMapping("/add/{customerId}/order")
    public ApiResponse<?> orderItems(@RequestBody OrderRequestDto model,
                                     @PathVariable Long customerId,
                                     HttpServletResponse response,
                                     HttpServletRequest request){
        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to order list of products for customer with id " + customerId);

        if(model == null){
            log.info("[ " + requestId + " ] request to order products failed, order is null");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "order is null"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);

        }

        Optional<Customer> optionalCustomer = customerService.findById(customerId);

        if(optionalCustomer.isEmpty()){
            log.info("[ " + requestId + " ] request to order products, user cannot be found");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "user cannot be found"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);
        }

        if(model.getCartItemIds() == null){
            log.info("[ " + requestId + " ] request to order products failed, product is null");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "product is null"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);
        }

        Customer customer = optionalCustomer.get();

        if(model.getCartItemIds().isEmpty()){
            log.info("[ " + requestId + " ] request to order products failed, order is null");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "order is null"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setCompleted(false);
        order.setLocation(model.getLocation());
        ZonedDateTime currentTime = ZonedDateTime.now();
        order.setCreatedOn(currentTime);
        order.setUpdatedOn(currentTime);
        List<CartItem> orderCartItems = order.getCartItems();



        for (Long id: model.getCartItemIds()) {

            Optional<CartItem> optionalCartItem = shoppingCartService.findCartById(id);

            if(optionalCartItem.isPresent()){
                CartItem cartItem = optionalCartItem.get();
                orderCartItems.add(cartItem);
            }
        }
        order.setCartItems(orderCartItems);



        Double price = orderService.getTotalPrice(orderCartItems);
        order.setTotalPrice(price);
        order = orderService.save(order, requestId);
        orderService.updateOnChildEntities(order);

        orderService.makeAnOrder(order,requestId,orderCartItems,customer);



        response.setStatus(status);
        log.info("[ " + requestId + " ] request to make an order for items successful");
        return new ApiResponse<>(request, HttpStatus.OK, order.toString(), null);

    }


    @GetMapping("/api/v1/product")
    public ApiResponse<?> searchItems(@RequestParam String productName,
                                      @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "sortBy", required = false, defaultValue = "createdOn") String sortBy,
                                      HttpServletRequest request,
                                      HttpServletResponse response){
        String requestId = request.getSession().getId();
        int status = 200;
        log.info("[" +requestId + "] is about to process request to search for product with name " + productName);

        if(!StringUtils.hasText(productName)){
            log.info("[ " + requestId + " ] request to search for products failed, product name is null");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "product name is null"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page,pageSize, sort);
        Page<Product> products = productService.findByName(productName, pageable);
        if(products.isEmpty()){
            log.info("[ " + requestId + " ] request to search for products failed, products donot exist");
            status = 400;
            response.setStatus(status);
            List<Error> errors = Collections.singletonList(new Error("error", "product do not exist"));
            return new ApiResponse<>(request, HttpStatus.BAD_REQUEST, null, errors);
        }

        return new ApiResponse<>(request, HttpStatus.OK, products, null);


    }



}

