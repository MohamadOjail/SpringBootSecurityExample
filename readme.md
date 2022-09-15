![](https://img.shields.io/badge/Spring%20Boot-2.7.3-brightgreen) ![](https://img.shields.io/badge/Java-11%20%3E-yellow)

# Spring boot security with JWT example
> this example is **NOT** based on [OAuth2](https://oauth.net/2/ "OAuth2")

------------


#### important to know
> - In this example I configure web-security with the new component-based configuration.
> - Please note that the `WebSecurityConfigurerAdapter` has been deprecated since Spring Boot v `2.7`
> - I did not include any tests at the moment, as the intention was to demonstrate how to configure web-security with JWT.

------------

#### About this example
This simple project is built with the idea in mind that we will be storing some users with different authorities in a database and use them to allow/deny access to some end points accordingly.
**the project has:**
- a simple Rest API to reach end-points.
- a simple Entity POJO with it's services and repositories.
- a security layer.

------------

### Dependencies:
Nothing fancy some basic dependencies:

|Dependency|artifactId|version|
| ------------ | ------------ | ------------ |
| Spring Data JPA  | spring-boot-starter-data-jpa  |  |
| Spring Security  | spring-boot-starter-security  |   |
| Spring Web  | spring-boot-starter-web  |   |
| MySQL Connector  | mysql-connector-java  |   |
| lombok  | lombok  |   |
| Java Faker  | javafaker  | 1.0.2  |
| auth0 JWT  | java-jwt  | 4.0.0  |

------------




## The workflow
- Each user will have a list of roles wich will allow/deny access to end points accordingly.
- authenticating will be user-name / password based.
- authorization will be based on user's authorities using the **access token**.
- the process will generate an **access token** and a **refresh token**
- The user will be able to log in and get the tokens needed.
- both acc/refresh tokens will have expiration time.

# Next:?
next example will utilize the OAuth2 soon.
> You can visit my [portfolio](https://ojail.online/ "portfolio") to know more about me.