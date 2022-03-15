package com.cradle.onlineshoppingpurchaseService.v1.controllers;

import com.cradle.onlineshoppingpurchaseService.v1.entities.Producer;
import com.cradle.onlineshoppingpurchaseService.v1.repositories.*;
import com.cradle.onlineshoppingpurchaseService.v1.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        ProductService.class,
        CategoryService.class,
        CustomerService.class,
        OrderService.class,
        ShoppingCartService.class,
        UserService.class,
        EmailService.class,
        TemplateService.class,
        JavaMailSender.class
})
@MockBean(UserRepository.class)
@MockBean(CategoryRepository.class)
@MockBean(ProductRepository.class)
@MockBean(CartItemRepository.class)
@MockBean(OrderRepository.class)
@MockBean(ShoppingCartRepository.class)
@MockBean(CustomerRepository.class)

@Slf4j
class ProductControllerTest {

    @Autowired
    private  ProductService productService;

    @Autowired
    private  UserService userService;

    @Autowired
    private  CategoryService categoryService;

    @Autowired
    private  OrderService orderService;

    @Autowired
    private  ShoppingCartService shoppingCartService;

    @Autowired
    private  CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProductController(productService,userService,categoryService, orderService,shoppingCartService,customerService)).build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }




    @Test
    void addProduct() throws Exception {

            Mockito.when(productService.findById(Mockito.anyLong())).thenReturn(null);

            Producer producer = new Producer();
            producer.setFirstName("Prince");
            producer.setLastName("David");

            String inputJson = mapToJson(producer);

            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/1/product/add")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(inputJson))
                    .andExpect(status().isCreated()).andReturn();

            MockHttpServletResponse httpServletResponse = mvcResult.getResponse();

            String responseAsString = httpServletResponse.getContentAsString();

            assertNotNull(responseAsString, "Response string cannot be null");


    }

    @Test
    void addProductToCart() {
    }

    @Test
    void sendRecommendation() {
    }

    @Test
    void listOfProductsByCategory() {
    }

    @Test
    void orderItems() {
    }
}