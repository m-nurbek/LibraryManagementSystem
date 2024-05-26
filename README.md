## Library Management System

This project involves the development of a web application for library management. The intended audience is library users and staff. The application will cater to various needs such as searching for books, reserving books, managing checkouts and returns, adding and managing books, and user account management. It aims to provide a seamless and efficient user experience for both library patrons and staff.

## Functional Requirements

1. Users, Anonymous Users, and Librarians should be able to search for books by title, author, or ISBN.
2. The system should allow Users to reserve books.
3. Librarians can search Users by surname, name, or email and see the list of reserved books by a specific User.
4. Librarians can mark books as returned for a specific User, or extend the due date/report User if the books is overdue.
5. Librarians should be able to add new books, update book information, remove (archived and then automatically removed after some time), unmark as archived.
6. System have to support authentication by email and password/JWT(Optional)
7. System must be able to display a list of books in the library.
8. API must support sort books by title, publish date, author.
9. API must support pagination.
10. The system must keep track of due dates, and send automated reminders to users. (scheduled jobs)

## Non-Functional Requirements

1. The system should be user-friendly, with an intuitive interface.
2. The system should be secure, with user authentication and authorization.
3. The system should be able to generate reports/notifications on user/librarian activity and book circulation.
4. Test coverage ~60-70%.

## Use Case Diagram
![image](https://github.com/m-nurbek/LibraryManagementSystem/assets/65078035/4bb52340-84f9-4ab4-84fc-dce8a9d0c8a8)

## UML Diagram
![image](https://github.com/m-nurbek/LibraryManagementSystem/assets/65078035/477f69cf-3d45-4786-a16a-512e2502dd88)

## ER Diagram
![image](https://github.com/m-nurbek/LibraryManagementSystem/assets/65078035/a551700c-8c2d-4cb5-bc08-074c8e86fdec)

## Interface Design
https://www.figma.com/design/KlVV8mZH1lXUlx9r7Il89l/Library-Management-System-Design-EPAM?node-id=0-1&t=EsGNGrl2Wh2LkDpF-1

