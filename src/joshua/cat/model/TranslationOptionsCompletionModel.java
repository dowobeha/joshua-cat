package joshua.cat.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import joshua.cat.TranslationOptions;

public class TranslationOptionsCompletionModel extends ListCompletionModel {

	private static final Logger logger =
		Logger.getLogger(TranslationOptionsCompletionModel.class.getName());
	
	private final Collection<TranslationOptions> translationsList;
	
	public Collection<TranslationOptions> getTranslationOptions() {
		return this.translationsList;
	}
	
	public TranslationOptionsCompletionModel(Locale locale, TranslationOptions... translationsList) {
		this(locale, Arrays.asList(translationsList));
	}
	
	public TranslationOptionsCompletionModel(Locale locale, Collection<TranslationOptions> translationsList) {
		super(locale, getList(translationsList));
		this.translationsList = translationsList;
	}
	
	public TranslationOptionsCompletionModel(TranslationOptions... translationsList) {
		this(Arrays.asList(translationsList));
	}
	
	public TranslationOptionsCompletionModel(Collection<TranslationOptions> translationsList) {
		this(Locale.getDefault(),translationsList);
	}
	
	private static List<String> getList(Collection<TranslationOptions> translationsList) {
		
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