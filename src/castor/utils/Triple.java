package castor.utils;

import java.util.Objects;

public class Triple<X, Y, Z> {
	private X a;

	private Y b;
	
	private Z c;

	public Triple(X a, Y b, Z c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public X getFirst() {
		return a;
	}

	public Y getSecond() {
		return b;
	}
	
	public Z getThird() {
		return c;
	}

	public void setFirst(X a) {
		this.a = a;
	}

	public void setSecond(Y b) {
		this.b = b;
	}
	
	public void setThird(Z c) {
		this.c = c;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Triple<?, ?, ?>) {
			Triple<?, ?, ?> p = (Triple<?, ?, ?>) o;
			return a.equals(p.a) && b.equals(p.b) && c.equals(p.c);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(a, b, c);
	}

	@Override
	public String toString() {
		return "< " + getFirst().toString() + " , " + getSecond().toString() + " , " + getThird().toString()
				+ " > ";
	}
}

