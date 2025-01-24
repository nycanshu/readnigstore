package org.example.readnigstore.Model;

public class OrderDetail {

    private int orderDetailId;
    private Order order; // Reference to the order, foreign key to Order
    private Book book; // The book in this order detail, foreign key to Book
    private int quantity; // Quantity of the book in this order
    private double price; // Price per unit at the time of purchase

    // Getters and Setters
    // Constructor(s)
}
