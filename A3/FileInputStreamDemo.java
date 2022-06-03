import java.io.IOException;
import java.io.FileInputStream;

public class FileInputStreamDemo {
   public static void main(String[] args) throws IOException {
      FileInputStream fis = null;
   //   ArrayList<> Arr = new ArrayList
      int i = 0;
      String c = "";
      byte[] bs = new byte[8];
     // byte bytes = new byte[128];
      int rev = 0;
      
      try {
         // create new file input stream
         fis = new FileInputStream("OS1testsequence.txt");
         int count = fis.available();
         
         while (count != 0){
         
         // read bytes to the buffer
         i = fis.read(bs);
         count--; 
           
        for(byte b:bs) {
    
         System.out.printf("%02x ",b , "\n");
        //  System.out.println(" ");
         // System.out.printf("%x is reversed to %x ", b, Long.reverseBytes(b));
        } 
        
        
        }
         
      } catch(Exception ex) {
         // if any error occurs
         ex.printStackTrace();
      } finally {
         // releases all system resources from the streams
         if(fis!=null)
            fis.close();
      }
       }

}