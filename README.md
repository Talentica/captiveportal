# captiveportal
Captiveportal application for opendaylight running on authentication server. We are using Mysql as Database to store the authenticated users.
# Desciption
This application is using Mysql to store the authenticated users. The Controller is quering the DB to know if a user is authenticated or not. For authenticated user, controller will install the flows in switch and unathenticatd users are not allowed to access.
