package castor.settings;

import java.util.List;

public class ModeMetadata {

	private List<String> types;
	private List<String> accessModes;
	
	public ModeMetadata(List<String> types, List<String> accessModes) {
		super();
		this.types = types;
		this.accessModes = accessModes;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public List<String> getAccessModes() {
		return accessModes;
	}

	public void setAccessModes(List<String> accessModes) {
		this.accessModes = accessModes;
	}
}
