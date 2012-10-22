import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Node class to represent the parsed query as a tree.
 */
public class Node {
	// Group 1 and Group 2 fields are mutually exclusive.
	
	// Group 1 fields
	private Operator operator;
	private List<Node> children;
	
	// Group 2 fields
	private String value;
	private String field;
	
	private double weight;
	
	
	public Node(Operator op) {
		this(op, Config.defaultNodeWeight);
	}
	
	public Node(Operator op, double weight) {
		operator = op;
		children = new ArrayList<Node>();
		this.weight = weight;
	}
	
	public Node(String s, double weight) {
		int splitPoint = s.indexOf(".");
		if(splitPoint < 0) {
			value = s;
			field = "body";
		}
		else {
			value = s.substring(0, splitPoint);
			field = s.substring(splitPoint+1);			
		}
		this.weight = weight;
	}
	
	public Node(String s) {
		this(s, Config.defaultNodeWeight);
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
	
	public void getLeafNodes(Set<String> set) {
		if(getValue() != null) {
			set.add(getValue());
		}
		else {
			for(Node n : getChildren()) {
				n.getLeafNodes(set);
			}
		}
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public double getWeight() {
		return this.weight;
	}
	
	public String toString() {
		String out = "";
		if(value != null) {
			out += "[" + weight + ":" + value + "]";
		}
		else {
			out += weight + ":" + operator.toString() + "(";
			for(int i=0; i<children.size(); i++) {
				out += children.get(i).toString();
			}
			out += ")";
		}
		
		return out;
	}
}
