## Problem Statement

An underwater volcano formed a new small island in the Pacific Ocean last month. All the conditions on the island seems perfect and it was
decided to open it up for the general public to experience the pristine uncharted territory.

The island is big enough to host a single campsite so everybody is very excited to visit. In order to regulate the number of people on the island, it
was decided to come up with an online web application to manage the reservations. You are responsible for **design and development of a REST
API** service that will manage the campsite reservations.

To streamline the reservations a few constraints need to be in place 
 * The campsite will be **free** for all.
 * The campsite can be reserved for **max 3 days**.
 * The campsite can be reserved minimum **1 day(s) ahead of arrival** and up to **1 month in advance**.
 * Reservations can be **cancelled anytime**.

For sake of simplicity assume the check-in & check-out time is 12:00 AM

### System Requirements
* The users will need to find out when the campsite is available. So the system should expose an API to provide information of the availability of the campsite for a given date range with the default being 1 month.
* Provide an end point for reserving the campsite. The **user** will provide his/her **email** & **full name** at the time of reserving the campsite along with intended **arrival date** and **departure date**. Return a **unique booking identifier** back to the caller if the reservation is successful. The **unique booking identifier** can be used to **modify** or **cancel** the reservation later on. Provide appropriate end point(s) to allow modification/cancellation of an existing reservation.
* Due to the popularity of the island, there is a high likelihood of multiple users attempting to reserve the campsite for the same/overlapping date(s). Demonstrate with **appropriate test cases** that the system can **gracefully handle concurrent requests** to reserve the campsite.
* Provide **appropriate error messages** to the caller to indicate the error cases.

_In general, the system should be able to **handle large volume of requests for getting the campsite availability**. There are no restrictions on how reservations are stored as as long as system constraints are not violated._

### Solution Highlights
* Solution is based on __Spring Boot webservices__ with __PostgreSQL__ database used for storage.
* A fast _in memory cache_ using __Caffeine__ to track all active reservations. It is also used to reply to queries for reservation _availability_A distributed service can inject distributed memory cache to handle multiple nodes.
* Project follows __Hexagonal Architecture__ guidelines
    * _REST services_, _data repository_, _data cache_ are all _adapters_ injected in the domain. and can be easily changed. Domain use these adapters to fulfil any request
    * Business constraints are enforced through a _validation_ adapter and can be quickly changed if needed.

#### Program execution
Project is managed using _gradle_. In project directory
  * `./gradlew clean` to clean the project
  * `./gradlew build` to build
  * Create the provided schema (`schema.sql` in resource folder) in the database, and update database connection information in configuration file (`application.yaml`).   
  * `./gradlew bootRun` to run the project.
  * API documentation is available at `<host>/swagger-ui/`

#### REST API endpoints 
* __GET__ `<host>/api/v1/booking/availableDates`
  * returns a list of available dates for reservation. It can also optionally accept a `startDate` and `endDate` date.
    Domain manager uses an in-memory cache to speed up responses.
    ```localhost/api/v1/booking/availableDates?startDate=2021-02-25&endDate=2021-05-01```

* __PUT__ `<host>/api/v1/booking/book`
  * creates or updates an existing booking.It requires a `BookingRequest` as request body. `BookingRequest` can have optional `bookingId`  
  ```{
    "bookingId" : "28fe0168-8a1d-414c-a281-d46ebeac9f57"
    "name": "John Doe",
    "email": "johd.doe@yahoo.com",
    "startDate": "2021-03-10",
    "endDate": "2021-03-12"
    }  

* __DELETE__ `<host>/api/v1/booking/<bookingId>`
  * cancels a reservation
  

#### Testing for concurrent bookings
* Domain manager is synchronised when creating/updating bookings. 
* Testing for concurrent updates is added in `BookingManagerImplTest`

#### Error messages
* __REST__ adapter has a `BookingExceptionHandler` which handles and maps the system exceptions to appropriate return codes and messages.