import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/* 
 * 
 */
public class Player {
	private final Strategy strategy;
	private final ArrayList<GoalField> goalField;
	private final HomeField homeField;
	private final JLabel playLabel;
	private final LudoGame parent;
	// Should be a set of 4.
	private final ArrayList<Pawn> pawns;

	/**
	 * 
	 * @param strategy
	 *            The strategy used by the player.
	 * @param goalField
	 *            The player's goal field. This should be determined by the game
	 *            itself.
	 * @param homeField
	 *            The player's home field. This should be determined by the game
	 *            itself.
	 * @param playLabel
	 *            The player's label, for showing in the GUI whose turn it is.
	 * @param pawns
	 *            The set of the players pawns. These should be created by the
	 *            Ludo game initialization.
	 */
	public Player(final Strategy strategy,
			final ArrayList<GoalField> goalField, final HomeField homeField,
			final JLabel playLabel, final ArrayList<Pawn> pawns,
			final LudoGame parent) {
		this.strategy = strategy;
		this.goalField = goalField;
		this.homeField = homeField;
		this.playLabel = playLabel;
		this.playLabel.setFont(new Font("Sans-Serif", Font.BOLD, 26));
		this.pawns = pawns;
		this.parent = parent;
	}

	/**
	 * Calls on the strategy to perform a move.
	 */
	public final void doMove(final int dieRoll) {
		if (dieRoll == 6 && homeField.getPawnCount() > 0
				&& checkMovePawnHome() != null) {
			takeMove(new Move(homeField.getPawn(), homeField.getNextField()));
		} else {
			strategy.chooseMove(this, dieRoll);
		}
	}

	public void takeMove(final Move move) {
		if (move != null) {
			Pawn p = move.getPawn();
			Field f = move.getField();

			while (move.getField() != f) {
				if (f.getClass() == BasicField.class) {
					if (((BasicField) f).hasGoalField()) {
						if (((BasicField) f).getGoalField() == goalField.get(3)) {
							f = ((BasicField) f).getGoalField();
						} else {
							f = f.getNextField();
						}
					} else {
						f = f.getNextField();
					}
				} else {
					f = f.getNextField();
				}
			}
			p.moveToField(f);
		}
		SwingUtilities.invokeLater(parent.continueAfterThreadEnd);
	}

	/**
	 * 
	 * @return The home field.
	 */
	public final HomeField getHomeField() {
		return homeField;
	}

	/**
	 * 
	 * @return The goal field.
	 */
	public final GoalField getEntryGoalField() {
		// The end of the GoalField array is where the entry point is.
		return goalField.get(goalField.size() - 1);
	}

	public final ArrayList<Pawn> getPawns() {
		return pawns;
	}

	/**
	 * Moves a pawn out of its homefield.
	 */
	public Field checkMovePawnHome() {
		if (checkValidMove(homeField.getNextField())) {
			// Pawn p = homeField.getPawn();
			// p.moveToField(homeField.getNextField());
			// System.out.println("Moved the pawn to "
			// + homeField.getNextField().getPoint().toString());
			return homeField.getNextField();
		} else {
			return null;
		}
	}

	/**
	 * Moves a pawn along the normal fields, checking for matching goal fields.
	 * Once distance left to travel is zero, settles on field to move to.
	 * 
	 * @param pawn
	 * @param field
	 * @param distance
	 */
	public Field checkMovePawnBasic(final Pawn pawn, final BasicField field,
			final int distance) {
		if (distance == 0 && checkValidMove(field)) {
			return field;
		} else if (distance == 0 && !checkValidMove(field)) {
			return null;
		} else {
			if (field.hasGoalField()) {
				if (field.getGoalField() == goalField.get(3)) {
					return checkMovePawnGoal(pawn, goalField.get(3),
							distance - 1);
				} else {
					System.out
							.println("Noticed a goal field ... failed to be interested");
					return checkMovePawnBasic(pawn,
							(BasicField) field.getNextField(), distance - 1);
				}
			} else {
				return checkMovePawnBasic(pawn,
						(BasicField) field.getNextField(), distance - 1);
			}
		}
	}

	/**
	 * Moves a pawn along the goal fields.
	 * 
	 * @param pawn
	 * @param goal
	 * @param distance
	 * @return
	 */
	public Field checkMovePawnGoal(final Pawn pawn, final GoalField goal,
			final int distance) {
		if (distance == 0) {
			if (checkValidMove(goal)) {
				return goal;
			} else {
				return null;
			}
		} else if (!goal.hasNextField()) {
			return null;
		} else {
			return checkMovePawnGoal(pawn, (GoalField) goal.getNextField(),
					distance - 1);
		}
	}

	/**
	 * 
	 * @return True if the player's goal fields are full.
	 */
	public boolean checkIfGoalFull() {
		boolean isFull = true;
		for (GoalField g : goalField) {
			isFull &= g.hasPawn();
		}
		return isFull;
	}

	/**
	 * 
	 * @return This returns true if one or more goal field has a pawn.
	 */
	public boolean checkIfGoalOccupied() {
		boolean hasPawn = false;
		for (GoalField g : goalField) {
			hasPawn |= g.hasPawn();
		}
		return hasPawn;
	}

	/**
	 * 
	 * @return The number of pawns occupying the goal.
	 */
	public int getGoalOccupiedCount() {
		int numPawns = 0;
		for (GoalField g : goalField) {
			if (g.hasPawn()) {
				numPawns++;
			}
		}
		return numPawns;
	}

	public void setLabelNotTurn() {
		playLabel.setForeground(new Color(0x000000));
	}

	public void setLabelIsTurn() {
		playLabel.setForeground(new Color(0xff2222));
	}

	/**
	 * 
	 * @param field
	 *            The field that the player wishes to move the pawn to.
	 * @return True if the move is valid.
	 */
	public boolean checkValidMove(final Field field) {
		if (field.hasPawn()) {
			if (isOwnPawn(field.getPawn())) {
				System.out
						.println("Invalid move considered, own pawn at field location");
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @param foundPawn
	 *            The pawn the player wishes to check.
	 * @return True if the foundPawn is the player's own pawn.
	 */
	private boolean isOwnPawn(final Pawn foundPawn) {
		boolean ownPawn = false;
		for (Pawn p : pawns) {
			ownPawn |= (p == foundPawn);
		}
		return ownPawn;
	}
}
