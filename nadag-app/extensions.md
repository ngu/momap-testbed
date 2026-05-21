# Extensions

The extensible MoMap client allows extensions to contribute frontend elements and logic and backend APIs,
so the core MoMap functionality may be extended.

## Overview

An extension is essentially a jar file containing both backend and frontend code,
where an implementations of `AppExtension` returns a list of `Contribution` instances, each holding a _contract_ interface and an implementing object.
These objects are provided to the MoMap backend and may be picked up by other code, e.g.
the route setup code uses implementations of (the contract interface) `RouteContribution` and
the code for serving the frontend uses implementations of `FrontendContribution`.

The extension will typically be a combination of Kotlin (and Java) code, for the `AppExtension`, `RouteContribution` and `FrontendContribution` implementations,
and Typescript code, for the frontend. The build system, e.g. Gradle or Maven, must build a jar that combines both frontend and backend code,
that can be run together with dependencies, including the MoMap backend and frontend combo.

## Details

The MoMap application discovers all `AppExtension` implementations in jars in the classpath by means of Java's `ServiceLoader` mechanism.
This requires that the `AppExtension` implementation classes are listed in the 
`/META-INF/services/imr.hi.mareano.extensions.AppExtension` file in the jar.

`AppExtension`'s `contributions` method returns a list of `Contribution` instances, which hold a contract interface and an implementing object.
These pairs are registered in the Koin dependency injection framework and may be picked up by other code, based on the contract interface.
E.g. the following code in MoMap's `Routing.kt` file ensures implementations of `RouteContribution` get a chance to register new routes:

```kotlin
getKoin().getAll<RouteContribution>().forEach {
    it.install(this, appContext)
}
```

### RouteContribution

This interface is used for registering additional routes/endpoints.
The `install` method registers the routes and handlers, while
the `pathPrefixes` method returns the list of all registered route paths.

Example:

```kotlin
private class ApiRoutes(...) : RouteContribution {

    override fun pathPrefixes(): List<String> = listOf("api/location-search")

    override fun install(route: Route, context: AppContext) {
        route.route("api") {
            get("location-search") {
                val q = call.request.queryParameters["q"]
                ...
            }
        }
    }
```

### FrontendContribution

This interface provides the mapping from request path to resource path in the jar,
to allow the frontend to request the extension's frontend code modules.

Example:

```kotlin
class FrontendScripts : FrontendContribution {
    override fun scripts(context: AppContext): List<FrontendModuleScript> = listOf(
        FrontendModuleScript(
            scriptPath = "/extensions/nadag/nadag-extension.js",
            resourcePath = "frontend-extensions/nadag/nadag-extension.js",
        ),
    )
}
```

Of course, this depends on how frontend code is packaged in the jar, and must be aligned with the extension's build.

### Building the extension

There are several issues the build must handle:

- building the frontend code
- building the backend code
- embedding the frontend and backend code in a common jar
- ensuring there is a (simple) way to launch the application as a whole

The last point can be handled as a separate build (in a different module), but
you can also rig the extension's jar so it can launch itself with the core MoMap client.

The testbed's extension:

The frontend is compiled and bundled with **pnpm** and **vite**.
The frontend build is triggered from **maven** by linking an execution of the exec plugin
to the `generate-resources` phase.

```pom.xml
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.5.0</version>
                <executions>
                    <execution>
                        <id>build-frontend-extension</id>
                        <phase>generate-resources</phase>
                        <goals>
                            <goal>exec</goal>
                        </goals>
                        <configuration>
                            <workingDirectory>${project.basedir}/frontend</workingDirectory>
                            <executable>pnpm</executable>
                            <arguments>
                                <argument>build</argument>
                            </arguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
```

**maven** compiles the backend (Kotlin and Java), and then copies the frontend bundle into the resource tree,
so it ends up in the jar at the path corresponding to the `FrontendContribution`:

```pom.xml
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <version>3.3.1</version>
                <executions>
                    <execution>
                        <id>copy-frontend-extension-assets</id>
                        <phase>process-resources</phase>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.outputDirectory}/frontend-extensions/nadag</outputDirectory>
                            <resources>
                                <resource>
                                    <directory>${project.basedir}/frontend/dist</directory>
                                    <filtering>false</filtering>
                                </resource>
                            </resources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
```

The last point is handled by copying all dependencies to the `target/dependencies` folder and
configuring the jar's classpath accordingly:


```
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>3.8.1</version>
                <executions>
                    <execution>
                        <id>copy-runtime-dependencies</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <includeScope>runtime</includeScope>
                            <outputDirectory>${project.build.directory}/dependencies</outputDirectory>
                            <excludeTransitive>false</excludeTransitive>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.4.2</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>imr.hi.nadag.AppLauncherKt</mainClass>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>dependencies/</classpathPrefix>
                            <useUniqueVersions>false</useUniqueVersions>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
```

The `mainClass` manifest property is also set to make the jar runnable with `java -jar <jarfile>`

It's simple to create a docker file for MoMap with extension(s), just copy the jar and the dependencies folder, and
make sure the `java -jar` command is run in the correct folder:

```Dockerfile
...
# Copy the application's dependencies to the container,
# as provided by maven-dependency-plugin entry in pom.xml
COPY /target/dependencies/*.jar ./dependencies/
# Copy the application's jar to the container
COPY /target/nadag-app-0.0.1-SNAPSHOT.jar ./

...

# Set the command to run the application
ENTRYPOINT ["java", "-jar", "nadag-app-0.0.1-SNAPSHOT.jar"]
```
