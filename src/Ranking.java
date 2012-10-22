import java.util.Map;


public abstract class Ranking {
	public double getScore(Node node, InvertedList il, InvertedListEntry ile) {
		return node.getWeight() * getScoreInternal(node, il, ile);
	}
	
	protected abstract double getScoreInternal(Node root, InvertedList il, InvertedListEntry ile); 
}
