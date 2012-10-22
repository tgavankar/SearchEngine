import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that parses in a string query. It handles both structured
 * and unstructured queries, where unstructured queries are combined
 * via the default operator defined in Config. Stopwords are removed.
 * 
 * The root Node of the tree representation of the parsed query is returned.
 * 
 * One instance of this class can be used for multiple queries to be parsed.
 */
public class QueryParser {
	public static final StopWords sw = new StopWords();
	
	public QueryParser() {}
	
	/**
	 * Takes in a string (the query), parses it, and returns the root
	 * Node for the query tree.
	 * 
	 * Handles hyphenated input words by changing it to NEAR operator.
	 * 
	 * Throws StackOverflowException if a structured query is malformed,
	 * such as unbalanced parentheses, etc.
	 * @param in
	 * @return
	 */
	public Node parse(String in) {
		in = in.replaceAll("(\\w+)-(\\w+)", "#" + Config.hyphenConvert + "\\($1 $2\\)"); // Handle hyphenated words
		//in = in.replaceAll("\\.\\s+", " "); // Handle non-field-defining periods
		in = in.replaceAll("'", ""); // Handle term-joining punctuation
		in = in.replaceAll("\\s*\\(\\s*", "\\("); // Trim open paren for ops, "#OR ( a b)" -> "#OR(a b)"
		in = in.replaceAll("\\s*\\)", "\\)"); // Trim close paren for ops, "#OR(a b )" -> "#OR(a b)"
		in = in.replaceAll("\\s+", " "); // Trim extra spaces
		// Convert #UW to #NEAR with all permutations
		while(in.indexOf("#UW") > -1) {
			in = in.replaceFirst("#UW\\/([0-9]+)\\(([^)]+)\\)", "#OR(" + convertWindow(in.substring(in.indexOf("#UW"))) + ")");
		}

		Node root = new Node(Config.defaultOperator);
		parse(in, root, 1.0);
		if(root.getChildren().size() > 1)
			return root;
		return root.getChildren().get(0);
	}
	
	/**
	 * Recursive helper function that does the actual parsing of the string 
	 * to a query tree and attaches it as a child of the parent Node.
	 */
	private Node parse(String in, Node parent, double weight) {
		boolean isWeighted = (weight == Double.NEGATIVE_INFINITY);
		double childWeight = Config.defaultNodeWeight;
		double nodeWeight = Config.defaultNodeWeight;
		for(int i=0; i<in.length(); i++) {
			if(in.charAt(i) == '#') {
				// The token is a structured operator.
				Operator op = Config.defaultOperator; //default op
				int iIncr = 0;
				if(in.substring(i+1, i+1+2).equals("OR")) {
					op = new OrOperator();
					iIncr += 1 + 2 + 1 + 1; // chars in #OR()
				}
				else if(in.substring(i+1, i+1+3).equals("AND")) {
					op = new AndOperator();
					iIncr += 1 + 3 + 1 + 1; // chars in #AND()
				}
				else if(in.substring(i+1, i+1+4).equals("NEAR")) {
					// Create NEAR operator with distance parsed from after "/" char to start of "(".
					op = new NearOperator(Integer.parseInt(in.substring(i+1+4+1, (in.substring(i+1+4+1).indexOf("("))+i+1+4+1)));
					iIncr += 1 + 4 + 1 + (String.valueOf(((NearOperator) op).getDistance()).length()) + 1 + 1; // chars in #NEAR/number()
				}
				else if(in.substring(i+1, i+1+6).equals("WEIGHT")) {
					op = new AndOperator();
					iIncr += 1 + 6 + 1 + 1;
					childWeight = Double.NEGATIVE_INFINITY;
				}
				
				Node node = new Node(op, nodeWeight);
				parent.addChild(node);
				String parenStr = getParenString(in.substring(i));
				
				// Recurse
				parse(parenStr, node, childWeight);
				i += parenStr.length() + iIncr;
			}
			else {
				// The token is a query term, so just a word.
				String word = in.substring(i);
				int endWord = word.indexOf(" ");
				if(endWord > 0) {
					word = in.substring(i, i+endWord);
				}
				
				if(isWeighted) {
					try {
						nodeWeight = Double.parseDouble(word);
						i += String.valueOf(nodeWeight).length();
						continue;
					}
					catch(NumberFormatException e) {
						//nodeWeight = Config.defaultNodeWeight;
					}
				}
				
				// Handle stopwords
				if(!sw.isStopword(word)) {
					parent.addChild(new Node(word, nodeWeight));
				}
				i += word.length();
			}
		}
		return parent;
	}
	
	/**
	 * Helper method that converts #UW -> #OR(#NEAR...)
	 * @param in
	 * @return
	 */
	private String convertWindow(String in) {
		String dist = in.substring(4, in.indexOf("("));
		List<List<String>> permutations = new ArrayList<List<String>>();
		permutateList(new ArrayList<String>(), Arrays.asList(getParenString(in).split(" ")), permutations);
		
		String fullOut = "";
		
		for(List<String> p : permutations) {
			String out = "";
			for(String e : p) {
				out += " " + e;
			}
			
			if(out.length() > 0) {
				out = out.substring(1);
			}
			
			fullOut += " #NEAR/" + dist + "(" + out + ")"; 
		}
		
		if(fullOut.length() > 0) {
			fullOut = fullOut.substring(1);
		}

		return fullOut;
	}
	
	// Source: http://setentiacuriosa.blogspot.com/2010/06/recursively-find-permutations-of-list.html
	private void permutateList(List<String> startList, List<String> endList, List<List<String>> result) {
	    if (endList.size() <= 1) {
			List<String> permResult = new ArrayList<String>();
			permResult.addAll(startList);
			permResult.addAll(endList);
			result.add(permResult);
	    }
	    else {
			for(int i = 0; i < endList.size(); i++) {
				List<String> newEndList = new ArrayList<String>();
				for(int j = 0; j < i; j++) 
					newEndList.add(endList.get(j));
				for(int j = i+1; j < endList.size(); j++) 
					newEndList.add(endList.get(j));
				
				List<String> newStartList = new ArrayList<String>();
				newStartList.addAll(startList);
				newStartList.add(endList.get(i));
				permutateList(newStartList, newEndList, result);
			}
	    }
	}
	
	
	/**
	 * Helper method to get substring inside of parentheses,
	 * using a count of open/close parens.
	 */
	private String getParenString(String in) {
		int count = 0;
		int start = in.indexOf("(");
		for(int i=start; i<in.length(); i++) {
			if(in.charAt(i) == '(') {
				count++;
			}
			else if(in.charAt(i) == ')') {
				count--;
			}
			
			if(count == 0) {
				return in.substring(start+1, i);
			}
		}
		// Malformed or no parentheses.
		return in;
	}
}
