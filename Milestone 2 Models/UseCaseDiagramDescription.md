Use Case Diagram Description
============================

Use Case: doMove
----------------
### Participating Actor ###
A player.

### Basic Flow ###
1. The Player recieves a die roll from the game board.
2. The Player utilizes their given strategy to evalute the best possible move to take.
3. The player takes their turn.

Use Case: checkMovePawnHome
---------------------------
### Participating Actor ###
A player.

### Basic Flow ###
1. Verify that the location of the players starting field is a valid movement.

Use Case: checkMovePawnBasic
-----------------------------
### Participating Actor ###
A player.

### Basic Flow ###
1. Determine the destination of the pawn.
2. Verify this is a legitimate move.
2. Move based on type of destination field. (Choosing the right method of movement)

Use Case: checkMovePawnGoal
----------------------------
### Participating Actor ###
A player.

### Basic Flow ###
1. Determine the destination of the pawn.
2. Verify this is a legitimate move.
2. Move along to the destination goal field.

Use Case: checkIfGoalFull
-------------------------
### Participating Actor ###
A player.

### Basic Flow ###
1. Check each location in the goal field, and determine weither **each** has a pawn.

Use Case: checkIfGoalOccupied
-------------------------
### Participating Actor ###
A player.

### Basic Flow ###
1. Check each location in the goal field, and determine weither **any** have a pawn.

Use Case: checkValidMove
-------------------------
### Participating Actor ###
A player.

### Basic Flow ###
1. Verify that the move the player is attempting does not contain one of the players's own pawns.