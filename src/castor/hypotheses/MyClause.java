package castor.hypotheses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.Term;

public class MyClause {

	private static MyLiteralsSorter _literalSorter = new MyLiteralsSorter();
	//
	private final Set<Literal> literals = new LinkedHashSet<Literal>();
	private final List<Literal> positiveLiterals = new ArrayList<Literal>();
	private final List<Literal> negativeLiterals = new ArrayList<Literal>();
	
	public MyClause() {
		// i.e. the empty MyClause
	}

	public MyClause(List<Literal> lits) {
		this.literals.addAll(lits);
		for (Literal l : literals) {
			if (l.isPositiveLiteral()) {
				this.positiveLiterals.add(l);
			} else {
				this.negativeLiterals.add(l);
			}
		}
//		recalculateIdentity();
	}
	
	public MyClause(Set<Literal> lits) {
		this.literals.addAll(lits);
		for (Literal l : literals) {
			if (l.isPositiveLiteral()) {
				this.positiveLiterals.add(l);
			} else {
				this.negativeLiterals.add(l);
			}
		}
//		recalculateIdentity();
	}
	
	public boolean isEmpty() {
		return literals.size() == 0;
	}

	public boolean isUnitClause() {
		return literals.size() == 1;
	}

	public boolean isDefiniteClause() {
		// A Definite MyClause is a disjunction of literals of which exactly 1 is
		// positive.
		return !isEmpty() && positiveLiterals.size() == 1;
	}

	public boolean isImplicationDefiniteClause() {
		// An Implication Definite MyClause is a disjunction of literals of
		// which exactly 1 is positive and there is 1 or more negative
		// literals.
		return isDefiniteClause() && negativeLiterals.size() >= 1;
	}

	public boolean isHornClause() {
		// A Horn MyClause is a disjunction of literals of which at most one is
		// positive.
		return !isEmpty() && positiveLiterals.size() <= 1;
	}

	public boolean isTautology() {

		for (Literal pl : positiveLiterals) {
			// Literals in a MyClause must be exact complements
			// for tautology elimination to apply. Do not
			// remove non-identical literals just because
			// they are complements under unification, see pg16:
			// http://logic.stanford.edu/classes/cs157/2008/notes/chap09.pdf
			for (Literal nl : negativeLiterals) {
				if (pl.getAtomicSentence().equals(nl.getAtomicSentence())) {
					return true;
				}
			}
		}

		return false;
	}

	public void addLiteral(Literal literal) {
//		if (isImmutable()) {
//			throw new IllegalStateException(
//					"MyClause is immutable, cannot be updated.");
//		}
		int origSize = literals.size();
		literals.add(literal);
		if (literals.size() > origSize) {
			if (literal.isPositiveLiteral()) {
				positiveLiterals.add(literal);
			} else {
				negativeLiterals.add(literal);
			}
		}
//		recalculateIdentity();
	}

	public void addPositiveLiteral(AtomicSentence atom) {
		addLiteral(new Literal(atom));
	}

	public void addNegativeLiteral(AtomicSentence atom) {
		addLiteral(new Literal(atom, true));
	}

	public int getNumberLiterals() {
		return literals.size();
	}

	public int getNumberPositiveLiterals() {
		return positiveLiterals.size();
	}

	public int getNumberNegativeLiterals() {
		return negativeLiterals.size();
	}

	public Set<Literal> getLiterals() {
		return Collections.unmodifiableSet(literals);
	}

	public List<Literal> getPositiveLiterals() {
		return Collections.unmodifiableList(positiveLiterals);
	}

	public List<Literal> getNegativeLiterals() {
		return Collections.unmodifiableList(negativeLiterals);
	}
	
	@Override
	public String toString() {
		List<Literal> sortedLiterals = new ArrayList<Literal>(literals);
		Collections.sort(sortedLiterals, _literalSorter);
		return sortedLiterals.toString();
	}
	
	public String toString2(String positiveSymbol, String negateSymbol) {
		// This format is used by CoverageBySubsumption in Castor
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < positiveLiterals.size(); i++) {
			sb.append(positiveSymbol+positiveLiterals.get(i).getAtomicSentence().toString()+",");
		}
		for (int i = 0; i < negativeLiterals.size(); i++) {
			sb.append(negateSymbol+negativeLiterals.get(i).getAtomicSentence().toString());
			if (i < negativeLiterals.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	public String bodyToString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < negativeLiterals.size(); i++) {
			sb.append(negativeLiterals.get(i).getAtomicSentence().toString());
			if (i < negativeLiterals.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(literals);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyClause other = (MyClause) obj;
		if (literals == null) {
			if (other.literals != null)
				return false;
		} else if (!literals.equals(other.literals))
			return false;
		return true;
	}
}

class MyLiteralsSorter implements Comparator<Literal> {
	public int compare(Literal o1, Literal o2) {
		int rVal = 0;
		// If literals are not negated the same
		// then positive literals are considered
		// (by convention here) to be of higher
		// order than negative literals
		if (o1.isPositiveLiteral() != o2.isPositiveLiteral()) {
			if (o1.isPositiveLiteral()) {
				return 1;
			}
			return -1;
		}

		// Check their symbolic names for order first
		rVal = o1.getAtomicSentence().getSymbolicName()
				.compareTo(o2.getAtomicSentence().getSymbolicName());

		// If have same symbolic names
		// then need to compare individual arguments
		// for order.
		if (0 == rVal) {
			rVal = compareArgs(o1.getAtomicSentence().getArgs(), o2
					.getAtomicSentence().getArgs());
		}

		return rVal;
	}

	private int compareArgs(List<Term> args1, List<Term> args2) {
		int rVal = 0;

		// Compare argument sizes first
		rVal = args1.size() - args2.size();

		if (0 == rVal && args1.size() > 0) {
			// Move forward and compare the
			// first arguments
			Term t1 = args1.get(0);
			Term t2 = args2.get(0);

			if (t1.getClass() == t2.getClass()) {
				// Note: Variables are considered to have
				// the same order
				if (t1 instanceof Constant) {
					rVal = t1.getSymbolicName().compareTo(t2.getSymbolicName());
				} else if (t1 instanceof Function) {
					rVal = t1.getSymbolicName().compareTo(t2.getSymbolicName());
					if (0 == rVal) {
						// Same function names, therefore
						// compare the function arguments
						rVal = compareArgs(t1.getArgs(), t2.getArgs());
					}
				}

				// If the first args are the same
				// then compare the ordering of the
				// remaining arguments
				if (0 == rVal) {
					rVal = compareArgs(args1.subList(1, args1.size()),
							args2.subList(1, args2.size()));
				}
			} else {
				// Order for different Terms is:
				// Constant > Function > Variable
				if (t1 instanceof Constant) {
					rVal = 1;
				} else if (t2 instanceof Constant) {
					rVal = -1;
				} else if (t1 instanceof Function) {
					rVal = 1;
				} else {
					rVal = -1;
				}
			}
		}

		return rVal;
	}
}
