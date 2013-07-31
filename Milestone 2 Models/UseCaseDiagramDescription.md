Use Case Diagram Description
============================

Use Case: Take Turn
-------------------
### Participating Actor ###
A player.

### Basic Flow ###
1. Start of player's turn.
2. (Choose Move) Player chooses a move according to their given strategy and the die roll.
3. (Take Move) The player takes their chosen move.
4. The player's turn is over.

### Alternative Flow ###
[Player has no valid moves]
The player skips step 3.

Use Case: Choose Move
---------------------
### Participating Actor ###
A player.

### Basic Flow ###
1. Verify there are no moves the player needs to take.
2. Check through possible moves
3. Choose the one most suited to the player's given strategy and die roll.

Use Case: Take Move
-------------------
### Participating Actor ###
A player.

### Basic Flow ###
1. Player's pawn moves in accordance to the die roll.

### Alternative Flow ###
[Player's move is invalid]
An exception occurs instead of step 1, and the game continues.
