<h1>Personal Library Management System</h1>

This project is a personal library management system built with Java, Spring Boot, Spring Security, Hibernate, Docker and PostgreSQL. It provides users with the ability to manage their own library collections, with features including user authentication using JWT tokens, role-based authorization, and CRUD operations on the main database.

<h3>Key Features</h3>

- **User Authentication:** Secure user authentication using JWT tokens ensures that only authorized users can access the system.
- **Role-Based Authorization:** Administrators have full CRUD (Create, Read, Update, Delete) permissions on the main database, while regular users (members) can manage their personal library collections.
- **Personal Library Management:** Members can add, remove, and view books in their personal library. They can also update the number of times a book has been read, allowing for easy tracking of reading habits and preferences.
  
<h3>Technologies Used</h3>

- **Spring Boot**: Framework for building robust Java applications.
- **Spring Security**: Provides authentication, authorization, and other security features for Spring applications.
- **Hibernate**: ORM (Object-Relational Mapping) tool for interacting with the database.
- **PostgreSQL:** Relational database management system used for storing application data.
- **Docker:** Platform for developing, shipping, and running applications in containers.


<h3>Updates in development</h3>

I'm currently working on implementing:
  - **Mapstruct**
  - **Liquibase**

<h3>Future updates</h3>

- **Front-end with Angular**

<h3>Getting Started</h3>

To run the project locally, follow these steps:

1. Clone the repository to your local machine.
2. Configure the PostgreSQL database settings in the application.yaml file.
3. Start the Docker containers 
4. Build and run the project using Maven or your preferred IDE.
5. Access the application through the provided endpoints and explore the features.

Set these user and password on PostgreSQL:
```
environment:
      - 'POSTGRES_DB=library_db'
      - 'POSTGRES_USER=library_admin_user'
      - 'POSTGRES_PASSWORD=library_admin_password'
```

Swagger and controllers will appear just like this:
![Screenshot 2024-02-22 103826](https://github.com/baldi364/library/assets/134001121/ae0bb226-b97c-475d-87bb-5c33efb0afaf)
