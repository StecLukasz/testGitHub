**GitHub Service API**

**Opis**

GitHub Service API to aplikacja Spring Boot, 
która umożliwia użytkownikom pobieranie informacji o nie-forkowanych repozytoriach z GitHuba dla danego użytkownika. 
Aplikacja korzysta z GitHub REST API do pobierania danych.


**Tech Stack**

- Language: Java 21

- Frameworks: Spring Boot 3, Hibernate, Junit

- Libraries: Lombok

- Build tool: Maven

- UI: REST API

**Funkcjonalności**

- Pobieranie listy nie-forkowanych repozytoriów dla określonego użytkownika GitHub.

- Zwracanie szczegółowych informacji o repozytoriach, w tym nazwy, właściciela (login), 
oraz listy branchy wraz z ostatnim commit SHA.

**Wymagania**

- Java 11 lub nowsza

- Maven 3.6 lub nowszy

- Dostęp do Internetu dla połączeń z GitHub API


**Obsługa Błędów**

W przypadku nieznalezienia użytkownika, aplikacja zwróci błąd 404 z odpowiednim komunikatem.

**Uruchomienie**

Po pomyślnej instalacji, uruchom aplikację przy użyciu:

`java -jar target/github-service-api-0.0.1-SNAPSHOT.jar`

**Użycie**

_Pobieranie danych z repozytoriów oraz ich składowych_

Aby pobrać dane dla użytkownika GitHub, wykonaj zapytanie GET na endpoint:

`GET /api/users/{username}/repos`

Zastąp {username} nazwą użytkownika GitHub.