package org.example.readnigstore.Model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {

    private int orderDetailId;
    private Order order; // Reference to the order, foreign key to Order
    private Book book; // The book in this order detail, foreign key to Book
    private int quantity; // Quantity of the book in this order
    private double price; // Price per unit at the time of purchase
    private Timestamp orderDate; // Date and time the order was placed

}
