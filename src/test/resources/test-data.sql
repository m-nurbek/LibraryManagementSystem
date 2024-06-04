-- Description: This file contains the test data for the application. Each table has exactly 10 rows.
-- Version: 1.0
-- Date: 2024-06-02
-- Author: Nurbek Malikov

-- account password is 'password'
insert into ACCOUNT (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE) values (1, 'Vera', 'Trahear', 'vtrahear0@flavors.me', '$2a$10$2Uz4lxxlJG9/iM2fpjmP5OWBAPPMD7X0zb.zcfMQV9HSzuwIX5h0u', null);
insert into ACCOUNT (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE) values (2, 'Claudette', 'McKirton', 'cmckirton1@tinyurl.com', '$2a$10$2Uz4lxxlJG9/iM2fpjmP5OWBAPPMD7X0zb.zcfMQV9HSzuwIX5h0u', null);
insert into ACCOUNT (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE) values (3, 'Nappie', 'Somes', 'nsomes2@sohu.com', '$2a$10$2Uz4lxxlJG9/iM2fpjmP5OWBAPPMD7X0zb.zcfMQV9HSzuwIX5h0u', null);
insert into ACCOUNT (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE) values (4, 'Farah', 'Raine', 'fraine3@flickr.com', '$2a$10$2Uz4lxxlJG9/iM2fpjmP5OWBAPPMD7X0zb.zcfMQV9HSzuwIX5h0u', null);
insert into ACCOUNT (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE) values (5, 'Adelle', 'Cheal', 'acheal4@bizjournals.com', '$2a$10$2Uz4lxxlJG9/iM2fpjmP5OWBAPPMD7X0zb.zcfMQV9HSzuwIX5h0u', null);
insert into ACCOUNT (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE) values (6, 'Lauri', 'Townes', 'ltownes5@google.pl', '$2a$10$2Uz4lxxlJG9/iM2fpjmP5OWBAPPMD7X0zb.zcfMQV9HSzuwIX5h0u', null);
insert into ACCOUNT (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE) values (7, 'Sheree', 'Mallard', 'smallard6@paypal.com', '$2a$10$2Uz4lxxlJG9/iM2fpjmP5OWBAPPMD7X0zb.zcfMQV9HSzuwIX5h0u', null);
insert into ACCOUNT (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE) values (8, 'Hermine', 'Izzett', 'hizzett7@miitbeian.gov.cn', '$2a$10$2Uz4lxxlJG9/iM2fpjmP5OWBAPPMD7X0zb.zcfMQV9HSzuwIX5h0u', null);
insert into ACCOUNT (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE) values (9, 'Jeffy', 'Cumbes', 'jcumbes8@tamu.edu', '$2a$10$2Uz4lxxlJG9/iM2fpjmP5OWBAPPMD7X0zb.zcfMQV9HSzuwIX5h0u', null);
insert into ACCOUNT (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE) values (10, 'Lianna', 'Judkins', 'ljudkins9@cbslocal.com', '$2a$10$2Uz4lxxlJG9/iM2fpjmP5OWBAPPMD7X0zb.zcfMQV9HSzuwIX5h0u', null);

insert into AUTHOR (ID, NAME) values (1, 'Kali Shiels');
insert into AUTHOR (ID, NAME) values (2, 'Oby Teanby');
insert into AUTHOR (ID, NAME) values (3, 'Liva Rown');
insert into AUTHOR (ID, NAME) values (4, 'Ola Dunstan');
insert into AUTHOR (ID, NAME) values (5, 'Elysee Stammer');
insert into AUTHOR (ID, NAME) values (6, 'Steffane Dewerson');
insert into AUTHOR (ID, NAME) values (7, 'Madelle Capp');
insert into AUTHOR (ID, NAME) values (8, 'Wyndham Loffel');
insert into AUTHOR (ID, NAME) values (9, 'Sunny Freeburn');
insert into AUTHOR (ID, NAME) values (10, 'Gage Powter');

insert into BOOK (ID, ISBN, TITLE, LANGUAGE, NUMBER_OF_PAGES, STATUS, PUBLISH_DATE, ARCHIVE_DATE, NUMBER_OF_COPIES, AUTHOR_ID) values (1, '108357909-6', 'Champagne for Caesar', 'PH', 1, null, '2023-02-16', null, 1, 1);
insert into BOOK (ID, ISBN, TITLE, LANGUAGE, NUMBER_OF_PAGES, STATUS, PUBLISH_DATE, ARCHIVE_DATE, NUMBER_OF_COPIES, AUTHOR_ID) values (2, '031405334-4', 'Where''s Poppa?', 'PH', 2, null, '2023-05-20', null, 2, 2);
insert into BOOK (ID, ISBN, TITLE, LANGUAGE, NUMBER_OF_PAGES, STATUS, PUBLISH_DATE, ARCHIVE_DATE, NUMBER_OF_COPIES, AUTHOR_ID) values (3, '253996010-3', 'Girls Just Want to Have Fun', 'RU', 3, null, '2022-10-08', null, 3, 3);
insert into BOOK (ID, ISBN, TITLE, LANGUAGE, NUMBER_OF_PAGES, STATUS, PUBLISH_DATE, ARCHIVE_DATE, NUMBER_OF_COPIES, AUTHOR_ID) values (4, '749507863-4', 'Zotz!', 'FR', 4, null, '2024-05-25', null, 4, 4);
insert into BOOK (ID, ISBN, TITLE, LANGUAGE, NUMBER_OF_PAGES, STATUS, PUBLISH_DATE, ARCHIVE_DATE, NUMBER_OF_COPIES, AUTHOR_ID) values (5, '151146632-4', 'Mrs. Henderson Presents', 'RU', 5, null, '2024-04-13', null, 5, 5);
insert into BOOK (ID, ISBN, TITLE, LANGUAGE, NUMBER_OF_PAGES, STATUS, PUBLISH_DATE, ARCHIVE_DATE, NUMBER_OF_COPIES, AUTHOR_ID) values (6, '327233292-8', 'Dead Space: Aftermath', 'MX', 6, null, '2019-06-17', null, 6, 6);
insert into BOOK (ID, ISBN, TITLE, LANGUAGE, NUMBER_OF_PAGES, STATUS, PUBLISH_DATE, ARCHIVE_DATE, NUMBER_OF_COPIES, AUTHOR_ID) values (7, '992683928-2', 'Love Me No More (Deux jours à tuer)', 'DE', 7, null, '2023-07-01', null, 7, 7);
insert into BOOK (ID, ISBN, TITLE, LANGUAGE, NUMBER_OF_PAGES, STATUS, PUBLISH_DATE, ARCHIVE_DATE, NUMBER_OF_COPIES, AUTHOR_ID) values (8, '122865408-5', 'Oh, Woe Is Me (Hélas pour moi)', 'TH', 8, null, '2011-02-06', null, 8, 8);
insert into BOOK (ID, ISBN, TITLE, LANGUAGE, NUMBER_OF_PAGES, STATUS, PUBLISH_DATE, ARCHIVE_DATE, NUMBER_OF_COPIES, AUTHOR_ID) values (9, '225294039-5', '41-Year-Old Virgin Who Knocked Up Sarah Marshall and Felt Superbad About It, The', 'PE', 9, null, '2017-12-31', null, 9, 9);
insert into BOOK (ID, ISBN, TITLE, LANGUAGE, NUMBER_OF_PAGES, STATUS, PUBLISH_DATE, ARCHIVE_DATE, NUMBER_OF_COPIES, AUTHOR_ID) values (10, '062140903-0', 'Calling Dr. Death', 'PT', 10, null, '2021-02-07', null, 10, 10);

insert into BOOK_RESERVATION (ID, RESERVATION_DATE, DUE_DATE, STATUS, BOOK_ID, ACCOUNT_ID) values (1, '2024-01-31', '2023-12-07', null, 1, 1);
insert into BOOK_RESERVATION (ID, RESERVATION_DATE, DUE_DATE, STATUS, BOOK_ID, ACCOUNT_ID) values (2, '2023-12-28', '2023-06-09', null, 2, 2);
insert into BOOK_RESERVATION (ID, RESERVATION_DATE, DUE_DATE, STATUS, BOOK_ID, ACCOUNT_ID) values (3, '2024-05-22', '2023-08-27', null, 3, 3);
insert into BOOK_RESERVATION (ID, RESERVATION_DATE, DUE_DATE, STATUS, BOOK_ID, ACCOUNT_ID) values (4, '2023-12-20', '2023-06-07', null, 4, 4);
insert into BOOK_RESERVATION (ID, RESERVATION_DATE, DUE_DATE, STATUS, BOOK_ID, ACCOUNT_ID) values (5, '2024-05-11', '2024-04-08', null, 5, 5);
insert into BOOK_RESERVATION (ID, RESERVATION_DATE, DUE_DATE, STATUS, BOOK_ID, ACCOUNT_ID) values (6, '2024-02-17', '2024-03-31', null, 6, 6);
insert into BOOK_RESERVATION (ID, RESERVATION_DATE, DUE_DATE, STATUS, BOOK_ID, ACCOUNT_ID) values (7, '2023-12-10', '2024-02-07', null, 7, 7);
insert into BOOK_RESERVATION (ID, RESERVATION_DATE, DUE_DATE, STATUS, BOOK_ID, ACCOUNT_ID) values (8, '2024-04-09', '2024-03-14', null, 8, 8);
insert into BOOK_RESERVATION (ID, RESERVATION_DATE, DUE_DATE, STATUS, BOOK_ID, ACCOUNT_ID) values (9, '2024-01-21', '2024-04-03', null, 9, 9);
insert into BOOK_RESERVATION (ID, RESERVATION_DATE, DUE_DATE, STATUS, BOOK_ID, ACCOUNT_ID) values (10, '2024-06-02', '2024-06-01', null, 10, 10);