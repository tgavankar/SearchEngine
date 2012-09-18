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
 * Run with -Xmx512m.
 *
 */
public class SearchEngineRunner {
	public static void main(String[] args) {
		boolean ranked = Config.ranked;
		int numResults = Config.numResults;

		long startTime = System.currentTimeMillis();
		System.out.println("START " + startTime);
		
		Map<Integer, String> queries = parseQueries(Config.queryFile);
		
		try {
			 BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Config.outFile), false));
			
			for(Entry<Integer, String> e : queries.entrySet()) {
				QueryParser qp = new QueryParser();
				QueryRunner qr = new QueryRunner(ranked);
				Node root = qp.parse(e.getValue());
				List<Integer[]> result = qr.run(root);
				for(int i=0; i<Math.min(numResults, result.size()); i++) {
					bw.write(e.getKey() + " ");
					bw.write("Q0 ");
					bw.write(result.get(i)[0] + " ");
					bw.write((i+1) + " ");
					bw.write(result.get(i)[1] + " ");
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
