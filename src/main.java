import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;


public class main {
	public static void main(String[] args) {
		boolean ranked = true;
		QueryParser qp = new QueryParser();
		QueryRunner qr = new QueryRunner(ranked);
		/*Node root = qp.parse("#AND(#AND(george bush) #OR(#AND(barack obama) #AND(bill clinton) #NEAR/10(white house)))");
		//assert(root.toString().equals("AND(AND([george][bush])OR(AND([barack][obama])AND([bill][clinton])NEAR:10([white][house])))"));
		System.out.println(root);
		
		root = qp.parse("the barack obama from within the white house");
		System.out.println(root);
		
		root = qp.parse("#AND(#AND(george bush) #OR(#AND(barack.title obama.title) #AND(hilary bill-clinton will) #NEAR/10(white-house lawn)))");
		System.out.println(root);*/
		
		/*HashMap<String, Integer> firstMap = new HashMap<String, Integer>();
		firstMap.put("a", 1);
		firstMap.put("b", 1);
		firstMap.put("c", 1);
		
		HashMap<String, Integer> secondMap = new HashMap<String, Integer>();
		secondMap.put("b", 2);
		secondMap.put("c", 1);
		secondMap.put("d", 1);
		
		HashMap<String, Integer> thirdMap = new HashMap<String, Integer>();
		thirdMap.put("b", 3);
		
		firstMap.keySet().retainAll(secondMap.keySet());
		firstMap.keySet().retainAll(thirdMap.keySet());
		
		System.out.println("asdf");*/
		
		Node root = qp.parse("#NEAR/1(atari games)");
		Map<Integer, Integer> ran = qr.run(root);
		
		for (Entry<Integer, Integer> entry  : entriesSortedByValues(ran)) {
		    System.out.println(entry.getKey()+":"+entry.getValue());
		}

	}
	
	// Source: http://stackoverflow.com/questions/2864840/treemap-sort-by-value
	static <K extends Comparable<? super K>,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
            new Comparator<Map.Entry<K,V>>() {
                @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                    int res = e2.getValue().compareTo(e1.getValue());
                    int res2 = e1.getKey().compareTo(e2.getKey());
                    return res != 0 ? res : (res2 != 0 ? res2 : 1); // Special fix to preserve items with equal values
                }
            }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

}
