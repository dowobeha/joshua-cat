/* This file is copyright 2010 by Lane O.B. Schwartz */
package joshua.cat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


public class SourceText implements Iterable<String> {

	private final List<String> sentences;
		
	public SourceText(String fileName) throws FileNotFoundException {
		this.sentences = new ArrayList<String>();
		
		Scanner scanner = new Scanner(new File(fileName));
		
		while (scanner.hasNextLine()) {
			sentences.add(scanner.nextLine());
		}
		
	}
	
	public Iterator<String> iterator() {
		return this.sentences.iterator();
	}
}
