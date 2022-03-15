package com.cradle.onlineshoppingpurchaseService.v1.services;

import com.cradle.onlineshoppingpurchaseService.v1.entities.*;
import com.cradle.onlineshoppingpurchaseService.v1.enums.Role;
import com.cradle.onlineshoppingpurchaseService.v1.models.UserResponseDto;
import com.cradle.onlineshoppingpurchaseService.v1.repositories.CustomerRepository;
import com.cradle.onlineshoppingpurchaseService.v1.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final CustomerRepository customerRepository;



    public UserService(UserRepository userRepository, EmailService emailService, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.customerRepository = customerRepository;
        Objects.requireNonNull(userRepository,"user Repository is required");
        Objects.requireNonNull(emailService, "email service is required");
        Objects.requireNonNull(customerRepository, "customer repository is required");
    }

    public Optional<Producer> findById(Long id){
        return userRepository.findById(id);
    }

    public Producer saveUser(Producer producer, String requestId) {


        log.info("[" +requestId + "] is about to process request to add user with id" + producer.getId());
        try {
            userRepository.save(producer);
        } catch (Exception e){
            log.warn("an error occurred while persisting user. message: " + e.getMessage());

        }
        return producer;

    }


    public Producer processAndSendRecommendation(AppUser user, Category category, String email){


        if(user != null) {
            String url = "http://localhost:9092/api/v1/product?categoryId=" + category.getId();

            emailService.send("elprince.elp@gmail.com", email,buildEmail(user.getFirstName(), url) );


        }

        return (Producer) user;
    }

    public Producer processAndSendOrderInfo(AppUser user, Order order){
        if(user != null) {
            String url = order.toString();
            String email = user.getUsername();

            emailService.send("elprince.elp@gmail.com", email,buildEmailForOwner(user.getFirstName(), order.toString()));


        }

        return (Producer) user;
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> You have been select by a pal to make a recommendation. Please click on the below link to see the recommendation list: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    private String buildEmailForOwner(String name, String order) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> You have received a new order. Below is the info about the purchase order. </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <p>A new purchase order has been made, details are as follows</p> </p></blockquote>\n Order delivery estimation is three (3) days. \\n\" + <p\">Order name " + order + ",</p>\n"+
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }


    public void processCreateUserMessage( String requestId, UserResponseDto responseDto) {
        Producer producer = null;
        Customer customer = null;
        boolean isSuccessful= false;

        if(responseDto!= null){
            if (responseDto.getRole() == Role.PRODUCER) {
                producer = responseDto.toProducer();
                try {
                    userRepository.save(producer);
                    isSuccessful = true;
                } catch (Exception e) {
                    log.warn("[ " + requestId + " ] error occurred while persisting received producer messages. message : " + e.getMessage());
                }
                }
            if(responseDto.getRole() == Role.USER){
               customer =  responseDto.toCustomer();
                try {
                    customerRepository.save(customer);
                    isSuccessful = true;
                } catch (Exception e) {
                    log.warn("[ " + requestId + " ] error occurred while persisting received customer messages. message : " + e.getMessage());
                }
            }
        }

        log.info("[ " + requestId + " ] create user message received with id : " + ((responseDto.getId() != null) ? responseDto.getId() : "none") + " is successful : " + isSuccessful);
    }
    public void processUpdateUserMessage( String requestId, UserResponseDto responseDto) {
        Producer producer = null;
        Customer customer = null;
        boolean isSuccessful= false;

        if(responseDto!= null){
            if (responseDto.getRole() == Role.PRODUCER) {
                producer = responseDto.toProducer();

                    try {
                        userRepository.save(producer);
                        isSuccessful = true;
                    } catch (Exception e) {
                        log.warn("[ " + requestId + " ] error occurred while persisting received producer messages. message : " + e.getMessage());
                    }
                }

            if(responseDto.getRole() == Role.USER) {
                customer = responseDto.toCustomer();
                    try {
                        customerRepository.save(customer);
                        isSuccessful = true;
                    } catch (Exception e) {
                        log.warn("[ " + requestId + " ] error occurred while persisting received customer messages. message : " + e.getMessage());
                    }
                }
            }


        log.info("[ " + requestId + " ] update user message received with id : " + ((responseDto.getId() != null) ? responseDto.getId() : "none") + " is successful : " + isSuccessful);
    }
    public void processDeleteUserMessage( String requestId, UserResponseDto responseDto) {

        boolean isSuccessful= false;

        if(responseDto!= null){
            if (responseDto.getRole() == Role.PRODUCER) {

                Optional<Producer> optionalProducer = userRepository.findById(responseDto.getId());

                if(optionalProducer.isPresent()){
                    try {
                    userRepository.save(optionalProducer.get());
                    isSuccessful = true;
                } catch (Exception e) {
                    log.warn("[ " + requestId + " ] error occurred while deleting received producer messages. message : " + e.getMessage());

                }
                }
                }
            if(responseDto.getRole() == Role.USER){
                Optional<Customer> optionalCustomer = customerRepository.findById(responseDto.getId());
                if(optionalCustomer.isPresent()) {
                    try {
                        customerRepository.save(optionalCustomer.get());
                        isSuccessful = true;
                    } catch (Exception e) {
                        log.warn("[ " + requestId + " ] error occurred while deleting received customer messages. message : " + e.getMessage());
                    }
                }
            }
        }

        log.info("[ " + requestId + " ] update user message received with id : " + ((responseDto.getId() != null) ? responseDto.getId() : "none") + " is successful : " + isSuccessful);
    }
}
