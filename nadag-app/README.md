# Nadag app

Example extensions for MoMap backend API and frontend.

## Setup

Export necessary variables, at least

- VITE_API_BASE_URL (=http://localhost:8080)
- VITE_AUTH_MODE (=local-mock)
- VITE_ADMIN_ROLE (=Mareano.Admin)
- VITE_BRAND (=ngu)

- NADAG_DB_URL

## Build and run

Build the Mareano backend and frontend modules witb extensibility turned on:

- in `mareano-api`, run `./gradlew publishExtensibleApp`

Then run `mvn package` to build your own extenstion.

Launch with `java -jar target/nadag-app-0.0.1-SNAPSHOT.jar` and open $VITE_API_BASE_URL in browser.
