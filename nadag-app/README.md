# Nadag app

Example extensions for MoMap backend API and frontend.

## Extensible MoMap

Briefly, extensibility in the MoMap client is based on the following interfaces and classes:
- `AppExtension` - interface that must be implemented by an extension (plugin) and that provides a set of `Contribution` objects
- `Contribution` - an object holding a contract interface and an implementing object, that will be made available through Koin (Kotlin DI mechanism)
- `RoutingContribution` - an object that installs one or more routes/endpoints that can be used by frontend code
- `FrontendContribution` - an object that returns info for frontend code that provides React elements and logic, that may be served as resources

On startup, the extensible MoMap will discover all available `AppExtension` implementations and
make their `Contribution` objects available to the backend. In strategic places in the backend,
the contributed objects will be given the chance to run code, e.g. in `Routing`, a contribution implementing `RoutingContribution`
may install additional routes for extra backend endpoints.

To make the frontend extensible in a similar way, the MoMap frontend is served by MoMap's backend (instead of the Node runtime).
When serving `index.html` (as the main entry point), extra frontend code from extensions are injected
as (module) scripts. These may be picked up by `FrontendContributionSlot` placeholders, where React elements may be injected.

For more details, See [>>>](./extensions.md) .

## Extension

An extension is essentially a jar file including both backend and frontend code, where an implementations of `AppExtension`
contribute routes (`RouteContribution`) and frontend code (`FrontendContribution`).

This extension has one `AppExtension` implementation, that contributes an `api/location-search` endpoint and
a right menu button and panel for searching for geo-locations.
Kartverket's stedsnavn and adresse REST APIs are currently supported
(there is code here for Nadag project search, too, but it's currently not used, since it requires DB credentials).

## Setup for MoMap testbed

Make a folder for your map application and clone (or download and upzip) the
[mareano-api](https://git.imr.no/digital-utvikling/mareano/mareano-api) and
[mareano-frontend](https://git.imr.no/digital-utvikling/mareano/mareano-frontend) repositories into it,
as well as [this MoMap testbed](https://github.com/ngu/momap-testbed) repository.

Install necessary languages and tools:

- Java 21 and maven
- pnpm and typescript compiler

Export necessary variables, at least

- `VITE_API_BASE_URL`=http://localhost:8080
- `VITE_AUTH_MODE`=local-mock
- `VITE_ADMIN_ROLE`=Mareano.Admin
- `VITE_BRAND`=ngu

Trick: Put them in a `.env` file, and use the following command sequence:

```sh
set -a # turn auto-export on
source .env # 'run' the .env file, i.e. set the variables, implicitly exporting them
set +a # turn auto-export off
```

## Build and run

You may need to run `npm install` in the `mareano-frontend` folder once.

Build the Mareano backend and frontend modules with extensibility turned on: in `mareano-api`, run `./gradlew publishExtensibleApp`
This will package the frontend into the backend API jar and install it in the local maven repo, so it can be used as a dependency in an extension module. If you need to share this combined frontend+backend artifact in a more central maven repo, you can use the following commands
(the `cp` command is needed because mvn refuses to deploy directly from `.m2/repository/...`):

```sh
cp $HOME/.m2/repository/imr/hi/mareano-kartklient-api/0.0.1-SNAPSHOT/mareano-kartklient-api-0.0.1-SNAPSHOT.jar target
mvn org.apache.maven.plugins:maven-deploy-plugin:3.1.4:deploy-file \
  -Durl=https://maven.pkg.github.com/<github-owner>/<github-repo> \
  -DrepositoryId=<github-server-id> \
  -Dfile=target/mareano-kartklient-api-0.0.1-SNAPSHOT.jar \
  -DpomFile=$HOME/.m2/repository/imr/hi/mareano-kartklient-api/0.0.1-SNAPSHOT/mareano-kartklient-api-0.0.1-SNAPSHOT.pom
```

As the `publishExtensibleApp` task depends in build tasks for both frontend and backend API,
it is the only command needed if either MoMap frontend and API have changed.

In `momap-testbed`, run `mvn package` to build this extension. The resulting artifact will appear in the `target` folder and
the (transitive) dependencies (including the MoMap backend API with the embedded frontend code) in the `target/dependencies` folder.

Launch with `java -jar target/nadag-app-0.0.1-SNAPSHOT.jar` and open `$VITE_API_BASE_URL` in browser.
This will ensure your extension's code and dependencies are all on the classpath, so the extension is picked up by the MoMap application.
