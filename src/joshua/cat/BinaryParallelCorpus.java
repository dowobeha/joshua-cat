/* This file is copyright 2010 by Lane O.B. Schwartz */
package joshua.cat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import joshua.corpus.suffix_array.ParallelCorpusGrammarFactory;
import joshua.corpus.vocab.SymbolTable;
import joshua.decoder.ff.tm.Rule;
import joshua.decoder.ff.tm.Trie;
import joshua.prefix_tree.ExtractRules;
import joshua.prefix_tree.PrefixTree;


public class BinaryParallelCorpus implements Iterable<PhrasePair> {

	private final Trie grammar;
	private final SymbolTable vocab;
	
	public BinaryParallelCorpus(String joshDir, SourceText sourceText) throws IOException, ClassNotFoundException {
		this(getDefaultExtractRules(joshDir),sourceText);
	}
	
	private static ExtractRules getDefaultExtractRules(String joshDir) {
		ExtractRules extractRules = new ExtractRules();
		extractRules.setMaxNonterminals(0);
		extractRules.setMaxPhraseLength(5);
		extractRules.setMaxPhraseSpan(5);
		extractRules.setJoshDir(joshDir);
		return extractRules;
	}
	
	public BinaryParallelCorpus(ExtractRules extractRules, SourceText sourceText) throws IOException, ClassNotFoundException  {
		
		ParallelCorpusGrammarFactory parallelCorpus = extractRules.getGrammarFactory();
		this.vocab = parallelCorpus.getSuffixArray().getVocabulary();
		PrefixTree prefixTree = new PrefixTree(parallelCorpus);
		
		// Process each sentence
		for (String sentence : sourceText) {
			String[] words = sentence.split("\\s+");
			int[] wordIDs = vocab.addTerminals(words);
			prefixTree.add(wordIDs);
		}
		
		this.grammar = prefixTree.getTrieRoot();
	}
	
	private void gatherAllNodes(Trie node, List<Trie> nodeList) {
		if (! node.getRules().getRules().isEmpty()) {
			nodeList.add(node);
		}
		
		if (node.hasExtensions()) {
			for (Trie child : node.getExtensions()) {
				gatherAllNodes(child, nodeList);
			}			
		}
	}
	
	@Override
	public Iterator<PhrasePair> iterator() {
		
		final ArrayList<Trie> nodeList = new ArrayList<Trie>();
		gatherAllNodes(grammar, nodeList);
		
		
		return new Iterator<PhrasePair>() {

			Iterator<Trie> nodeIterator = nodeList.iterator();
			Iterator<Rule> ruleIterator = nodeIterator.hasNext()
				? nodeIterator.next().getRules().getRules().iterator()
				: Collections.<Rule>emptyList().iterator();
					
			@Override
			public boolean hasNext() {
				return ruleIterator.hasNext() || nodeIterator.hasNext();
			}

			@Override
			public PhrasePair next() {
				if (! ruleIterator.hasNext()) {
					ruleIterator = nodeIterator.hasNext()
					? nodeIterator.next().getRules().getRules().iterator()
					: Collections.<Rule>emptyList().iterator();
				}
				
				Rule rule = ruleIterator.next();
				int[] sourceIDs = rule.getFrench();
				int[] targetIDs = rule.getEnglish();
				return new PhrasePair(vocab.getWords(sourceIDs), vocab.getWords(targetIDs));
				
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		};
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String joshDir = "/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es-en.10000.josh";
		SourceText sourceText = new SourceText("/Users/lane/Research/brainstorm/Joshua Scratch/data/europarl.es.10");
		
		BinaryParallelCorpus corpus = new BinaryParallelCorpus(joshDir, sourceText);
		
		for (PhrasePair phrasePair : corpus) {
			System.out.println(phrasePair);
		}
		
	}
	
}
