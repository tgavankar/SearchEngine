import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class BM25Ranking extends Ranking {
	private static final double k1 = 1.2;
	private static final double b = 0.75;
	private static final double k3 = 500;
	private static final long avgDocSize = 1301;
	private static final long vocabSize = 4073034;
	private static final long corpusNumDocs = 890630;
	private static final long corpusNumWordOccurs = 1158815080;
	
	@Override
	public double getScoreInternal(Node root, InvertedList il, InvertedListEntry ile) {
		int dft = il.getDocFreq();
		int tft = ile.getTotalFreq();
		int doclen = ile.getDocLength();
		int qtft = il.getCollectionTermFreq();
		
		return Math.log((corpusNumDocs - dft + 0.5)/(dft + 0.5)) * ((tft)/(tft + k1 *((1 - b) + b * ((doclen)/(avgDocSize))))) * (((k3 + 1) * qtft)/(k3 + qtft));
	}

}
