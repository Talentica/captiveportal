#Description
Captive portal web application which provides a lending web page for the unauthenticated users.
#Problem Statement
To develop a captive portal web application running on authentication server.
#Solution Approach
Captive portal web application provides a lending web page or more specifically a login page for the unauthenticated users which tries to access the network. This application is using Mysql to save the users. The Controller queries the database to know if a user is authenticated or not. For authenticated user, controller installs the flows on switch and allows network access. Any HTTP request from the unauthenticated user is redirected to authentication server and user details are saved in database. A login page is returned to the user provided by this application. On successful login, user details are updated and a welcome page is shown to the user.  There are three user roles associated with users accessing the network: admin, user, guest. An admin can add users and view user details. It can also create and update flows and meters on the switches. A guest is allowed limited bandwidth and a normal user is allowed higher bandwidth in the network.
#Deployment
Create a schema with name captiveportal.
```
CREATE SCHEMA `captiveportal` ;
```
Clone the captive portal web application from our github and start the application:
```
git clone https://github.com/Talentica/captiveportal.git
cd captiveportal
mvn clean install
cd captiveportal-web/target
java -jar captiveportal-web-0.0.1-SNAPSHOT.jar
```
This will start the captive portal web application and 2 users will be added in database. Also the root node will be added which is used to communicate between base machine and mininet topology. Open your favorite browser and hit
```
http://localhost:9090
```
A login page is shown. Login using username/password:admin/admin123