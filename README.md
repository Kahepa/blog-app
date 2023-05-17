## 23994

## Affinity Blog

Affinity Blog is a Java Spring Boot application that allows users to create, read, update, and delete blog posts. It provides a user-friendly interface for managing blog content and supports authentication and authorization for different user roles.

## Feature

- User Registration: Users can create an account to access the blog application.
- User Authentication: Registered users can log in using their credentials.
- User Roles: The application supports three user roles: Anonymous, User, and Admin.
- Blog Post Management: Users can create, read, update, and delete blog posts.
- Rich Text Editor: The blog post editor provides a WYSIWYG interface for formatting and styling content.
- Image Upload: Users can upload and associate images with their blog posts.
- Image Download: Users can download images associated with a blog posts.
- Access Control: User roles determine the actions a user can perform on the blog posts.
- Responsive Design: The application is responsive and works seamlessly on different devices.


## Prerequisites:

Before running the Affinity Blog application, ensure that you have the following prerequisites installed:

- Java Development Kit (JDK) 17 or higher
- Maven
- Git


## Getting Started
Follow these steps to get the Affinity Blog application up and running:

# Clone the repository:

git clone https://github.com/Kahepa/blog-app.git


# Navigate to the project directory:

cd blog-app

# Build the project using Maven:

mvn clean install

# Run the application:

mvn spring-boot:run

# Access the application in your web browser:

http://localhost:8080


## Default User Accounts

# The Affinity Blog application comes with pre-configured user accounts for testing purposes:

- Email: **user.user@domain.com**
- Password: password
- User Role: User

- Email: **admin.admin@domain.com**
- Password: password
- User Role: Admin

- Anynimous User can only read blogs


## Technologies Used

- Java Spring Boot
- Spring Data JPA
- H2 Database
- Thymeleaf
- Spring Security
- Model-View-Controller (MVC) architecture
- Bootstrap (for styling)

## APPLICATION DOCUMENTS
-- User Documentation https://docs.google.com/document/d/1ncmpj6oD2m3s1fvjDZDfLKrSCYyPrKzmOpdjDIeGl_Y/edit?usp=sharing
-- Project Plan https://docs.google.com/document/d/1Ug-J88v15Lzad1jbJiK9jNIERCuMQ2a1z1G9uqziGIE/edit?usp=sharing
-- Project Requirement https://docs.google.com/document/d/1XklRf4EebRT3lHdcb5PthTl2AFR5tVHjH2aswifWaaQ/edit?usp=sharing
-- Technical Documentation https://docs.google.com/document/d/1PnDb1amzWpVJIUWBLfbjWct-afhdvFRh9ALQHAJFiTc/edit?usp=sharing
