# Camel Quarkus Forge

Dynamic framework for creating Apache Camel routes from YAML configuration without writing Java code.

## Features

- YAML-based route configuration
- CDI dependency injection with automatic component discovery
- Automatic URL encoding for special characters
- Route chaining via direct endpoints
- Zero-configuration component extensions
- Hot reload for development

## Supported Components

**Camel Consumers**: File, Direct, Timer, Kafka  
**Camel Producers**: File, Direct, Log, Kafka

## Quick Start

**Development mode:**
```bash
./mvnw quarkus:dev
```

**Build application:**
```bash
./mvnw package
```

**Run application:**
```bash
java -jar target/quarkus-app/quarkus-run.jar
```

## Configuration Example

Configure routes in `src/main/resources/application.yml`:

```yaml
camel:
  routes:
    definitions:
      - id: "file-to-file-route"
        source:
          type: "file"
          properties:
            path: "src/input"
            delete: true
            include: .*\.json
        destination:
          type: "file"
          properties:
            path: "src/output"

      - id: "timer-to-log-route"
        source:
          type: "timer"
          properties:
            name: "scheduler"
            period: 5000
        destination:
          type: "log"
          properties:
            name: "scheduler-log"
```

The above configuration creates two routes: one monitoring JSON files and another logging scheduled events.

## Tech Stack

- **Apache Camel Quarkus 3.29.3** - Integration framework
- **Quarkus 3.29.3** - Cloud-native Java framework
- **CDI** - Dependency injection
- **Java 21** - Programming language

---

**Author**: [Tsukoyachi](https://github.com/Tsukoyachi)