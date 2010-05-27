package joshua.util;

public enum Parity {
	
	ODD,
	EVEN;
	
	public Parity next() {
		if (this==ODD) {
			return EVEN;
		} else {
			return ODD;
		}
	}
}