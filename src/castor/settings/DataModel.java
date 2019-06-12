package castor.settings;

import castor.language.Mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataModel {

	private Mode modeH;
	private List<Mode> modesB;
	private String spName;
	private Map<String, List<List<String>>> modesBMap;

	public DataModel(Mode modeH, List<Mode> modesB, String spName) {
		super();
		this.modeH = modeH;
		this.modesB = modesB;
		this.spName = spName;
	}

	public DataModel(Mode modeH, List<Mode> modesB, Map<String, List<List<String>>> modesBMap, String spName) {
		super();
		this.modeH = modeH;
		this.modesB = modesB;
		this.modesBMap = modesBMap;
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

	public Map<String, List<List<String>>> getModesBMap() {
		return modesBMap;
	}

	public void setModesBMap(Map<String, List<List<String>>> modesBMap) {
		this.modesBMap = modesBMap;
	}

	public List<String> getModesBString(){
		List<String> modesList = new ArrayList<>();
		for(Mode mode:  this.modesB){
			modesList.add(mode.toString());
		}
		return modesList;
	}
}
