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
2. Install JavaFX kit
3. Install **MySQL Server**.
4. Set up your environment with a compatible **IDE** (e.g., IntelliJ IDEA, Eclipse).

### Steps to Run the Application
1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com/nycanshu/readnigstore
   ```
2. Import the project into your IDE.
3. Set up the database:
   - Create a new MySQL database.
   - Execute the provided SQL script (`schema.sql`) to initialize the database schema. - (It will be updated later)
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

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request for review.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.

## Contact
For any inquiries or support, please contact:
- **Developer Name**: [Himanshu](https://www.linkedin.com/in/okay-anshu/)
- **Email**: [himanshu.is.dev@gmail.com]

## Output Samples
These are some sample images, but the actual project contains more screens with more additional features. 

<div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px;">
  <img src="https://github.com/user-attachments/assets/30ec9741-89db-4e56-99a6-85f2c48032b9" alt="Output 1" style="width: 100%;"/>
  <img src="https://github.com/user-attachments/assets/9b3c2f25-e0bd-40a8-b05a-3dbe77bd6048" alt="Output 2" style="width: 100%;"/>
  <img src="https://github.com/user-attachments/assets/36939478-2b88-4707-bd15-0d1556ab7491" alt="Output 3" style="width: 100%;"/>
  <img src="https://github.com/user-attachments/assets/e52ddcde-44c7-4ee3-bcb6-5bf917c265fa" alt="Output 4" style="width: 100%;"/>
  <img src="https://github.com/user-attachments/assets/ea1cce1c-8002-4037-9b1f-1fda175dca01" alt="Output 5" style="width: 100%;"/>
  <img src="https://github.com/user-attachments/assets/511fd5c9-c344-40b4-bd5a-85e5875eb96c" alt="Output 6" style="width: 100%;"/>
</div>


