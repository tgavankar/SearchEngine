import java.util.Map;


public class BooleanRanking extends Ranking {

	@Override
	public double getScoreInternal(Node root, InvertedList il, InvertedListEntry ile) {
		return ile.getTotalFreq();
	}

}
