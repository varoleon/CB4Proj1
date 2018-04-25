package app;

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

	public void setBody(String body) {
		this.body = body;
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

	public void writeToFile(String filepath) {
		File file = new File(filepath);

		if (!file.exists()) {
			try {
				file.createNewFile();

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error with file or conversation directory. Check Config class");

			}
		}

		FileWriter fw;
		BufferedWriter bw = null;
		try {

			fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);

			
			if (isResponse) {
				String identation = "\t\t\t\t";
				if (editedBy != null) {
					bw.append(identation + "---EDITED by " + editedBy + "---\n");
				}
				bw.append(identation + this.toString().replace("\n", "\n" + identation));
				bw.append("\n" + identation + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
			} else {
				if (editedBy != null) {
					bw.append("---EDITED by " + editedBy + "---\n");
				}
				bw.append(this.toString());
				bw.append("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("File error");
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
