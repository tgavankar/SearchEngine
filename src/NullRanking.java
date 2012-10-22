import java.util.Map;
import java.util.Map.Entry;


public class NullRanking extends Ranking {

	@Override
	public double getScoreInternal(Node root, InvertedList il, InvertedListEntry ile) {
		return 1.0;
	}

}
