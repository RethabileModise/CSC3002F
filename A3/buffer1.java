import java.io.*;

public class buffer1 {


	public static void main(String[] args) {
		
	


File file = new File("OS1testsequence.txt");
StringBuilder builder = new StringBuilder();
try {
    FileInputStream fin = new FileInputStream(file);
    byte[] buffer = new byte[1024];
    int bytesRead = 0;
    while((bytesRead = fin.read(buffer)) > -1)
        for(int i = 0; i < bytesRead; i++)
            builder.append(String.format("%02x", buffer[i] & 0xFF)).append(i != bytesRead - 1 ? " ": "");
            } catch (IOException e) {
    e.printStackTrace();
}

  System.out.print(builder.toString());
}

}