
import java.io.*;

public class test1 {
     
     private FileInputStream inputStream;
     private int ColNo;
     private static long[] arr = new long[16];
     private static  byte[] bytes = new byte[8];
     
     
     public test1(FileInputStream inputStream, int colNum){
     this.inputStream = inputStream;
     ColNo=colNum;
     
     
     
     }
       
	  public static void  readHex(final FileInputStream inputStream,final int numberOfColumns) throws Exception{
         // long streamPtr=0;
          while (inputStream.available() > 0) { 
            for(int i=0; i<1; i++){
               
               int line = inputStream.read();
           //    byte fileContent[] = new byte[(int)inputStream.length()];
               
               
           //    // arr[i]= line;
//               String s = Integer.toHexString(line);
//               String  er = "0" + Integer.toHexString(s);
               System.out.printf("%02X ",line);
//                 arr[i]= er;
            }
            
          //  System.out.printf("%02x ", arr.toString());
            
            
          
         // final long col = streamPtr++ % numberOfColumns;
          //System.out.printf("%02x ",inputStream.read());
        //  System.out.printf();
         // if (col == (numberOfColumns-1)) {
        //  System.out.printf("\n");
        }
     //   System.out.printf( arr.toString());
      }
    // }
     
     
     public static void main(String[] args){
     
   //  File hexfile = new File("OS1testsequence.txt"); 
   try {
     FileInputStream f = new FileInputStream("OS1testsequence.txt");
     test1 read = new test1(f,128);
     readHex(f,128);
     }
  catch (Exception e){
      System.out.println("file not found");      
      }
     
          
     } 

	}
