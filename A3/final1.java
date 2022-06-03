import java.io.*;
import java.util.*;
import java.lang.Math;
public class final1 {

	public static void main(String[] args) {
    
   String longg = "";
    

    try {
            DataInputStream input = new DataInputStream(new FileInputStream(
                    "OS1testsequence.txt"));

            while (input.available() > 0) {
              // String hex = String.format("%02x", input.readByte() & 0xFF);
// 
//                System.out.print(Long.toHexString(Long.parseLong(hex)) + ' ');
            //	System.out.println(Long.toHexString(input.readLong()));
             longg = Long.toHexString(input.readLong());
            if (longg.length() < 2){
                longg =  " " + Long.toHexString(input.readLong());
            }
            
         //   int i = Integer.reverseBytes(longg);
            System.out.println(longg);
            
                        


            }

        } catch (IOException e) {
        }




	}
}