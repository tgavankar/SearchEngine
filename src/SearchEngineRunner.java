import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * Main class that runs the actual searches and experiments by reading queries
 * from a structured text file and outputs the results to another text file.
 * 
 * This runner prints the execution time in ms, including time required for
 * reading data from disk and parsing it, and also writing the output to disk.
 * 
 * 
 * 
 * ***IMPORTANT***: Note that for an input list of many queries (i.e. the 
 * 		supplied queries.txt), the JVM should be run with flag -Xmx512m (to 
 * 		increase max heap memory size).
 */
public class SearchEngineRunner {
	/**
	 * Main method. Runs the experiment with the options defined in Config.java.
	 */
	public static void main(String[] args) {
		Ranking ranker = Config.ranker;
		int numResults = Config.numResults;

		QueryParser qp = new QueryParser();
		QueryRunner qr = new QueryRunner(ranker);
		
		// Grab and print starting time.
		long startTime = System.currentTimeMillis();
		System.out.println("START " + startTime);
		
		Map<Integer, String> queries = parseQueries(Config.queryFile);
		
		try {
			 BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Config.outFile), false));
			
			for(Entry<Integer, String> e : queries.entrySet()) {
				Node root = qp.parse(e.getValue());
				List<DocScore> result = qr.run(root);
				// Write to file with special format for trec_eval.
				for(int i=0; i<Math.min(numResults, result.size()); i++) {
					bw.write(e.getKey() + " ");
					bw.write("Q0 ");
					bw.write(result.get(i).getDocId() + " ");
					bw.write((i+1) + " ");
					bw.write(result.get(i).getScore() + " ");
					bw.write("run-1");
					bw.newLine();
				}
			}
	        bw.close();
		} 
		catch (Exception e) {
			System.err.println(e);
		}

		
		long endTime = System.currentTimeMillis();
		System.out.println("SUCCESS " + endTime);
		System.out.println("RUNTIME: " + (endTime - startTime));
	}
	
	/**
	 * Helper method to parse the input file which is a list of
	 * query IDs and queries.
	 */
	private static Map<Integer, String> parseQueries(String filename) {
		Map<Integer, String> queries = new TreeMap<Integer, String>();
		try {
			FileInputStream fstream = new FileInputStream(filename);
	
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			String[] split;

			while ((strLine = br.readLine()) != null)   {
				split = strLine.split(":");
				if(split.length > 0) {
					queries.put(Integer.parseInt(split[0]), split[1]);
				}
			}
	
			in.close();
		} catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
		return queries;
	}
}
