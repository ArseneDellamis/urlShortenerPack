## URL Shortener Service 🚀

### 📌 Description
This is a URL Shortener service implemented using Spring Boot and PostgreSQL. The service provides a REST API that allows clients to create, retrieve, and delete shortened URLs.

Each long URL is assigned a unique alphanumeric ID, which acts as the short link. Clients can optionally set their own custom IDs. Additionally, a time-to-live (TTL)

### SetUp and insttallation

  1.copy this link `https://github.com/ArseneDellamis/urlShortenerPack.git` and use Comand Line(CLI) to clone the project
  2.use postgreSQL for database and create a database called  `abat_url_shortener`
  3.download and istall JDK, and any IDE for compiling java code

after you done Open and Run the project. Hibernatee will create tables for you  

⚠️ **N.B.:** Hibernate is set to `create-drop` that means tables will be create as soon as the program run and destroyed when program exits.

### dependencies used

    1. spring web
    2. spring deev tools
    3. spring data jpa
    4. lombok
    5. postgres
    6.commons-lang3

