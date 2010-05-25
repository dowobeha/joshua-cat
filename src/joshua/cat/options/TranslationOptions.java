package joshua.cat.options;


import good.maybe.TranslationOptionsListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;



import com.google.common.collect.ArrayListMultimap;


public class TranslationOptions {

	private final ArrayListMultimap<String,String> sourceToTarget;
	
	private final Set<TranslationOptionsListener> listeners;
	
	public TranslationOptions(Iterable<PhrasePair> phrasePairs) {
		this.listeners = new HashSet<TranslationOptionsListener>();
		this.sourceToTarget = ArrayListMultimap.create();
		
		for (PhrasePair phrasePair : phrasePairs) {
			sourceToTarget.put(phrasePair.getSource(), phrasePair.getTarget());
		}
	}
	
	
	public List<String> get(String sourcePhrase) {
		return sourceToTarget.get(sourcePhrase);
	}
	
	public void registerListener(TranslationOptionsListener l) {
		listeners.add(l);
	}
}

