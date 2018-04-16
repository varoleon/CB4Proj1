package p0;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import users.User;

public class Message {
	protected User sender;
	protected User receiver;
	protected String body;
	protected Timestamp timestamp;
	protected int id;
	private boolean isResponse = false;
	private String editedBy = null;

	public Message(User sender, User receiver, String body) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.body = body;
		timestamp = new Timestamp(System.currentTimeMillis());
	}

	@Override
	public String toString() {
		return "Message id:" + id + "\nFrom: " + sender.getUsername() + "\t  To: " + receiver.getUsername()
				+ "\nSent on: " + timestamp + "\n\t" + body;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setEditedBy(String name) {
		this.editedBy = name;
	}
	
	public int getId() {
		return this.id;
	}


	public String getBody() {
		return body;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void saveToLog() {
		writeToFile(Config.MESSAGE_LOG);
	}

	public void saveToSenderReceiverFile() {
		if (new File(
				Config.CONVERSATION_FOLDER + this.receiver.getUsername() + "_to_" + this.sender.getUsername() + ".txt")
						.exists()) {
			isResponse = true;
			writeToFile(Config.CONVERSATION_FOLDER + this.receiver.getUsername() + "_to_" + this.sender.getUsername()
					+ ".txt");
		} else {
			writeToFile(Config.CONVERSATION_FOLDER + this.sender.getUsername() + "_to_" + this.receiver.getUsername()
					+ ".txt");
		}
	}

//	public void storeToDb() {
//		this.id = this.sender.storeMsgToDB(sender.getId(), receiver.getId(), body, timestamp);
//	}

	public void writeToFile(String filepath) {
		File file = new File(filepath);
		BufferedWriter bw = null;

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileWriter fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);

			if (editedBy!=null) {
				bw.append("---UPDATED by "+editedBy+"---\n");
			}
			if (isResponse) {
				String identation = "\t\t\t\t";
				bw.append(identation + this.toString().replace("\n", "\n" + identation));
				bw.append("\n" + identation + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
			} else {
				bw.append(this.toString());
				bw.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (Exception ex) {
				System.out.println("Error in closing the BufferedWriter" + ex);
			}
		}

	}

}
