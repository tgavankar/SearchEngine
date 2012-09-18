/**
 * This class keeps the configuration options for the entire search
 * engine, so as to make tweaking and modifications of the settings
 * easier.
 */
public class Config {
	/**
	 * These are the fields that will probably change per run,
	 * depending on what the desired outcome is.
	 */
	
	/**
	 * Whether the results should be ranked or not.
	 */
	public static final boolean ranked = true;
	
	/**
	 * What the default bag-of-words operation should be.
	 * True => OR
	 * False => AND
	 */
	public static final boolean defaultIsOr = true;
	
	/**
	 * The location of the input query file. It should have the format:
	 * QUERYID:QUERY STRING
	 * 
	 * So, for example:
	 * 4:obama family tree
	 * 7:#AND(obama #NEAR/1(family tree))
	 */
	public static final String queryFile = "struct_queries.txt";
	
	/**
	 * The location of the output file. This file does not
	 * have to exist, but if it does, it will be overwritten
	 * without confirmation.
	 */
	public static final String outFile = "tempout.txt";
	
	
	/**
	 * These are the fields that are sometimes changed, but
	 * not usually.
	 */
	
	/**
	 * The number of results you want to show per query after ranking. 
	 */
	public static final int numResults = 100;
	
	/**
	 * These shouldn't really be changed unless they
	 * absolutely need to be.
	 */
	
	
	/**
	 * What operation a hyphenated word should be replaced with.
	 */
	public static final String hyphenConvert = "NEAR/1";
	
	/**
	 * The name of the directory that holds the inverted lists.
	 * Within this directory should be more directories titled
	 * "body", "title", etc (field names) and then the WORD.inv 
	 * files should be placed within those directories.
	 */
	public static final String invListDir = "invlist";
}
