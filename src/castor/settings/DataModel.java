package castor.settings;

import java.util.List;

import castor.language.Mode;

public class DataModel {

	private Mode modeH;
	private List<Mode> modesB;
	private String spName;
	
	public DataModel(Mode modeH, List<Mode> modesB, String spName) {
		super();
		this.modeH = modeH;
		this.modesB = modesB;
		this.spName = spName;
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
