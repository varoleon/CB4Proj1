package p0;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {
	

	public static void save(Message m,String filepath) {
		File file = new File(filepath);
		BufferedWriter bw = null;

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			FileWriter fw = new FileWriter(file,true);
			bw = new BufferedWriter(fw);
			
			bw.append(String.format("From: %s , To: %s , Date: %s", m.sender.username,m.receiver.username,m.timestamp ));
			bw.newLine();
			bw.append(m.body);
			bw.newLine();
			bw.append("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
			bw.newLine();
			
			System.out.println("File written Successfully");
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
