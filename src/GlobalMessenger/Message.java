package GlobalMessenger;

/**
 *
 * Message java object
 * (exchanged among Nodes through the GlobalMessenger)
 * 
 * @author Diego Pizzocaro
 * 
 */
public class Message {
	
	Node sender;
	Node receiver;
	Object content;
	double timestamp;
	
	public Message(Node sender, Node receiver, Object content, double timestamp) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.timestamp = timestamp;
	}
	
	public Node getSender() {
		return sender;
	}
	public void setSender(Node sender) {
		this.sender = sender;
	}
	public Node getReceiver() {
		return receiver;
	}
	public void setReceiver(Node receiver) {
		this.receiver = receiver;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public double getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
