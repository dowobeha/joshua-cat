package joshua.cat;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

import joshua.corpus.suffix_array.ParallelCorpusGrammarFactory;
import joshua.corpus.vocab.SymbolTable;
import joshua.decoder.ff.tm.Rule;
import joshua.decoder.ff.tm.RuleCollection;
import joshua.decoder.ff.tm.Trie;
import joshua.prefix_tree.PrefixTree;

@SuppressWarnings("serial")
public class TranslationOptionsModel extends AbstractListModel implements MutableComboBoxModel {

	public TranslationOptionsModel(
			ParallelCorpusGrammarFactory parallelCorpus, 
			String... sourcePhrase) {
		
		SymbolTable vocab = parallelCorpus.getSuffixArray().getVocabulary();
		int[] sentence = vocab.addTerminals(sourcePhrase);
		
		PrefixTree prefixTree = new PrefixTree(parallelCorpus);
		prefixTree.add(sentence);
		
		Trie grammar = prefixTree.getTrieRoot();
		
		Trie trieNode = grammar;
		for (String sourceWord : sourcePhrase) {
			if (trieNode==null) {
				break;
			} else {
				trieNode = trieNode.matchOne(vocab.addTerminal(sourceWord));
			}
		}
		
		String[] strings;
		if (trieNode == null) {
			strings = new String[0];
		} else {
			RuleCollection ruleCollection = trieNode.getRules();
			List<Rule> rules = ruleCollection.getRules();
			strings = new String[rules.size()];
			for (int i=0, n=rules.size(); i<n; i++) {
				int[] translation = rules.get(i).getEnglish();
				strings[i] = vocab.getWords(translation);
			}
		}
	}
	
	@Override
	public void addElement(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertElementAt(Object obj, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeElement(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeElementAt(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getSelectedItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelectedItem(Object anItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getElementAt(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
