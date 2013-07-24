import java.awt.Point;
import java.util.ArrayList;

public class HomeField extends Field {

	private final ArrayList<Pawn> homePawns = new ArrayList<Pawn>();
	private final ArrayList<Point> thePoints = new ArrayList<Point>();

	public HomeField(final ArrayList<Point> points) {
		super(points.get(0));
		for (Point p : points) {
			thePoints.add(p);
		}
	}

	@Override
	public final Pawn getPawn() {
		Pawn p = null;
		if (hasPawn()) {
			p = homePawns.remove(homePawns.size() - 1);
		}
		return p;
	}

	public final void addPawn(final Pawn pawn) {
		homePawns.add(pawn);
	}

	@Override
	public Point getPoint() {
		return null;
	}

	public ArrayList<Point> getPoints() {
		return thePoints;
	}
}
