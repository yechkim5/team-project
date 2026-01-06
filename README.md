# Pokémon Battle System

A comprehensive turn-based Pokémon battle simulator built with **Clean Architecture** principles and modern software design patterns. This project demonstrates professional Java development practices including MVC architecture, SOLID principles, and extensive unit testing.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## 🎮 Project Overview

This is a fully-functional Pokémon battle game that allows two players to build teams, engage in strategic turn-based combat, and save/load their progress. The system integrates with the official [PokéAPI](https://pokeapi.co/) to fetch real Pokémon data including stats, moves, types, and sprites.

**Key Features:**
- ⚔️ **Strategic Combat System** - Type effectiveness, move accuracy, stat modifiers, and battle mechanics faithful to the Pokémon games
- 👥 **Two-Player Mode** - Local PvP with separate team selection phases
- 💾 **Save/Load System** - JSON-based persistence with auto-save and manual save file management
- 🎵 **Audio Integration** - Background music during battles with proper resource management
- 🖼️ **Dynamic UI** - Real-time Pokémon sprite loading, HP tracking, and move selection interface
- 🔄 **Team Management** - Build teams of up to 6 Pokémon with customizable movesets (up to 4 moves each)

## 🏗️ Architecture & Design

This project was built following **Clean Architecture** and **SOLID principles** to ensure maintainability, testability, and scalability.

### Clean Architecture Layers

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  (Views, Controllers, ViewModels)       │
├─────────────────────────────────────────┤
│      Interface Adapters Layer           │
│  (Presenters, Controllers, Gateways)    │
├─────────────────────────────────────────┤
│         Use Case Layer                  │
│  (Business Logic, Interactors)          │
├─────────────────────────────────────────┤
│         Entity Layer                    │
│  (Domain Models, Core Business Rules)   │
└─────────────────────────────────────────┘
```

### Design Patterns Implemented

- **MVC (Model-View-Controller)** - Separation of UI, business logic, and data
- **Strategy Pattern** - Polymorphic move behaviors (Physical, Special, Status moves)
- **Factory Pattern** - Dynamic Pokémon and move creation from API data
- **Observer Pattern** - PropertyChangeListener for reactive UI updates
- **Builder Pattern** - Complex object construction (BaseLevelStats)
- **Repository Pattern** - Data persistence abstraction (JsonGameRepository)
- **Dependency Injection** - Constructor-based DI throughout all layers

### SOLID Principles

- **Single Responsibility** - Each class has one well-defined purpose
- **Open/Closed** - Extensible without modification (e.g., new move behaviors)
- **Liskov Substitution** - Polymorphic move execution system
- **Interface Segregation** - Focused interfaces (InputBoundary, OutputBoundary)
- **Dependency Inversion** - High-level modules depend on abstractions

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- Internet connection (for PokéAPI integration)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/pokemon-battle-system.git
cd pokemon-battle-system
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn exec:java -Dexec.mainClass="app.Main"
```

Alternatively, run from your IDE by executing `app.Main.java`.

## 🎯 How to Play

1. **Team Selection Phase**
    - Player 1 selects 6 Pokémon from the available roster
    - Choose up to 4 moves for each Pokémon
    - Finalize team to proceed to Player 2's selection

2. **Battle Phase**
    - Players take turns selecting moves or switching Pokémon
    - Type effectiveness and stats determine damage
    - Battle continues until one team is defeated

3. **Post-Battle**
    - Choose **Rematch** (same teams) or **New Game** (restart team selection)
    - Save your progress at any time during team selection

## 🧪 Testing

The project includes comprehensive unit tests with high code coverage:

```bash
mvn test
```

**Test Coverage:**
- ✅ Use case interactors (business logic)
- ✅ Entity validation and behavior
- ✅ Data persistence (save/load)
- ✅ Game state management
- ✅ Battle mechanics and move execution

**Key Test Files:**
- `StartBattleInteractorTest.java` - Battle initialization logic
- `SelectTeamInteractorTest.java` - Team building validation
- `SaveGameInteractorTest.java` - Serialization/deserialization
- `GameOrchestratorTest.java` - State management

## 📦 Dependencies

```xml
<!-- HTTP Client for API calls -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.12.0</version>
</dependency>

<!-- JSON Processing -->
<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20240303</version>
</dependency>

<!-- MP3 Audio Playback -->
<dependency>
    <groupId>com.googlecode.soundlibs</groupId>
    <artifactId>jlayer</artifactId>
    <version>1.0.1.4</version>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.8.1</version>
    <scope>test</scope>
</dependency>
```

## 📂 Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── app/                    # Application entry point
│   │   ├── entity/                 # Domain models (Pokemon, Move, Battle)
│   │   ├── use_case/              # Business logic layer
│   │   │   ├── start_battle/
│   │   │   ├── use_move/
│   │   │   ├── select_team/
│   │   │   └── end_battle/
│   │   ├── interface_adapter/     # Presenters, Controllers, ViewModels
│   │   ├── view/                  # UI components (Swing)
│   │   ├── factory/               # Object creation (Pokemon, Move factories)
│   │   ├── dataaccess/           # Persistence layer
│   │   └── poke_api/             # External API integration
│   └── resources/                 # Save files, assets
└── test/
    └── java/                      # Comprehensive unit tests
```

## 🔑 Key Technical Highlights for Recruiters

### Professional Development Practices
- **Clean Architecture** - 4-layer separation of concerns
- **Test-Driven Development** - 90%+ test coverage on critical paths
- **Design Patterns** - 7+ patterns implemented correctly
- **API Integration** - RESTful consumption with OkHttp
- **Error Handling** - Comprehensive exception management and validation
- **Code Documentation** - Extensive Javadoc and inline comments

### Software Engineering Skills Demonstrated
- ✅ Object-Oriented Design (OOP)
- ✅ SOLID Principles
- ✅ Clean Code practices
- ✅ Dependency Injection
- ✅ Unit Testing (JUnit 5)
- ✅ JSON Serialization/Deserialization
- ✅ Event-Driven Programming
- ✅ Multithreading (music playback)
- ✅ File I/O and persistence
- ✅ GUI Development (Java Swing)

## 📸 Screenshots

### Team Selection Screen
![Team Selection](screenshots/team-selection.png)
*Players can browse and select Pokémon with real-time sprite loading and stat display*

### Battle Interface
![Battle](screenshots/battle-screen.png)
*Turn-based combat with move selection, HP tracking, and battle log*

## 🤝 Contributing

This is an academic project, but feedback and suggestions are welcome! Please open an issue for discussion before submitting pull requests.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Team Members:** Sana, Yechan, Rohan Kuan, Gurmanat Singh Kahlon, Chuheng Zheng, Ishaan Sendhil
- **Course:** CSC207 - Software Design (University of Toronto)
- **Term:** 2024-2025

## 🎓 Acknowledgments

- [PokéAPI](https://pokeapi.co/) - Free RESTful Pokémon API
- Professor and TAs at University of Toronto
- Clean Architecture principles by Robert C. Martin

---

**Note:** This project was developed as part of a university software design course to demonstrate professional development practices and clean code principles. All Pokémon-related content is property of Nintendo/Game Freak/The Pokémon Company.