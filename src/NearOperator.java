import java.util.List;
import java.util.Map;


public class NearOperator extends Operator {
	private int distance;
	
	public NearOperator(int dist) {
		distance = dist;
	}
	
	@Override
	public String getType() {
		return "NEAR";
	}

	public int getDistance() {
		return distance;
	}
	
	public String toString() {
		return getType() + ":" + distance;
	}

	@Override
	public Map<Integer, Integer> combine(List<Map<Integer, Integer>> l) {
		// TODO Auto-generated method stub
		return null;
	}
}
