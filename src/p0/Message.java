package p0;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class Message {
	protected User sender;
	protected User receiver;
	protected String body;
	protected Timestamp timestamp;
	private boolean isResponse = false;

	public Message(User sender, User receiver, String body) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.body = body;
		timestamp = new Timestamp(System.currentTimeMillis());

	}

	@Override
	public String toString() {
		return "Message From: " + sender.username + "\t  To: " + receiver.username + "\nSent on: " + timestamp + "\n\t"
				+ body;
	}

	public void saveToLog() {
		writeToFile(Config.MESSAGE_LOG);
	}

	public void saveToSenderReceiverFile() {
		if (new File(Config.CONVERSATION_FOLDER + this.receiver.username + "_to_" + this.sender.username + ".txt")
				.exists()) {
			isResponse = true;
			writeToFile(Config.CONVERSATION_FOLDER + this.receiver.username + "_to_" + this.sender.username + ".txt");
		} else {
			writeToFile(Config.CONVERSATION_FOLDER + this.sender.username + "_to_" + this.receiver.username + ".txt");
		}
	}

	public void storeToDb() {
		Database db = Database.getDbInst();
		db.insertNewMessage(sender.id, receiver.id, body, timestamp);
	}

	private void writeToFile(String filepath) {
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
