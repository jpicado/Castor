package castor.language;

import java.util.LinkedList;
import java.util.List;

public class Mode {

	private String predicateName;
	private List<Argument> arguments;
	
	public Mode(String predicateName, List<Argument> arguments) {
		super();
		this.predicateName = predicateName;
		this.arguments = arguments;
	}
	
	public String getPredicateName() {
		return predicateName;
	}
	
	public List<Argument> getArguments() {
		return arguments;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(predicateName + "(");
		for (int i = 0; i < arguments.size(); i++) {
			String mode = "";
			if(arguments.get(i).getIdentifierType().equals(IdentifierType.CONSTANT)) {
				mode += "#";
			} else if (arguments.get(i).getIdentifierType().equals(IdentifierType.INPUT)) {
				mode += "+";
			} else {
				mode += "-";
			}
			mode += arguments.get(i).getType();
			if (i < arguments.size() - 1) {
				mode += ",";
			}
			sb.append(mode);
		}
		sb.append(")");
		return sb.toString();
	}
	
	/*
	 * Output mode, where all identifier types (access modes) are converted to constant (#)
	 */
	public Mode toGroundMode() {
		String predicateName =  this.predicateName;
		List<Argument> arguments = new LinkedList<Argument>();
		for (Argument argument : this.arguments) {
			arguments.add(new Argument(argument.getType(), IdentifierType.CONSTANT));
		}
		return new Mode(predicateName, arguments);
	}
	
	/*
	 * Output string of mode, where all identifier types (access modes) are converted to constant (#)
	 */
	public String toGroundModeString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(predicateName + "(");
		for (int i = 0; i < arguments.size(); i++) {
			String mode = "#"+arguments.get(i).getType();
			if (i < arguments.size() - 1) {
				mode += ",";
			}
			sb.append(mode);
		}
		sb.append(")");
		return sb.toString();
	}
	
	/*
	 * Output string for mode without identifier types (access modes)
	 */
	public String toStringWithoutAccessModes() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(predicateName + "(");
		for (int i = 0; i < arguments.size(); i++) {
			String mode = arguments.get(i).getType();
			if (i < arguments.size() - 1) {
				mode += ",";
			}
			sb.append(mode);
		}
		sb.append(")");
		return sb.toString();
	}
	
	/*
	 * Output mode containing only identifier types (access modes) for input attributes (+)
	 */
	public String toStringOnlyInputAccessModes() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(predicateName + "(");
		for (int i = 0; i < arguments.size(); i++) {
			String accessMode = "";
			if (arguments.get(i).getIdentifierType().equals(IdentifierType.INPUT))
				accessMode = "+";
			String mode = accessMode+arguments.get(i).getType();
			if (i < arguments.size() - 1) {
				mode += ",";
			}
			sb.append(mode);
		}
		sb.append(")");
		return sb.toString();
	}
	
	/*
	 * Output mode containing only identifier types (access modes) for input (+) and constant (#) attributes
	 */
	public String toStringOnlyInputOrConstantAccessModes() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(predicateName + "(");
		for (int i = 0; i < arguments.size(); i++) {
			String accessMode = "";
			if (arguments.get(i).getIdentifierType().equals(IdentifierType.INPUT))
				accessMode = "+";
			else 
				if (arguments.get(i).getIdentifierType().equals(IdentifierType.CONSTANT))
				accessMode = "#";
			String mode = accessMode+arguments.get(i).getType();
			if (i < arguments.size() - 1) {
				mode += ",";
			}
			sb.append(mode);
		}
		sb.append(")");
		return sb.toString();
	}
	
	/*
	 * Convert mode string to mode object
	 */
	public static Mode stringToMode(String modeString) {
		String[] modeTokens = modeString.split("\\(|\\)");
		String modePredicate = modeTokens[0];
		String[] modeArguments = modeTokens[1].split(",|\\s+");
		List<Argument> arguments = new LinkedList<Argument>();
		for (int i = 0; i < modeArguments.length; i++) {
			arguments.add(new Argument(getArgumentType(modeArguments[i]), toIdentifierType(getArgumentIdentifierType(modeArguments[i]))));			
		}
		Mode mode = new Mode(modePredicate, arguments);
		return mode;
	}
	
	/*
	 * Get identifier type: +, - or #
	 */
	private static String getArgumentIdentifierType(String argument) {
		return argument.substring(0, 1);
	}

	/*
	 * Get argument type: from +type, extract type
	 */
	private static String getArgumentType(String argument) {
		return argument.substring(1, argument.length());
	}
	
	/*
	 * Convert string identifier type to corresponding enum
	 */
	private static IdentifierType toIdentifierType(String type) {
		if (type.equals("+"))
			return IdentifierType.INPUT;
		else if (type.equals("-"))
			return IdentifierType.OUTPUT;
		else
			return IdentifierType.CONSTANT;
	}
}
