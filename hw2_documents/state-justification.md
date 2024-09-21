# Justification for handling state
Below, describe where you stored each of the following states and justify your answers with design principles/goals/heuristics/patterns. Discuss the alternatives and trade-offs you considered during your design process.

## Players
Storage: The Game class maintains a list of Player objects.

Justification: Storing players within the Game class encapsulates the relationship between the game and its players. This adheres to the principle of Encapsulation, ensuring that player-related actions and attributes are managed in a single place, promoting cohesion.

Alternatives: One alternative considered was to have a PlayerManager class. While this could isolate player logic, it would complicate the interaction with the Game class and introduce unnecessary dependencies, since there are only 2 players. Therefore, I choose to use Game to store Players, which is simpler and mroe intuitive

## Current player
Storage: The Game class keeps track of the currentPlayer as private field of turn.

Justification: Storing the current player in the Game class maintains clarity about whose turn it is. This follows the Single Responsibility Principle (SRP), as the Game is responsible for managing the game state, including the current player's turn.

Alternatives: I considered storing the current player within the Player class itself. However, this would confuse the player’s role and lead to tight coupling. The chosen design maintains a clear hierarchy and separation of concerns.>

## Worker locations
Storage: Each Worker object has its own position attribute, which is represented by a Cell object.

Justification: Associating each worker directly with its location supports Object-Oriented Design principles. Each Worker is responsible for knowing its position, which aligns with the Encapsulation principle, keeping worker-related data self-contained.

Alternatives: An alternative could be to maintain a separate WorkerManager to track all worker positions. However, this would introduce unnecessary complexity and data redundancy, as each worker inherently knows its position. The current design promotes clarity and direct access to worker attributes.

## Towers (Blocks)
Storage: Tower(blocks) states are stored within the Cell class, with each cell containing a tower level attribute.

Justification: Storing block information within Cell objects leverages the Composition principle, as blocks are fundamentally part of the game board structure. This choice simplifies access and manipulation of blocks data relative to their locations.

Alternatives: Another option was to maintain a separate TowerManager. However, this would complicate interactions with cells and make tower management cumbersome. The chosen approach minimizes dependencies and enhances the model's clarity.

## Winner
Storage: The Player class contains a winStatus attribute to indicate whether the player has won.

Justification: Storing the winner's status within the Player class adheres to the principle of local state management. Each player tracks their own win status, making the logic clearer. This simplifies the implementation of game logic since determining the winner only requires checking each player's status, rather than additional logic in the Game class. It also follows the Single Responsibility Principle, as each Player is responsible for its own state.

Alternatives: Another option was to store the winner's status in the Game class. While this could centralize game state management, it would make the Game class more cumbersome and increase logical complexity. Since the victory status of players is independent of other game states (like the current player or tower states), keeping it in the Player class aligns better with object-oriented design principles.


## Design goals/principles/heuristics considered
Encapsulation: Keeping related data and methods together to reduce complexity and improve maintainability.
Single Responsibility Principle: Ensuring that each class has a single purpose, facilitating easier updates and debugging.
Composition: Utilizing the relationship between objects to maintain a clear and intuitive structure.
Clarity and Simplicity: Designing with straightforward interactions to reduce cognitive load for developers and users.

## Alternatives considered and analysis of trade-offs
PlayerManager vs. Players in Game: While a PlayerManager could centralize player logic, it would complicate the architecture and introduce unnecessary coupling. Directly managing players within the Game class promotes simplicity.
Current Player in Player vs. Game: Storing the current player within the Player class could make individual player states more complex. Keeping it in the Game class maintains a clear game flow.
WorkerManager vs. Worker in Cell: A separate manager for worker locations could lead to redundancy and increased complexity. Storing worker locations in the Cell class provides direct access and manipulation.