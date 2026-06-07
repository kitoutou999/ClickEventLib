# ClickEventLib

A lightweight Java library for [Hytale](https://hytale.com) plugin development that adds left/right click event detection on items and entities.

## Features

- Detect left and right click events on items held by players
- Register multiple listeners via a simple registry pattern
- Supports both `.jar` integration and direct source inclusion

> **Note:** Click events require the player to hold an item that has a registered interaction. Raw mouse input without an item is not supported by this system.

## Installation

### Option 1 - JAR

1. Copy `ClickEventLib-1.0.0.jar` into your plugin's `libs/` folder
2. Add the dependency in `build.gradle.kts`:

```kotlin
dependencies {
    implementation(files("libs/ClickEventLib-1.0.0.jar"))
}
```

### Option 2 - Source

Copy the `src/` folder directly into your project. Rename imports to match your package structure before building.

## Usage

```java
ClickEventRegistry registry = ClickEventRegistry.getInstance();
registry.init(this); // pass your JavaPlugin instance

registry.addListener(event -> {
    if (event.getClickType() == ClickType.RIGHT) {
        // handle right click
    }
});
```

## Example Resources

See `example-resources/` for sample item and interaction config files.

## Requirements

- Hytale server SDK
- Java 17+
- Gradle
