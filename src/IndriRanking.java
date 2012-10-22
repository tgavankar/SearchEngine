import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class IndriRanking extends Ranking {
	private static final double lambda = 0.25;
	private static final double mu = 1500;
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
		
		return Math.log(lambda * ((tft + mu * ((qtft)/(corpusNumWordOccurs)))/(doclen + mu)) + (1-lambda) * ((qtft)/(corpusNumWordOccurs)));
	}

}
