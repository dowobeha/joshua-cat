/* This file is copyright 2010 by Lane O.B. Schwartz */
package joshua.cat.options;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface TextCompletionModel {

	public String complete(String prefix);
	
}

class TranslationOptionsCompletionModel extends ListCompletionModel {

	private static final Logger logger =
		Logger.getLogger(TranslationOptionsCompletionModel.class.getName());
	
	public TranslationOptionsCompletionModel(Locale locale, TranslationOptions... translationsList) {
		this(locale, Arrays.asList(translationsList));
	}
	
	public TranslationOptionsCompletionModel(Locale locale, List<TranslationOptions> translationsList) {
		super(locale, getList(translationsList));
	}
	
	public TranslationOptionsCompletionModel(TranslationOptions... translationsList) {
		this(Arrays.asList(translationsList));
	}
	
	public TranslationOptionsCompletionModel(List<TranslationOptions> translationsList) {
		this(Locale.getDefault(),translationsList);
	}
	
	private static List<String> getList(List<TranslationOptions> translationsList) {
		
		TreeSet<String> words = new TreeSet<String>();
		
		for (TranslationOptions translations : translationsList) {
			for (String phrase : translations.getAllTargets()) {
				for (String word : phrase.split("\\s+")) {
					words.add(word);
				}
			}
		}
		
		List<String> result = new ArrayList<String>(words);
		
		if (logger.isLoggable(Level.FINEST)) {
			for (String phrase : result) {
				logger.finest(phrase);
			}
		}
		
		return result;
	}
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
