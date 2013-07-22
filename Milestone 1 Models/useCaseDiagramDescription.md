# Use Case Diagram Description

## Use Case: "Take Ludo Turn"
### Basic Flow:

1. Start of player's turn.
2. (rollDie) Player recieves randomized dice roll.
3. (planMove) Positions of each pawn after move is calculated and displayed or passed into the AI.
4. (takeMove) Player chooses one of the pawns to move.
5. (moveTo) The pawn choice is moved according to the die roll.
6. (checkCollisions) IF the pawn is occupying the same space as another, remove it from the field. ELSE do nothing.
7. Player turn ends.

Alternative Flows:
[EndField]
After step 4, move pawn into win field.
Resume at step 7.