package joshua.cat;

import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import joshua.corpus.vocab.SymbolTable;
import joshua.decoder.ff.tm.Rule;
import joshua.decoder.ff.tm.RuleCollection;
import joshua.decoder.ff.tm.Trie;

@SuppressWarnings("serial")
public class TranslationOptionsModel extends AbstractListModel implements MutableComboBoxModel {

	private String[] values;
	
	private int selectedIndex;
	
	public TranslationOptionsModel(
			Trie trieNode, SymbolTable vocab) {
		
		if (trieNode == null) {
			values = new String[0];
			this.selectedIndex = -1;
		} else {
			this.selectedIndex = 0;
			RuleCollection ruleCollection = trieNode.getRules();
			List<Rule> rules = ruleCollection.getRules();
			values = new String[rules.size()];
			for (int i=0, n=rules.size(); i<n; i++) {
				int[] translation = rules.get(i).getEnglish();
				values[i] = vocab.getWords(translation);
			}
		}
	}
	
	@Override
	public void addElement(Object obj) {
		this.selectedIndex = Arrays.binarySearch(values, obj);
		if (this.selectedIndex < 0) {
			this.selectedIndex = -1 * this.selectedIndex;
		}
		this.values[this.selectedIndex] = obj.toString();

		notifyListeners(this.selectedIndex);
	}

	@Override
	public void insertElementAt(Object obj, int index) {
		this.values[index] = obj.toString();
		this.selectedIndex = index;
		notifyListeners(this.selectedIndex);
	}

	@Override
	public void removeElement(Object obj) {
		this.selectedIndex = Arrays.binarySearch(values, obj);
		if (this.selectedIndex >= 0) {
			this.values[this.selectedIndex] = null;
			notifyListeners(this.selectedIndex);
		}
	}

	@Override
	public void removeElementAt(int index) {
		this.values[index] = null;
		notifyListeners(index);
	}

	@Override
	public Object getSelectedItem() {
		if (selectedIndex < 0 || selectedIndex >= values.length) {
			return null;
		} else {
			return values[selectedIndex];
		}
	}

	@Override
	public void setSelectedItem(Object anItem) {
		this.selectedIndex = Arrays.binarySearch(values, anItem);
		notifyListeners(this.selectedIndex);
	}

	@Override
	public Object getElementAt(int index) {
		return values[index];
	}

	@Override
	public int getSize() {
		return values.length;
	}

	private void notifyListeners(int index) {
		ListSelectionEvent e = new ListSelectionEvent(this,index,index,false);
		for (ListSelectionListener l : this.listenerList.getListeners(ListSelectionListener.class)) {
			l.valueChanged(e);
		}
	}
}
