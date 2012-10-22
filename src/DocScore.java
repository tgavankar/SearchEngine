
public class DocScore {
	int docId;
	double score;
	
	public DocScore(int docId, double score) {
		this.docId = docId;
		this.score = score;
	}
	
	/**
	 * @return the docId
	 */
	public int getDocId() {
		return docId;
	}
	
	/**
	 * @param docId the docId to set
	 */
	public void setDocId(int docId) {
		this.docId = docId;
	}
	
	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}

	
}
