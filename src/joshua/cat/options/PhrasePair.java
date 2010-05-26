/* This file is copyright 2010 by Lane O.B. Schwartz */
package joshua.cat.options;

public class PhrasePair {

	private final String source;
	private final String target;
	
	public PhrasePair(String source, String target) {
		this.source = source;
		this.target = target;
	}
	
	public String getSource() {
		return this.source;
	}
	
	public String getTarget() {
		return this.target;
	}
	
	public String toString() {
		return "\"" + source + "\"\t\"" + target + "\"";
	}
}
