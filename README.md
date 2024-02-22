<h1>Personal Library Management System</h1>

This project is a personal library management system built with Spring Boot, Spring Security, Hibernate, and MySQL. It provides users with the ability to manage their own library collections, with features including user authentication using JWT tokens, role-based authorization, and CRUD operations on the main database.

<h3>Key Features</h3>

- User Authentication: Secure user authentication using JWT tokens ensures that only authorized users can access the system.
- Role-Based Authorization: Administrators have full CRUD (Create, Read, Update, Delete) permissions on the main database, while regular users (members) can manage their personal library collections.
- Personal Library Management: Members can add, remove, and view books in their personal library. They can also update the number of times a book has been read, allowing for easy tracking of reading habits and preferences.
  
<h3>Technologies Used</h3>

- Spring Boot: Framework for building robust Java applications.
- Spring Security: Provides authentication, authorization, and other security features for Spring applications.
- Hibernate: ORM (Object-Relational Mapping) tool for interacting with the database.
- MySQL: Relational database management system used for storing application data.

<h3>Getting Started</h3>

To run the project locally, follow these steps:

1. Clone the repository to your local machine.
2. Configure the MySQL database settings in the application.properties file.
3. Build and run the project using Maven or your preferred IDE.
4. Access the application through the provided endpoints and explore the features.

Set these environmental variables
![Screenshot 2024-02-22 105717](https://github.com/baldi364/library/assets/134001121/e1389068-c865-42d0-b052-566345f483d7)

Swagger and controllers will appear just like this:
![Screenshot 2024-02-22 103826](https://github.com/baldi364/library/assets/134001121/ae0bb226-b97c-475d-87bb-5c33efb0afaf)
