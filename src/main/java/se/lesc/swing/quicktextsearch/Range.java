package se.lesc.swing.quicktextsearch;

class Range implements Comparable<Range> {
	final int from;
	final int to;

	Range(int from, int to) {
		this.from = from;
		this.to = to;
	}

	/** Joins to ranges. Should only be called if the ranges intersects */
	static Range join(Range a1, Range a2) {

		int lowerBounds = Math.min(a1.from, a2.from);
		int upperBounds = Math.max(a1.to, a2.to);

		return new Range(lowerBounds, upperBounds);
	}
		
	boolean intersects(Range o) {
		if (inside(o.from) || inside(o.to) || o.inside(from) || o.inside(to)) {
			return true;
		}
		
		return false;
	}

	boolean inside(int point) {
		if (point >= from && point <= to) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "[" + from + ", " + to + "]";
	}
	


	@Override
	public int compareTo(Range o) {
		return Integer.valueOf(from).compareTo(Integer.valueOf(o.from));
	}
	
	//Generated by Eclipse
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + from;
		result = prime * result + to;
		return result;
	}

	//Generated by Eclipse
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Range other = (Range) obj;
		if (from != other.from)
			return false;
		if (to != other.to)
			return false;
		return true;
	}
	
}
