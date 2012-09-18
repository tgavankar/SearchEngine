
public class Config {
	// Always modified
	public static final boolean ranked = true;
	public static final boolean defaultIsOr = true;
	public static final String queryFile = "struct_queries.txt";
	public static final String outFile = "tempout.txt";
	
	// Sometimes modified
	public static final int numResults = 100;
	
	// Shouldn't need changing
	public static final String hyphenConvert = "NEAR/1";
	public static final String invListDir = "invlist";
}
