# Extensions

The extensible MoMap client allows extensions to contribute frontend elements and logic and backend APIs,
so the core MoMap functionality may be extended.

## Overview

An extension is essentially a jar file containing both backend and frontend code,
where an implementations of `AppExtension` returns a list of `Contribution` instances, each holding a contract interface and an implementing object.
These objects are provided to the MoMap backend and may be picked up by other code, e.g.
the route setup code uses implementations of `RouteContribution` and
the code for serving the frontend uses implementations of `FrontendContribution`.

The extension will typically be a combination of Kotlin (and Java) code, for the `AppExtension`, `RouteContribution` and `FrontendContribution` implementations,
and Typescript code, for the frontend. The build system, e.g. Gradle or Maven, must build a jar that combines both frontend and backend code,
that can be run together with dependencies, including the MoMap backend API with frontend code included as resources.

### Details

An extension is essentially a jar file containing both backend and frontend code,
where an implementations of `AppExtension` is registered in `resources/META-INF/services/imr.hi.mareano.extensions.AppExtension`,
to be picked up by Java's `ServiceLoader` mechanism. Its `contributions` method returns a list of `Contribution` instances,
which hold a contract interface and an implementing object. These objects are provided to the MoMap backend and may be picked up by other code, e.g.
the route setup code uses implementations of `RouteContribution` and the code for serving the frontend uses implementations of `FrontendContribution`.

The extension will typically be a combination of Kotlin (and Java) code, for the `AppExtension`, `RouteContribution` and `FrontendContribution` implementations,
and Typescript code, for the frontend. The build system, e.g. Gradle or Maven, must build a jar that can be run together with dependencies,
including the MoMap backend API with frontend code included as resources.
