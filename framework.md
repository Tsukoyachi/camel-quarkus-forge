# Dynamic Camel Routes Framework

## 🎯 Overview

This framework allows creating Apache Camel routes dynamically from YAML configuration files, without writing Java code. It provides a declarative and flexible approach for configuring integrations.

## 🏗️ Framework Architecture

### Class Structure

```
src/main/java/com/tsukoyachi/project/
├── TestRoute.java                      # Main RouteBuilder with CDI (@ApplicationScoped)
├── RouteConfigurationService.java      # YAML loading service (@ApplicationScoped)
├── ComponentFactory.java              # Factory for creating components (@ApplicationScoped)
├── RouteConfiguration.java            # Route configuration model
├── ComponentConfiguration.java        # Component configuration (type + properties)
├── ProcessorConfiguration.java        # Processor configuration
├── Consumer.java                      # Interface for consumers
├── Producer.java                      # Interface for producers
├── Processors.java                   # Transformation processors (Transform, Filter, Custom)
└── components/                        # Package organized by component
    ├── file/
    │   ├── FileConsumer.java          # File consumer with dynamic URL encoding
    │   └── FileProducer.java          # File producer with dynamic URL encoding
    ├── direct/
    │   ├── DirectConsumer.java        # Direct consumer (direct:name)
    │   └── DirectProducer.java        # Direct producer (direct:name)
    ├── timer/
    │   └── TimerConsumer.java         # Timer consumer (timer:name?period=X)
    └── log/
        └── LogProducer.java           # Log producer (log:name)
```

### Configuration Files

```
src/main/resources/
└── routes.yml                        # YAML route configuration
```

### Maven Dependencies

```xml
<dependencies>
    <!-- Camel Quarkus Components -->
    <dependency>
        <groupId>org.apache.camel.quarkus</groupId>
        <artifactId>camel-quarkus-file</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.camel.quarkus</groupId>
        <artifactId>camel-quarkus-direct</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.camel.quarkus</groupId>
        <artifactId>camel-quarkus-timer</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.camel.quarkus</groupId>
        <artifactId>camel-quarkus-log</artifactId>
    </dependency>
    
    <!-- Framework Dependencies -->
    <dependency>
        <groupId>org.yaml</groupId>
        <artifactId>snakeyaml</artifactId>
    </dependency>
</dependencies>
```

## 🔧 Framework Components

### 1. TestRoute (Entry Point)

**Location**: `com.tsukoyachi.project.TestRoute`

**Role**: Main RouteBuilder that loads and dynamically instantiates routes from YAML.

```java
@ApplicationScoped
public class TestRoute extends RouteBuilder {
    @Inject RouteConfigurationService routeConfigService;
    @Inject ComponentFactory componentFactory;
    
    @Override
    public void configure() throws Exception {
        // Load configurations from YAML
        List<RouteConfiguration> routes = routeConfigService.loadRouteConfigurations();
        
        // Create routes dynamically
        for (RouteConfiguration routeConfig : routes) {
            createDynamicRoute(routeConfig);
        }
    }
}
```

**Features**:
- ✅ CDI Bean (`@ApplicationScoped`)
- ✅ Automatic dependency injection with `@Inject`
- ✅ Dynamic route creation via `ComponentFactory`

### 2. RouteConfigurationService (YAML Loader)

**Location**: `com.tsukoyachi.project.RouteConfigurationService`

**Role**: Loads and parses route configurations from `routes.yml`.

```java
@ApplicationScoped
public class RouteConfigurationService {
    public List<RouteConfiguration> loadRouteConfigurations() {
        // Load routes.yml from classpath
        // Parse with SnakeYAML
        // Return List<RouteConfiguration>
    }
}
```

### 3. ComponentFactory (Factory Pattern)

**Location**: `com.tsukoyachi.project.ComponentFactory`

**Role**: Dynamically creates components by type using the new packages.

```java
@ApplicationScoped
public class ComponentFactory {
    public Consumer createConsumer(ComponentConfiguration config) {
        return switch (config.getType().toLowerCase()) {
            case "file" -> new components.file.FileConsumer(config);
            case "direct" -> new components.direct.DirectConsumer(config);
            case "timer" -> new components.timer.TimerConsumer(config);
            default -> throw new IllegalArgumentException("Unsupported type: " + config.getType());
        };
    }
    
    public Producer createProducer(ComponentConfiguration config) {
        return switch (config.getType().toLowerCase()) {
            case "file" -> new components.file.FileProducer(config);
            case "direct" -> new components.direct.DirectProducer(config);
            case "log" -> new components.log.LogProducer(config);
            default -> throw new IllegalArgumentException("Unsupported type: " + config.getType());
        };
    }
}
```

### 4. Configuration Models

#### RouteConfiguration
```java
public class RouteConfiguration {
    private String id;                                    // Unique route ID
    private ComponentConfiguration source;                 // Consumer configuration
    private ComponentConfiguration destination;           // Producer configuration
    private List<ProcessorConfiguration> processors;     // Optional processors
}
```

#### ComponentConfiguration
```java
public class ComponentConfiguration {
    private String type;                    // Type: "file", "direct", "timer", "log"
    private Map<String, Object> properties; // Component-specific properties
}
```

### 5. Components by Package

#### 📁 components.file

**FileConsumer** & **FileProducer**: 
- **Automatic URL encoding** of parameters (★ NEW)
- **Dynamic support** for all Camel File properties
- **Validation** of required `path` parameter

```java
// Example of dynamically generated endpoint
"file:src/input?delete=true&include=%2A%5C.json&recursive=true"
```

#### 📁 components.direct

**DirectConsumer** & **DirectProducer**:
- Direct endpoints for internal communication
- Required `name` property

#### 📁 components.timer  

**TimerConsumer**:
- Periodic trigger
- Properties: `name` (required), `period` (default: 5000ms)

#### 📁 components.log

**LogProducer**:
- Output to logs
- Property `name` (default: "log")

## 📝 YAML Configuration

### Current routes.yml structure

```yaml
routes:
  # File-to-File route with JSON filtering
  - id: "file-to-file-json-only-route"
    source:
      type: "file"
      properties:
        path: "src/input"
        delete: true
        include: .*\.json              # ★ Regex for JSON files
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
        period: 2000                   # ★ Every 2 seconds
    destination:
      type: "direct"
      properties:
        name: "timer-event"

  # Direct to Log route (chain continuation)
  - id: "direct-to-log-route"
    source:
      type: "direct"
      properties:
        name: "timer-event"           # ★ Same name as previous destination
    destination:
      type: "log"
      properties:
        name: "timer-to-log Logger"
```

### ★ Important new features

1. **Automatic URL encoding**: Special characters are automatically encoded
2. **Organized packages**: Each component in its own package
3. **Route chaining**: Using `direct` to connect routes
4. **Regex support**: For `include`, use regular expressions

## 🔄 Execution Flow

1. **Startup**: Quarkus starts the application
2. **CDI Injection**: `TestRoute` receives `RouteConfigurationService` and `ComponentFactory`
3. **YAML Loading**: Parse `routes.yml` with SnakeYAML
4. **Component Creation**: `ComponentFactory` instantiates from `components.*` packages
5. **Dynamic Endpoints**: Generate Camel URIs with URL encoding
6. **Route Configuration**: Camel configures the 3 defined routes
7. **Execution**: 
   - Route 1: Monitors JSON files in `src/input`
   - Route 2: Timer sends to direct endpoint
   - Route 3: Direct endpoint displays in logs

## ✅ Framework Benefits

### 🚀 Technical Flexibility
- **Automatic encoding**: Transparent handling of special characters
- **Organized packages**: Modular `components.{type}` structure
- **Extensibility**: New component = new package
- **Type safety**: `Consumer`/`Producer` interfaces ensure compatibility

### 📦 Advanced Modularity
- **Domain separation**: file/, direct/, timer/, log/
- **Centralized factory**: Single point for component creation
- **Externalized configuration**: YAML decoupled from Java code

### 🔧 Operational Ease  
- **Hot reload**: YAML modifications without recompilation
- **Debugging**: Automatic logging of generated endpoints
- **Validation**: Clear errors for invalid configuration

## 🌟 Extension Examples

### Adding an HTTP component

1. **Create the package**:
```
components/http/
├── HttpConsumer.java
└── HttpProducer.java
```

2. **Implement HttpConsumer**:
```java
package com.tsukoyachi.project.components.http;

public class HttpConsumer implements Consumer {
    @Override
    public String getEndpoint() {
        String host = (String) config.getProperties().get("host");
        Integer port = (Integer) config.getProperties().get("port");
        return "http://" + host + ":" + port;
    }
}
```

3. **Add to ComponentFactory**:
```java
case "http" -> new components.http.HttpConsumer(config);
```

4. **Add Maven dependency**:
```xml
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-http</artifactId>
</dependency>
```

5. **Use in YAML**:
```yaml
source:
  type: "http"
  properties:
    host: "localhost"
    port: 8080
    path: "/api/webhook"
```

## 📊 YAML → Camel Mapping

### Transformation examples

| YAML Configuration | Generated Camel Endpoint |
|---|---|
| `file: {path: "input", delete: true}` | `file:input?delete=true` |
| `timer: {name: "test", period: 1000}` | `timer:test?period=1000` |
| `direct: {name: "process"}` | `direct:process` |
| `log: {name: "audit"}` | `log:audit` |

### ★ Special character handling

| YAML Value | Encoded Value | Final Endpoint |
|---|---|---|
| `include: "*.json"` | `include=%2A.json` | `file:input?include=%2A.json` |
| `name: "test & debug"` | `name=test%20%26%20debug` | `log:test%20%26%20debug` |

---

**This framework now offers an enterprise-ready architecture with organized packages, automatic encoding, and full support for route chaining via direct endpoints.** 🎊