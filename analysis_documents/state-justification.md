# Justification for handling state
Below, describe where you stored each of the following states and justify your answers with design principles/goals/heuristics/patterns. Discuss the alternatives and trade-offs you considered during your design process.

## Players
Storage: I store the players as an array of Player objects in the Game class. (Game: players[2])

Justification: Storing players within the Game class encapsulates the relationship between the game and its players. The Game class is responsible for managing the players, which aligns with the Single Responsibility Principle. Given that there are exactly 2 players, using and array of length 2 is the simplest and most intuitive way.

Alternatives: One alternative considered is to separate player fields: Having player1 and player2 as individual fields. This can reduce array indexing but would make code less maintainable when the logic becomes more complex. 

Tradeoffs: Using the fixed-length array limits scalability. If the game were to expand to more than two players, the array would need to be replaced or modified to a more flexible data structure.

## Current player
Storage:  The Game class has a reference to the turn, which is the current player. (Game: turn)

Justification: Storing the current player in the Game class intuitively makes sense because Game controls whose turn it is. Managing the turn directly in Game allows the game logic (like switching turns) to be encapsulated within the Game class. 

Alternatives: I considered storing the current player within the Player class itself, e.g.: add isTurn attribute. However, this would confuse the player’s role (spread game-related logic across multiple classes), which will increase coupling between Game and Player. 

Tradeoffs: Storing turn in the Game class centralizes turn management, making the Player class simpler but slightly increasing the complexity of the Game class.

## Worker locations
Storage: I store worker locations in 2 places. (1). Worker Location in Worker: position: Each Worker object stores a reference to the Cell it occupies. (2). Worker Location in Cell: occupied: Each Cell object has an occupied field that points to the Worker (if any) on that cell. (Worker: position and Cell: occupied)

Justification: 

In Worker: position: Each Worker object holds a reference to a Cell object through its position attribute. This design keeps track of where a worker is on the board. This also adheres to the Single Responsibility Principle (SRP): the Worker is responsible for managing its position, while the Cell is responsible for managing what occupies it. 

In Cell: occupied: This ensures that the board can easily determine if a cell is occupied, making game logic related to movement and validity checking more efficient. This helps with low coupling, as the Game or Board classes don’t need to keep track of all worker positions separately — the Cell objects themselves know if they are occupied.

Alternatives: Only Store Location in Cell or Worker: Storing the location only in one of these classes  would reduce redundancy. However, this may make querying less efficient. For example, if only Worker tracks its position, checking whether a Cell is occupied would require scanning through all workers — increasing time complexity for movement-related operations.

Tradeoffs: Having both Worker.position and Cell.occupied introduces some redundancy. The system must ensure consistency between these two references (e.g., if a worker moves, both the worker’s position and the cell’s occupied state must be updated).

## Towers (Blocks)
Storage: Tower(blocks) states are stored within the Cell class, with each cell containing a tower level attribute. (Cell: block)

Justification: Each Cell contains a Block object, which is an intuitive design since blocks are physical entities on the cell on the board. This follows the principle of Composition Over Inheritance, as a Cell "has a" block rather than block inhertently as a type of Cell. This makes the code more stable by avoiding tight coupling and the inheritance fragile base class problem. 

Alternatives: Blocks could be stored separately in a list or map in Board, but this would complicate the mapping between blocks and their locations, making it harder to manage the game state.

Tradeoffs: Storing the Block in the Cell simplifies access to block data (height & dome status), but this ties the block tightly to the cell. In cases where more complicated case, e.g.: multiple blocks per cell could exist, this would need to be rethought.

## Winner
Storage: (Player: winStatus) The Player class contains a winStatus attribute to indicate whether the player has won.

Justification: The Game class can easily check the winStatus of each player to determine if the game has ended, aligning with the Delegation principle by offloading responsibility for checking win conditions to the Player class.

Alternatives: The Game class could directly store a winner variable. However, this would make Game more complex and violate the principle of keeping responsibilities in the correct class. 

Tradeoffs: Storing the win state in Player keeps the game logic cleaner, but it requires the Game class to query each player for their status, potentially introducing more checks.

## Design goals/principles/heuristics considered
Encapsulation: Keeping related data and methods together to reduce complexity and improve maintainability.
Single Responsibility Principle: Ensuring that each class has a single purpose, facilitating easier updates and debugging.
Composition: Utilizing the relationship between objects to maintain a clear and intuitive structure.
Clarity and Simplicity: Designing with straightforward interactions to reduce cognitive load for developers and users.
Low coupling: Ensuring that each class interacts with others through well-defined, minimal interfaces, limiting their direct dependencies on each other. 

## Alternatives considered and analysis of trade-offs
Indicated in each above.