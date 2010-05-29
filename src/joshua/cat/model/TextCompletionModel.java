/* This file is copyright 2010 by Lane O.B. Schwartz */
package joshua.cat.model;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public interface TextCompletionModel {

	public String complete(String prefix);
	
}

class DummyCompletionModel extends ListCompletionModel {
	public DummyCompletionModel() {
		super(Locale.getDefault(), getDummyList());
	}
	
	private static List<String> getDummyList() {
		ArrayList<String> words = new ArrayList<String>(5);
		words.add("aleph");
		words.add("alpha");
		words.add("beta");
		words.add("beaker");
		words.add("cat");
		return words;
	}
}

class ListCompletionModel implements TextCompletionModel {
	private final List<String> words;
	protected final Collator localeAwareComparator;
	
	public ListCompletionModel(Locale locale, List<String> words) {
		this.words = words;
		this.localeAwareComparator = Collator.getInstance(locale);
	}
	
	public String complete(String prefix) {
		int n = Collections.binarySearch(words, prefix,localeAwareComparator);

		if (n < 0 && -n <= words.size()) {
			int prefixLength = prefix.length();
			String match = words.get(-n - 1);
			if (match.length() > prefixLength) {
				String startOfMatch = match.substring(0, prefixLength);
				if (startOfMatch.equalsIgnoreCase(prefix)) {
					return match.substring(prefixLength);
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
}
