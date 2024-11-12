# Waste Sorting - Educational Environmental Protection Game

## Introduction
Amid increasing environmental pollution, sorting and recycling waste has become more important than ever. The game **Waste Sorting** was developed to raise community awareness about proper waste sorting through an educational and entertaining approach. Players will participate by sorting different types of waste into the correct bins, fostering environmental consciousness through small actions.

## Objectives
The **Waste Sorting** game aims to:
1. Help players recognize different types of waste and how to sort them correctly.
2. Raise environmental awareness through the act of sorting waste.
3. Encourage habits of waste sorting from small daily actions.

## System Structure
**Waste Sorting** is a networked game built on a Client-Server architecture:
- **Client**: Built using JavaFX, provides the user interface and sends requests to the server.
- **Server**: Processes requests, manages player information, scores, and leaderboards.

### Overall Architecture
- **Client**:
   - Uses JavaFX to create a user-friendly game interface.
   - Connects to the server via TCP protocol, sending requests and receiving results in JSON or String format.

- **Server**:
   - Receives and processes requests from the client through a **Handler**.
   - Uses **Command** to handle specific requests and **Service** to perform business logic.
   - Interacts with the MySQL database through **DAO** and **Hibernate** to store data.

- **Database**: MySQL is used to store player information, game results, waste types, and other necessary data. Hibernate manages data storage and retrieval from the database.

## Key Features

### 1. Registration/Login
- Players can register for a new account or log in if they already have an account.
- After logging in, players can view a list of online players and their statuses.

### 2. Start a Match
- Players select an opponent from the online list to send a match invitation.
- If the opponent accepts, both players enter the waiting room, and the game starts after a 5-second countdown.

### 3. Game Rules
- The game lasts for 2 minutes, during which players must sort randomly generated waste into the correct bins:
   - **Organic Bin**: Food, fruit peels.
   - **Plastic Bin**: Plastic bottles, nylon bags.
   - **Metal Bin**: Aluminum cans, metal boxes.
   - **Paper Bin**: Old newspapers, cardboard.
   - **Glass Bin**: Glass bottles, glass cups.
- Each correct sort earns 1 point **(Realtime)** ; incorrect sorting does not score points.
- The player with the higher score at the end of the time wins.

### 4. End of Match
- At the end of the match, the system updates each player's score and match history.
- The winner earns 3 points; the loser earns none. In case of a draw, each player receives 1 point.

### 5. Leaderboard
- Displays players' ranks based on total points and match outcomes (wins, draws, losses).
- Players can view their rank and compare it with others.

### 6. Match History
- Stores and displays details of previous matches, including player names, scores, and match times.

## Database
Uses MySQL to store data tables including:
- **Player**: Player information.
- **Player_History**: Player match history.
- **Game**: Game results.

## Technology Stack
- **JavaFX**: Builds the client user interface.
- **TCP**: Transmission protocol between client and server.
- **Hibernate**: ORM (Object-Relational Mapping) for MySQL interaction.
- **MySQL**: Stores game data.

## Installation Guide
1. **Install MySQL**: Create a database for the system and configure the server connection.
2. **Server**:
   - Download the server source code and install the necessary libraries.
   - Run the server and configure connection parameters.
3. **Client**:
   - Download the client source code.
   - Use JavaFX to run the client application and connect to the server via TCP.

## Contributions
We welcome contributions to improve the **Waste Sorting** game! Please create a **Pull Request** or **Issue** to discuss any changes.

## Contact
For any questions, please contact us via email: **ducchinh.work@gmail.com**

---

**Join the Waste Sorting game to learn how to protect the environment through small actions!**
