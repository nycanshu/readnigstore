package org.example.readnigstore.Model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private boolean isAdmin;


    public User(String firstName, String lastName, String username, String password, boolean isAdmin) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}
