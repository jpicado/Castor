package castor.language;


public class Determination {

	private String predicate;
	private int arity;
//	private List<String> attributeNames;
	
	public Determination(String predicate, int arity) {
		super();
		this.predicate = predicate;
		this.arity = arity;
//		this.attributeNames = attributeNames;
	}
	
	public String getPredicate() {
		return predicate;
	}
	
	public int getArity() {
		return arity;
	}
	
//	public List<String> getAttributeNames() {
//		return attributeNames;
//	}
	
	public void printDet(){
		System.out.println("predicate name:" + "\t" + predicate + "\t" + arity);
//		System.out.println("attributes:");
//		for (int i = 0; i < attributeNames.size(); i++)
//			System.out.println(attributeNames.get(i));
	}
}
