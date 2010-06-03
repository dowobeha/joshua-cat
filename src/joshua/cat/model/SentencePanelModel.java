/* This file is copyright 2010 by Lane O.B. Schwartz */
package joshua.cat.model;

import java.util.Arrays;
import java.util.Collection;

import joshua.cat.TranslationOptions;

import com.google.common.base.Preconditions;

public class SentencePanelModel {

	private final String sourceSentence;
	private final String[] sourceWords;
	
	private final ComboBoxModel[][] models;
	private final int numRows;
	
	public SentencePanelModel(String sourceSentence, int spanLimit, TranslationOptions... translationsList) {
		this(sourceSentence, spanLimit, Arrays.asList(translationsList));
	}
	
	public SentencePanelModel(String sourceSentence, int spanLimit, final Collection<TranslationOptions> translationsList) {
		Preconditions.checkNotNull(sourceSentence);
		Preconditions.checkNotNull(translationsList);
		
		this.sourceSentence = sourceSentence;
		this.sourceWords = sourceSentence.split("\\s+");

		numRows=Math.min(sourceWords.length, spanLimit);

		// Initialize chart model
		{
			models = new ComboBoxModel[numRows][];

			// Add rows to chart model
			for (int row=0; row<numRows; row+=1) {

				int numCells=sourceWords.length-row;
				models[row] = new ComboBoxModel[numCells];

				// Add cells to chart model
				for (int cell=0; cell<numCells; cell+=1) {

					StringBuilder sourcePhraseBuilder = new StringBuilder();
					for (int wordIndex=cell, lastIndex=cell+row; wordIndex<=lastIndex; wordIndex++) {
						if (wordIndex>cell) {
							sourcePhraseBuilder.append(' ');
						}
						sourcePhraseBuilder.append(sourceWords[wordIndex]);
					}
					ComboBoxModel model = new ComboBoxModel(sourcePhraseBuilder.toString(), translationsList);
					models[row][cell] = model;

				}

			}
		}

	}
	
	public String[] getWords() {
		return sourceWords;
	}
	
	public String getWord(int index) {
		Preconditions.checkArgument(index < sourceWords.length);
		
		return sourceWords[index];
	}
	
	public ComboBoxModel getComboBoxModel(int row, int cell) {
		return models[row][cell];
	}
	
	public int getNumRows() {
		return this.numRows;
	}
	
	public String getSentence() {
		return this.sourceSentence;
	}
	
}
