
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class last{


	public static void main(String[] args) {
		Path path = Paths.get("OS1testsequence.txt");
      try{
		byte[] byteArray= Files.readAllBytes(path);
		ByteBuffer bbuffer = ByteBuffer.wrap(byteArray);
		short numS = bbuffer.getShort();
		System.out.println("short: " + bbuffer.toString());
      }
      catch (Exception e ){
         System.out.println("404: File not found");
      }

	}



}