# Reading Store JavaFX Application

## Overview
The **Reading Store** is a JavaFX-based desktop application designed to facilitate the buying and selling of books. The application provides a seamless user experience with features tailored for both customers and administrators. The system implements role-based access control to ensure that different user roles have access to the features specific to their needs.

## Features

### 1. Role-Based Access Control
- Different user roles such as **Admin** and **Customer**.
- Admins have privileges to manage the book inventory, including adding, editing, and deleting books.
- Customers can browse, add books to their cart, and proceed with purchases.

### 2. Dedicated Buy/Sell Feature
- Customers can buy books directly.
- Users can also list books for sale, enabling a two-way marketplace.

### 3. Enhanced Cart Management
- The cart system includes a limit-checking mechanism to ensure proper handling of items.
- Users are notified if the cart exceeds the predefined limit.

### 4. Efficient Checkout Process
- A streamlined process for checking out ensures a quick and hassle-free experience.
- Includes validation for payment and order confirmation.

### 5. Admin Book Management
- Admins can add new books to the store’s inventory.
- Books can be edited or removed based on requirements.

## Tech Stack

- **Java**: Core programming language for application logic.
- **JavaFX**: Used for building a responsive and modern user interface.
- **JDBC**: Facilitates database connectivity and interaction.
- **MySQL**: Database management system for storing user, book, and transaction data.

## Installation and Setup

### Prerequisites
1. Install **Java JDK** (version 11 or later).
2. Install **MySQL Server**.
3. Set up your environment with a compatible **IDE** (e.g., IntelliJ IDEA, Eclipse).

### Steps to Run the Application
1. Clone the repository to your local machine:
   ```bash
   git clone <repository-url>
   ```
2. Import the project into your IDE.
3. Set up the database:
   - Create a new MySQL database.
   - Execute the provided SQL script (`schema.sql`) to initialize the database schema.
4. Update database configurations in the application (e.g., `db.properties` file):
   ```properties
   db.url=jdbc:mysql://localhost:3306/reading_store
   db.user=root
   db.password=yourpassword
   ```
5. Build and run the project through your IDE or using the command line:
   ```bash
   mvn javafx:run
   ```

## Usage Instructions

### For Admins
- Log in with admin credentials.
- Navigate to the "Manage Books" section to add, edit, or delete books.

### For Customers
- Browse the catalog and add books to your cart.
- Proceed to checkout to complete your purchase.

## Future Enhancements
- **User Registration and Profile Management**: Allow customers to create accounts and manage profiles.
- **Payment Gateway Integration**: Add online payment options for better convenience.
- **Analytics Dashboard**: Provide sales and user activity insights for admins.
- **Enhanced Search and Filter**: Improve the browsing experience with advanced search capabilities.

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request for review.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.

## Contact
For any inquiries or support, please contact:
- **Developer Name**: [Himanshu](https://www.linkedin.com/in/okay-anshu/)
- **Email**: [himanshu.is.dev@gmail.com]

