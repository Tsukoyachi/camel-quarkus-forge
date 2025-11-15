# Dynamic Camel Routes Framework

A framework for creating Apache Camel routes dynamically from YAML configuration files, without writing Java code.

## 🚀 Features

- **YAML-based Configuration**: Define routes declaratively without Java coding
- **Organized Package Structure**: Components organized in `components.{type}` packages
- **Automatic URL Encoding**: Transparent handling of special characters
- **Route Chaining**: Connect routes using `direct` endpoints
- **CDI Integration**: Full dependency injection support with Quarkus
- **Extensible**: Easy to add new component types

## 📦 Supported Components

### Consumers (Sources)
- **File**: Monitor directories for files (`file:path?delete=true&include=.*\.json`)
- **Direct**: Internal routing endpoints (`direct:name`)
- **Timer**: Periodic triggers (`timer:name?period=5000`)

### Producers (Destinations)
- **File**: Write to directories (`file:path`)
- **Direct**: Send to internal endpoints (`direct:name`)
- **Log**: Output to logs (`log:name`)

## ⚙️ Configuration

Routes are configured in `src/main/resources/routes.yml`:

```yaml
routes:
  # File processing with JSON filtering
  - id: "file-to-file-json-only-route"
    source:
      type: "file"
      properties:
        path: "src/input"
        delete: true
        include: .*\.json              # Regex for JSON files only
    destination:
      type: "file"
      properties:
        path: "src/output"

  # Timer to Direct route (chaining)
  - id: "timer-to-direct-route"
    source:
      type: "timer"
      properties:
        name: "timer"
        period: 2000                   # Every 2 seconds
    destination:
      type: "direct"
      properties:
        name: "timer-event"

  # Direct to Log route (chain continuation)
  - id: "direct-to-log-route"
    source:
      type: "direct"
      properties:
        name: "timer-event"           # Same name as previous destination
    destination:
      type: "log"
      properties:
        name: "timer-to-log Logger"
```

## 🏗️ Architecture

```
src/main/java/com/tsukoyachi/project/
├── TestRoute.java                      # Main RouteBuilder (@ApplicationScoped)
├── RouteConfigurationService.java      # YAML loading service
├── ComponentFactory.java              # Component factory
└── components/                         # Organized by component type
    ├── file/
    │   ├── FileConsumer.java
    │   └── FileProducer.java
    ├── direct/
    │   ├── DirectConsumer.java
    │   └── DirectProducer.java
    ├── timer/
    │   └── TimerConsumer.java
    └── log/
        └── LogProducer.java
```

## 🚀 Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## 🎯 How it works

1. **YAML Loading**: `RouteConfigurationService` parses `routes.yml`
2. **Dynamic Creation**: `ComponentFactory` creates components from organized packages
3. **URL Encoding**: Special characters are automatically encoded
4. **Route Building**: `TestRoute` constructs Camel routes dynamically
5. **Execution**: Routes process messages based on YAML configuration

## ✨ Adding New Components

1. **Create package**: `components/{type}/`
2. **Implement interfaces**: `Consumer` and/or `Producer`
3. **Update factory**: Add cases in `ComponentFactory`
4. **Add dependency**: Include Camel component in `pom.xml`
5. **Use in YAML**: Configure routes with new type

For detailed documentation, see [framework.md](framework.md).

## 📦 Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it's not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## 🏔️ Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

## Related Guides

- Camel File ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/file.html)): Read and write files
- Camel Core ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/core.html)): Camel core functionality and basic Camel languages
- Camel Jackson ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/jackson.html)): Marshal POJOs to JSON and back using Jackson
- Camel Direct ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/direct.html)): Internal routing endpoints
- Camel Timer ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/timer.html)): Periodic triggers
- Camel Log ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/log.html)): Logging output