package p0;

import java.sql.Timestamp;

public class Message {
	protected User sender;
	protected User receiver;
	protected String body;
	protected Timestamp timestamp;
	
	
	public Message(User sender, User receiver, String body) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.body = body;
		timestamp = new Timestamp(System.currentTimeMillis());
		
	}

	@Override
	public String toString() {
		return "Message from:" + sender.username + ", to:" + receiver.username + "\n sent on:" + timestamp+"\n\t:" + body + "]";
	}
	public void saveToLog() {
		WriteToFile.save(this,Config.MESSAGE_LOG);
	}
	public void saveToSenderReceiverFile() {
		WriteToFile.save(this, Config.CONVERSATION_FOLDER+this.sender.username+"_to_"+this.receiver.username+".txt");
	}
	public void storeToDb() {
		Database db = Database.getDbInst();
		db.insertNewMessage(sender.id,receiver.id,body,timestamp);
	}
}
