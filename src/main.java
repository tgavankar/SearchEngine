import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * Run with -Xmx512m (can be done with 300m).
 *
 */
public class main {
	public static void main(String[] args) {
		boolean ranked = true;
		int numResults = 100;
		
		Map<Integer, String> queries = parseQueries("queries.txt");
		
		System.out.println("START");
		try {
			 BufferedWriter bw = new BufferedWriter(new FileWriter(new File("AND_BOW_RANKED.txt"), false));
			
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

		
		System.out.println("SUCCESS");
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
