package org.example.readnigstore.Model;

import java.util.Date;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private int orderId;
    private User user; // The user who placed the order, foreign key to User
    private Date orderDate; // Date and time of order
    private double totalPrice; // Calculated total price for the order
    private String paymentCardNumber; // Fake payment info, validate only if 16 digits
    private String paymentExpiryDate;
    private String paymentCVV;

    private List<OrderDetail> orderDetails; // List of books in the order

}