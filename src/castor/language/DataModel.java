package castor.language;

import java.util.List;

public class DataModel {

	private String target;
	private Mode modeH;
	private List<Mode> modesB;
	private String spName;
	
	public DataModel(String target, Mode modeH, List<Mode> modesB,
			String spName) {
		super();
		this.target = target;
		this.modeH = modeH;
		this.modesB = modesB;
		this.spName = spName;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public Mode getModeH() {
		return modeH;
	}
	public void setModeH(Mode modeH) {
		this.modeH = modeH;
	}
	public List<Mode> getModesB() {
		return modesB;
	}
	public void setModesB(List<Mode> modesB) {
		this.modesB = modesB;
	}
	public String getSpName() {
		return spName;
	}
	public void setSpName(String spName) {
		this.spName = spName;
	}
}
