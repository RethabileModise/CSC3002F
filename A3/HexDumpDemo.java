import java.io.FileInputStream;
impor

public class HexDumpDemo {
    public static void main(String[] args) throws Exception {
    int bytesRead = 0;
        // Open the file using FileInputStream
        try (FileInputStream fis = new FileInputStream("OS1testsequence.txt")) {
            // A variable to hold a single byte of the file data
            int i = 0;

            // A counter to print a new line every 16 bytes read.
            int count = 0;

            // Read till the end of the file and print the byte in hexadecimal
            // valueS.
            while ((i = fis.read()) != -1) {
            String format = String.format("%02x ",i & 0xFF);
            Long j = Long.parseLong(format);
                System.out.print(format);
                System.out.print(j);
                
                count++;

                if (count == 8) {
                    System.out.println("");
                    count = 0;
                }
            }
        }
    }
    
 /**
   * Byte swap a single long value.
   * 
   * @param value  Value to byte swap.
   * @return       Byte swapped representation.
   */
  public static long swap (String format)
   {
    long value = Long.parseLong(format);
    long b1 = (value >>  0) & 0xff;
    long b2 = (value >>  8) & 0xff;
    long b3 = (value >> 16) & 0xff;
    long b4 = (value >> 24) & 0xff;
    long b5 = (value >> 32) & 0xff;
    long b6 = (value >> 40) & 0xff;
    long b7 = (value >> 48) & 0xff;
    long b8 = (value >> 56) & 0xff;

    return b1 << 56 | b2 << 48 | b3 << 40 | b4 << 32 |
           b5 << 24 | b6 << 16 | b7 <<  8 | b8 <<  0;
  }
  
  static String hexToBin(String s) {
  return new BigInteger(s, 16).toString(2);
  }
  
  /**
	 * Extract the page number.
	 */
	public int getPageNumber(int virtualAddress) {
		return (virtualAddress & 0x0000ff00) >> 8;
	}

	/**
	 * Extract the offset.
	 */
	public int getOffset(int virtualAddress) {
		return (virtualAddress & 0x000000ff);
	}
   
   
   

  
  
  
     
    
}