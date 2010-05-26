/* This file is copyright 2010 by Lane O.B. Schwartz */
package joshua.cat.options;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface TextCompletionModel {

	public String complete(String prefix);
	
}

class DummyCompletionModel implements TextCompletionModel {
	
	private final List<String> words;
	
	public DummyCompletionModel() {
		
		words = new ArrayList<String>(5);
		words.add("spark");
		words.add("special");
		words.add("spectacles");
		words.add("spectacular");
		words.add("swing");
	}
	
	public String complete(String prefix) {
		int n = Collections.binarySearch(words, prefix);

		if (n < 0 && -n <= words.size()) {
			String match = words.get(-n - 1);
			if (match.startsWith(prefix)) {
				return match.substring(prefix.length());
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
}
