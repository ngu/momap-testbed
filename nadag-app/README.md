# Nadag app

Example extensions for MoMap backend API and frontend.

## Extensible MoMap

Briefly, extensibiløity in the MoMap client is based on Java's `ServiceLoader` mechanism for discovering interface implementations in the classpath and
a couple of interfaces related to extensibility:
- `AppExtension` - interface that must be implemented by an extension (plugin) and that provides a set of `Contribution` objects
- `Contribution` - an object implementing a contract interface and that will be made available through Koin (Kotin DI mechanism)

On startup, the extensible MoMap will discover all available `AppExtension` implementations and install their `Contribution` objects into Koin.
In strategic places in the backend, Koin objects will be given the chance to contribute logic (run code), e.g.
in `Routing`, a contribution implementing `RoutingContribution` may install additional routes for extra backend endpoints.

To make the frontend extensible in a similar way, the MoMaps frontend is packaged as resources in MoMap's backend and served as by it.
When serving `index.html` (as main entry point), extra frontend code from extensions (by means of FrontendContribution objects) are injected
as module scripts. In addition, and similiar to how Koin is used to lookup objects,
the frontend code may add `FrontendContributionSlot` placeholders, where React elements from contributed frontend code may be injected.

Currently, the right menu is made extensible in this way, and this MoMap testbed extension provides a location search tool that uses
a corresponding backend API for searching for geo-locations.

## Extension

An extension is essentially a jar file including both backend and frontend code, where one or more implementations of `AppExtension`
is registered in `resources/META-INF/services/imr.hi.mareano.extensions.AppExtension`. The(se) implementation(s) in turn
contributes routes and frontend code.

This extension has one `AppExtension` implementation, that contributes an `api/location-search` endpoint and
a right menu button and panel for searching for geo-locations. Kartverket's stedsnavn and adresse APIs are currently supported
(there is code here for Nadag project search, too, but it's currently not used).

## Setup

Make a folder for your map application and clone (or download and upzip) the
[mareano-api](https://git.imr.no/digital-utvikling/mareano/mareano-api) and
[mareano-frontend](https://git.imr.no/digital-utvikling/mareano/mareano-frontend) repositories into it,
as well as [this MoMap testbed](https://github.com/ngu/momap-testbed) repository.

Export necessary variables, at least

- VITE_API_BASE_URL (=http://localhost:8080)
- VITE_AUTH_MODE (=local-mock)
- VITE_ADMIN_ROLE (=Mareano.Admin)
- VITE_BRAND (=ngu)

## Build and run

You may need to run `npm install` in the `mareano-frontend` folder once.

Build the Mareano backend and frontend modules witb extensibility turned on: in `mareano-api`, run `./gradlew publishExtensibleApp`
This will package the frontend into the backend API jar and install it in the local maven repo, so it can be used as a dependency in an extension module.

In `momap-testbed`, run `mvn package` to build this extension. The resulting artifact will appear in the `target` folder and
the (transitive) dependencies (including the MoMap backend API with the embedded frontend code) in the `target/dependencies` folder.

Launch with `java -jar target/nadag-app-0.0.1-SNAPSHOT.jar` and open `$VITE_API_BASE_URL` in browser.
This will ensure your extension's code and dependencies are all on the classpath, so the extension is picked up by the MoMap application.
