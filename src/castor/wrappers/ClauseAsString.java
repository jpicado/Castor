package castor.wrappers;

import java.util.List;

public class ClauseAsString {

	private String head;
	private List<String> body;
	public ClauseAsString(String head, List<String> body) {
		super();
		this.head = head;
		this.body = body;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public List<String> getBody() {
		return body;
	}
	public void setBody(List<String> body) {
		this.body = body;
	}
}
