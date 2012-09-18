
public class QueryParser {
	public static final StopWords sw = new StopWords();
	
	public QueryParser() {
		
	}
	
	public Node parse(String in) {
		in = in.replaceAll("(\\w+)-(\\w+)", "#" + Config.hyphenConvert + "\\($1 $2\\)"); // Handle hyphenated words
		in = in.replaceAll("\\.\\s+", " "); // Handle non-field-defining periods
		in = in.replaceAll("[,/;]", " "); // Handle other term-separating punctuation
		in = in.replaceAll("'", ""); // Handle term-joining punctuation
		in = in.replaceAll("\\s*\\(\\s*", "\\("); // Trim open paren for ops, "#OR ( a b)" -> "#OR(a b)"
		in = in.replaceAll("\\s*\\)", "\\)"); // Trim close paren for ops, "#OR(a b )" -> "#OR(a b)"
		in = in.replaceAll("\\s+", " "); // Trim extra spaces
		Node root = new Node(Config.defaultIsOr ? new OrOperator() : new AndOperator());
		parse(in, root);
		if(root.getChildren().size() > 1)
			return root;
		return root.getChildren().get(0);
	}
	
	private Node parse(String in, Node parent) {
		for(int i=0; i<in.length(); i++) {
			if(in.charAt(i) == '#') {
				Operator op = new OrOperator(); //default op
				int iIncr = 0;
				if(in.substring(i+1, i+1+2).equals("OR")) {
					//op = new OrOperator();
					iIncr += 1 + 2 + 1 + 1; // #OR()
				}
				else if(in.substring(i+1, i+1+3).equals("AND")) {
					op = new AndOperator();
					iIncr += 1 + 3 + 1 + 1; //#AND()
				}
				else if(in.substring(i+1, i+1+4).equals("NEAR")) {
					op = new NearOperator(Integer.parseInt(in.substring(i+1+4+1, (in.substring(i+1+4+1).indexOf("("))+i+1+4+1)));
					iIncr += 1 + 4 + 1 + (String.valueOf(((NearOperator) op).getDistance()).length()) + 1 + 1; //#NEAR/number()
				}
				
				Node node = new Node(op);
				parent.addChild(node);
				String parenStr = getParenString(in.substring(i));
				parse(parenStr, node);
				i += parenStr.length() + iIncr;
			}
			else {
				String word = in.substring(i);
				int endWord = word.indexOf(" ");
				if(endWord > 0) {
					word = in.substring(i, i+endWord);
				}
				if(!sw.isStopword(word)) { // Handle stopwords
					parent.addChild(new Node(word));
				}
				i += word.length();
			}
		}
		return parent;
	}
	
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
