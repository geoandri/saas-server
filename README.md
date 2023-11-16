# Saas Server

An SaaS server API based on Spring boot implementing OIDC server and resource server on the same application.
The list of implemented features includes

- Role based access to endpoints
- User registration and verification flows
- Password reset flow
- Password complexity rules
- Uploading list of users for organization administrators

# How to run locally

- Uncomment the commented lines in `AddClient.java` to create the configuration for the front end client. This needs to be done only the first time that the application runs in order to presist the front end client configuration in the database. After the initial run the lines should be commented out to avoid duplicates in the databse.
- Run the docker-compose.yml file with the following command: `docker-compose up`
- Run the Spring boot application
- For testing it, use a local OIDC client (e.g. https://github.com/geoandri/saas-dummy-client) with credentials username `admin` and password `12345`