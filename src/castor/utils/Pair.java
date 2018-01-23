package castor.utils;

public class Pair<X, Y> {
	private X a;

	private Y b;

	public Pair(X a, Y b) {
		this.a = a;
		this.b = b;
	}

	public X getFirst() {
		return a;
	}

	public Y getSecond() {
		return b;
	}

	public void setFirst(X a) {
		this.a = a;
	}

	public void setSecond(Y b) {
		this.b = b;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Pair<?, ?>) {
			Pair<?, ?> p = (Pair<?, ?>) o;
			return a.equals(p.a) && b.equals(p.b);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return a.hashCode() + 31 * b.hashCode();
	}

	@Override
	public String toString() {
		return "< " + getFirst().toString() + " , " + getSecond().toString()
				+ " > ";
	}
}

