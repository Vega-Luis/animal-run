# animal-run

**Animal Run** is a small **JavaFX** desktop app that simulates an animal race to explore **Java threads**.  
You can configure each animal’s **step size** (how far it moves each tick) and **cadence** (how often it moves), then start the race and view the final ranking.

This project is built with **Maven** and uses the **OpenJFX (JavaFX) Maven plugin** to run the application easily on most computers.

---

## Features

- **JavaFX GUI** with two screens:
  - Welcome screen ("Animal Run")
  - Race configuration screen
- **Race simulation using threads**
  - Each animal runs on its own `Thread`
  - A separate monitor thread waits for all animals to finish
- **Configurable animals**
  - Camel / Ostrich / Deer
  - Adjustable:
    - **Step**: how much the animal advances per movement
    - **Cadence**: delay in milliseconds between movements (lower = faster)
- **Randomness factor**
  - Sometimes an animal’s step is randomly modified to make outcomes less predictable
- **Results popup**
  - Shows winner + ordered results (time in milliseconds)

---

## Tech Stack

- **Java 11**
- **Maven**
- **JavaFX (OpenJFX)**
  - `javafx-controls`
  - `javafx-fxml`

---

## Requirements

Install these on any computer before running:

1. **Java Development Kit (JDK) 11**
   - The project is configured to compile with **Java 11** (`maven.compiler.source/target` and `release` are set to 11).
2. **Apache Maven**
   - Used to download dependencies and run the JavaFX app.

Optional (recommended):
- **Git** (to clone the repository)

---

## How to Run (Works on Windows / macOS / Linux)

### 1) Clone the repository
```bash
git clone https://github.com/Vega-Luis/animal-run.git
cd animal-run
```

### 2) Run with Maven + JavaFX plugin
```bash
mvn clean javafx:run
```

This uses the configured JavaFX main class:
- `com.vegaluis.animalrun.App`

---

## How to Use the App

1. Launch the app (`mvn clean javafx:run`)
2. Click **“Continuar”**
3. Configure each animal:
   - **Paso (Step)**
   - **Cadencia (Cadence)**
4. Click **“Iniciar carrera”**
5. Wait until all animals reach the finish line
6. A dialog will show the winner and the times

---

## Project Structure (high level)

- `pom.xml` – Maven build + JavaFX plugin configuration
- `src/main/java/`
  - `com.vegaluis.animalrun.App` – JavaFX `Application` entrypoint (loads `primary.fxml`)
  - `PrimaryController` – switches to the race screen
  - `SecondaryController` – race logic (threads + UI updates)
  - `module-info.java` – Java module definition
- `src/main/resources/`
  - `primary.fxml` – welcome UI
  - `secondary.fxml` – configuration + race UI
  - sprite images are expected under:
    - `/com/vegaluis/animalrun/sprites/` (camel/ostrich/deer)

---

## Troubleshooting

### `mvn: command not found`
Maven is not installed or not added to your PATH. Install Maven and try again.

### Java version issues
If you get compilation errors related to source/target versions, confirm:
```bash
java -version
```
You should be on **Java 11**.

### JavaFX / graphics issues (Linux)
Some Linux distributions require extra system libraries for JavaFX (depending on your setup/desktop environment). If the app starts but fails to display a window, install the common OpenJFX-related dependencies for your distro and try again.

---

## License

This repository includes a `LICENSE` file. See it for usage rights.