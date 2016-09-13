#Description
Captive portal web application which provides a lending web page for the unauthenticated users which tries to access the network.
#Problem Statement
To develop a captive portal web application running on authentication server.
#Solutiuon Approach
Captive portal web application provides a lending web page or more specifically a login page for the unauthenticated users which tries to access the network. This application is using Mysql to store the authenticated users. The Controller queries the database to know if a user is authenticated or not. For authenticated user, controller installs the flows on switch and allows network access. Any HTTP request from the unauthenticatd user is redirected to authentication server and user details are saved in database. A login page is returned to the user provided by this application. On successful login, user details are updated and a welcome page is shown to the user.  There are three user roles associated with users accessing the network: admin, normal user, guest. An admin can add users, view user details and update flows. A guest is allowed limited bandwidth and normal user is allowed higher bandwidth in the network. Spring boot, Spring data jpa, REST, Mysql, Flyway and Thymeleaf has been used to develop the application.
#Deployment
Create a schema with name captiveportal.
```
CREATE SCHEMA `captiveportal` ;
```
Clone the captive portal web application from our github and start the application:
```
git clone https://github.com/nkkize/captiveportal.git
cd captiveportal
mvn clean install
cd captiveportal-web/target
java -jar captiveportal-web-0.0.1-SNAPSHOT.jar
```
This will start the captiveportal web application and 2 users will be added in database. Also the root node will be added which is used to communicate between base machine and mininet topology. Open your favorite browser and hit
```
http://localhost:9090
```
A login page is shown. Login using username/password:admin/admin123