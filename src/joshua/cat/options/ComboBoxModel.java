package joshua.cat.options;

import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;


@SuppressWarnings("serial")
public class ComboBoxModel extends DefaultComboBoxModel {

	private final List<TranslationOptions> translationsList;
	private final String sourcePhrase;
	
	public ComboBoxModel(String sourcePhrase, TranslationOptions... translationsList) {
		this(sourcePhrase,Arrays.asList(translationsList));
	}
	
	public ComboBoxModel(String sourcePhrase, List<TranslationOptions> translationsList) {
		this.translationsList = translationsList;
		this.sourcePhrase = sourcePhrase;
		
		resetContents();
	}

	private void resetContents() {
		this.removeAllElements();
		
		for (TranslationOptions translations : translationsList) {
			for (String targetPhrase : translations.get(sourcePhrase)) {
				this.addElement(targetPhrase);
			}
		}
	}
	
}
