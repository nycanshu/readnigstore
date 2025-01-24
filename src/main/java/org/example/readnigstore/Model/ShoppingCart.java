package org.example.readnigstore.Model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart {

    private int cartId;
    private User user; // The user who owns the cart, foreign key to User
    private Book book; // The book added to the cart, foreign key to Book
    private int quantity;
    private boolean isCheckedOut; // To track if cart item has been checked out

}