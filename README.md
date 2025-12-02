# 🎮 Gomoku CLI – Java Project

A command-line implementation of **Gomoku (Five-in-a-Row)** built using:

- Java 17  
- Maven  
- JUnit 5  
- H2 Database  
- DAO Pattern  
- Strategy Pattern  
- Logback Logging  

Play as the human player against a machine opponent that picks moves automatically.

---

## 📂 Project Structure

src/
└── main/java/com/gomoku
├── app # CLI entry point (Main.java)
├── model # Board, Move, CellState, GameResult
├── service # Game logic + AI strategies
└── persistence # H2 database + DAO implementations
└── test/java/com/gomoku
└── service # JUnit tests
pom.xml

yaml
Copy code

---

## ▶️ Running the Game

### **1. Build the project**

```bash
mvn clean install
2. Run the application
bash
Copy code
mvn exec:java -Dexec.mainClass="com.gomoku.app.Main"
Or run Main.java in IntelliJ IDEA.

🎮 CLI Commands
Command	Description
show	Displays the current board
move r c	Human move at row r, column c
ai	Forces the AI to make a move
stats	Show cumulative statistics from database
save	Saves the current game state
exit	Exit the game

Example session:
markdown
Copy code
> show
> move 8 8
Move accepted
> ai
AI moved
> stats
Games Played: 3 | Human Wins: 1 | AI Wins: 2 | Draws: 0
🧠 AI Implementations
RandomAiStrategy
Chooses a random valid cell on the board.

SmartAiStrategy
Tries to win immediately if possible.

Blocks the human from winning on the next move.

Falls back to random selection if needed.

Both strategies use the Strategy Pattern:

java
Copy code
public interface AiStrategy {
    Move chooseMove(Board board);
}
🛢 Database – H2 + DAO Pattern
The application uses an embedded H2 relational database.

Stored tables:
game_stats – win/loss/draw counters

saved_game – serialized board states

Data access is handled with the DAO pattern, e.g.:

java
Copy code
GameStats stats = statsDao.load();
stats.incrementGamesPlayed();
statsDao.save(stats);
🧪 Testing (JUnit 5)
Tests cover:

Board operations

Move validation

Winner detection

AI decisions

Service logic

Run all tests with:

bash
Copy code
mvn test
🧱 Design Patterns Used
Pattern	Used In	Purpose
Strategy	AI implementations	Easily switch and extend AI behavior
DAO	Persistence layer	Clean DB abstraction
Layered Architecture	app → service → model → persistence	Clear separation of responsibilities

📝 Logging
Logging uses SLF4J + Logback.

Modify the config in:

css
Copy code
src/main/resources/logback.xml
✔ Completed Features
Playable Gomoku on 15×15 board

Human vs AI

Smart & Random AI strategies

Database persistence

Logging

Fully tested business logic

JavaDoc for methods

Maven project (compiles & runs)
