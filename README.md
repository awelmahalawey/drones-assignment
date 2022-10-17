# drones-assignment

Branch called `main` contains the final code of the project.

Project Specs:<br />
- Java 11
- Gradle based project
- Spring boot 2.7.4
- H2 Database

Required Commands:
- Build Command: ./gradlew build
- Run Command: ./gradlew bootrun
- Test Unit Run Command: ./gradlew test 
- After running the application you can view APIs documentation at http://localhost:8080/swagger-ui.html <br />

Assumptions Made Upon Development:
- Drone round trip takes 20 minutes
- Drone battery decreases by 1% for every minute not being IDLE
- Drone battery is recharged 0.02% in one second, which makes it recharge from 0 to 100 in around 1 hour and 20 minutes
