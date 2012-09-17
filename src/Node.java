import java.util.ArrayList;
import java.util.List;


public class Node {
	// Group 1 and Group 2 fields are mutually exclusive.
	
	// Group 1 fields
	private Operator operator;
	private List<Node> children;
	
	// Group 2 fields
	private String value;
	private String field;
	
	
	public Node(Operator op) {
		operator = op;
		children = new ArrayList<Node>();
	}
	
	public Node(String s) {
		int splitPoint = s.indexOf(".");
		if(splitPoint < 0) {
			value = s;
			field = "body";
		}
		else {
			value = s.substring(0, splitPoint);
			field = s.substring(splitPoint+1);			
		}
	}
	
	public void addChild(Node c) {
		children.add(c);
	}
	
	public List<Node> getChildren() {
		return children;
	}
	
	public Operator getOperator() {
		return operator;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getField() {
		return field;
	}
	
	public String toString() {
		String out = "";
		if(value != null) {
			out += "[" + field + ":" + value + "]";
		}
		else {
			out += operator.toString() + "(";
			for(int i=0; i<children.size(); i++) {
				out += children.get(i).toString();
			}
			out += ")";
		}
		
		return out;
	}
}
