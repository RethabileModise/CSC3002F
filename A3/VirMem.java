import java.util.*;
import java.io.*;
import java.lang.*;
import java.math.*;

class VirMem{
 //declare data structures where all classes can use.
 long[] PhyMem;
 long[] BitMapFrames;
 long [][] TLB;
 FileWriter fw;
 public VirMem(){}
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 void InitializePM (FileInputStream in){
  // A variable to hold a single byte of the file data
  int i = 0;
  // A counter to print a new line every 16 bytes read.
  int count = 0;
   
   while ((i = fis.read()) != -1) {
       System.out.printf("%02X ", i);
        count++;
   
        if (count == 8) {
           System.out.println("");
           count = 0;
                   } 
  PhyMem = new long[524288];
  BitMapFrames = new long[32];
 // Scanner sc = new Scanner(in);
 // String strs_f = sc.nextLine();
  String [] s_f = strs_f.split(" ");
  BitMapFrames[0] = BitMapFrames[0] | (1<<31) ; //segment table occupies frame 0. mark frame 0 as occupied,
  for (int i = 0 ; i < s_f.length; i = i + 2){    //entries longo segment table
   long s = Long.parseLong(s_f [0 + i]);
   long f = Long.parseLong(s_f [1 + i]);
   entrytoST(s, f);
  }
  String strp_s_f = sc.nextLine();
  String []p_s_f = strp_s_f.split(" ");
 
  for (int i = 0 ; i < p_s_f.length; i = i + 3){  // entries longo page table
   long p = Long.parseLong(p_s_f [0 + i]);
   long s = Long.parseLong(p_s_f [1 + i]);
   long f = Long.parseLong(p_s_f [2 + i]);
   entrytoPT(p, s, f);
  }
 }

 void entrytoST(long s, long f){ // PT of segment s starts at address f (if f == -1 , PT not resident in PM)
  PhyMem[0+(int)s] = f;
  if (f != -1 && f != 0){                   //PT must be in mem && must exist ==> indicate in bitmap that frame is occupied
   long frameno = f/512 ;
   long frameno2 = frameno + 1;           //PT occupies 2 frames
   long longeger_inBitMap = frameno / 32;
   long longeger_inBitMap2 = frameno2 / 32;
   long nst_bit = frameno % 32;
   long nst_bit2 = frameno2 % 32;
   long shift = 31 - nst_bit;  
   long shift2 = 31 - nst_bit2;                                           
   BitMapFrames[(int)longeger_inBitMap] = BitMapFrames[(int)longeger_inBitMap] | (1 << shift) ;
   BitMapFrames[(int)longeger_inBitMap2] = BitMapFrames[(int)longeger_inBitMap2] | (1 << shift2) ;
  }
 }

 void entrytoPT(long p, long s, long f){ //page p of segment s starts at address f ( if f == -1, page not resident in PM)
  long segadd = PhyMem[(int)s];        // access page table of segment
  long pageadd = segadd + p;      // go to corresponding position for p in page table
  PhyMem[(int)pageadd] = f;           // indicate that page starts at f.
  if (f != -1 && f != 0){                   //Page must be in mem && must exist ==> indicate in bitmap that frame is occupied
   long frameno = f/512 ;
   long longeger_inBitMap = frameno / 32;
   long nst_bit = frameno % 32;
   long shift = 31 - nst_bit;                                            
   BitMapFrames[(int)longeger_inBitMap] = BitMapFrames[(int)longeger_inBitMap] | (1 << shift) ; //page data occupies 1 frame
  }
 }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 void translate (InputStream in) throws IOException{
  TLB = new long[4][3];
  for (int i = 0; i < 4; i ++){      // initiate TLB with no entries
    for (int j = 1;  j < 3; j++){
      TLB[i][j] = -1;
    } 
  }

  Scanner trans = new Scanner (in);
  String strop_VA = trans.nextLine();
  String []op_VA = strop_VA.split(" ");
  for (int i = 0 ; i < op_VA.length; i = i + 2){ 
   System.out.println("---get VA to translate---");
   long op = Long.parseLong(op_VA [0 + i]);
   long VirAdd = Long.parseLong(op_VA [1 + i]);
   System.out.println("translating VA");
   va_2_pa_TLB(op, VirAdd);
  }
  System.out.println("END-- NO MORE VA to translate");
 }

 

 void va_2_pa_TLB (long mode, long va) throws IOException {
  
  //obtain sp and w from the corresponding va
  long mask_sp = createMask(4, 22); 
  long sp = (mask_sp & va) >> 9;
  long mask_w = createMask(23, 31);
  long w = (mask_w & va);
  long a = 0;
   //check if sp exists within TLB
   for( int i =0 ; i < 4 ; i++){
    if (TLB[i][1] == sp){
      a=1;
      long address = TLB[i][2] + w;
      fw.write("h "+ address + " ");
      decrementLRU(i);
      break;
    }
   }

  if (a == 0 ){ // no match is found TLB
    System.out.println("no match in TLB");
    Std_VA_translate(mode, sp, w);
  }
 }

 void Std_VA_translate(long mode, long sp, long w) throws IOException{
    long mask_s = createMask(13,21);
    long mask_p = createMask(22,31);
    int s = (int)(mask_s & sp) >> 10;
    long p = (mask_p & sp);
   
    if (mode == 0){ //read
      if(PhyMem[(int)s] == -1 || PhyMem[(int)PhyMem[s] + (int)p] == -1){
        fw.write("pf ");
      }else if(PhyMem[(int)s] == 0 || PhyMem[(int)PhyMem[s] +(int) p] == 0){
        System.out.println("error");
        fw.write("error ");
      }else{
        long pa = PhyMem[(int)PhyMem[s] + (int)p] + (int)w;
        updateTLB(sp, s , p);
        System.out.println("m " + pa + " ");
        fw.write("m " + pa + " ");
      }
    }else{//write
      if(PhyMem[(int)s] == -1 || PhyMem[(int)PhyMem[s] + (int)p] == -1){
        fw.write("pf ");
      }else{
        if(PhyMem[s] == 0){ //ST entry is 0 
          PhyMem[s] = checkbitmap(0); // update address polonging to PT for the Segment;
        }
        if (PhyMem[(int)PhyMem[s] + (int)p] == 0){ //PT entry is 0
          PhyMem[(int)PhyMem[s] + (int) p]  = checkbitmap(1); //allocate new blank page ;
        }
        long pa = PhyMem[(int)PhyMem[s] + (int)p] + (int) w;
        updateTLB(sp, s , p);
        System.out.println("m " + pa + " ");
        fw.write("m " + pa + " ");
      }
    }
 }

 long checkbitmap(long a){
  if ( a == 0){  //ST entry is 0 , find two consecutive empty frame
    long PTadd = 0;
    for (int i = 0 ; i < 32; i++){ //scan trhough the bitmap represented by 32 longegers
      if(BitMapFrames[i] != -1 ){ // there is free frame in this longeger
        long b = BitMapFrames[i];
        long bnext = BitMapFrames[i+1];
        String k = String.format("%32s", Long.toBinaryString(b)).replace(' ', '0');
        String knext = String.format("%32s", Long.toBinaryString(bnext)).replace(' ', '0');
        long c = k.indexOf("00"); //position in longeger of the 2 free frames
        long l = k.lastIndexOf("0");
        long first = knext.indexOf("0");
        if (c != -1){//found the suitable frame
          long shift = 30 - c;
          BitMapFrames[i] = BitMapFrames[i] | (3 << shift); //update bit map
          PTadd = (c + (i * 32)) * 512;
          break;                    //PT starting address
        }
        if (l == 31 && first == 0){ //consecutive frames in different longegers
          BitMapFrames[i] = BitMapFrames[i] | 1;           //update bitmap
          BitMapFrames[i+1] = BitMapFrames[i+1] | (1<<31);
          PTadd = (31 + (i * 32))*  512;   // PT starting address
          break;
        }
      }
    }
    return PTadd;
  }
  else { //PTentry is 0, find an empty frame;
    long Padd = 0;
    for (int j = 0;  j < 32; j ++){ //scan through the bitmap represented by 32 longegers
      if (BitMapFrames[j] != -1){ //there is a free frame in this longeger rep.
        long z = BitMapFrames[j];
        String zstr = String.format("%32s", Long.toBinaryString(z)).replace(' ', '0');
        long y = zstr.indexOf("0"); // position in longeger of the free frame
        long shift = 31 - y;
        BitMapFrames[j] = BitMapFrames[j] | (1 << shift); //update bitmap
        Padd = (y + j*32) * 512;
        break;
      }
    }
    return Padd;
  } 
}

 void updateTLB(long sp, long s, long p){
    for (int i = 0; i < 4; i ++){                
      if (TLB[i][0] == 0 | TLB[i][0] == -1 ){
        TLB[i][1] =(int) sp;
        TLB[i][2] = PhyMem[(int)PhyMem[(int)s] +(int) p];
        for (int j = 0; j < 4; j++){
          if (TLB[j][0] != -1 && TLB[j][0] != 0)
            TLB[j][0]--;
        }
        TLB[i][0] = (int) 3;
        break;
      }
    }
    System.out.println("LRU   sp    f");
    for (int i = 0; i < 4 ; i++){
      for (int j = 0; j < 3; j ++){
       System.out.print(TLB [i][j] + "    ");
      }
      System.out.println("");
    }
}

 long createMask(long a, long b){
    long r = 0;
    for (int i=(int)a; i<=b; i++){
       r |= 1 << 31 - i;
    }
    return r;
  }

 void decrementLRU(int i){
  for (int j = 0 ; j < 4 ; j ++){
    if (TLB[j][0] > TLB [i][0]){
      TLB[j][0] --;
    }
  }
  TLB[(int)i][0] = 3;
 }

//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

 public static void main(String [] args) throws Exception{
  VirMem VM = new VirMem();
  VM.run();
 }

 void run() throws Exception{
  Scanner scfile = new Scanner(System.in);
  System.out.println("Please input path of file to initialize PM");  
  InputStream init = new FileInputStream(scfile.nextLine());
  InitializePM(init);
  
  //print BitMap-------------------------------------------------
  System.out.println("Initial BitMap Mapping: ");
   for(long a : BitMapFrames){
     System.out.println(String.format("%32s", Long.toBinaryString(a)).replace(' ', '0'));
   }
   System.out.println("");
  //-------------------------------------------------------------
   
  //print initialized Physical Memory-----------------
   System.out.println("Physical Memory Allocation Mapping: ");
   for (int i = 0; i < PhyMem.length; i++){
     long a = PhyMem[i];
     if (a != 0){
     System.out.println(i + " = " + a + " , ");
     }
   }
   System.out.println("otherwise = 0");
   System.out.println("+++++++++++++++++++++++++++++++++++++++");
   
  //--------------------------------------------------
  System.out.println("Please input path of file with VAs to translate");
  InputStream VAs = new FileInputStream(scfile.nextLine());
  File f = new File("C:\\Users\\Nicholas\\Desktop\\A0112224B.txt");
  if (!f.exists()) {
        f.createNewFile();
      }
 
      fw = new FileWriter(f.getAbsoluteFile());
      translate(VAs);
      //print BitMap-------------------------------------------------
  System.out.println("Final BitMap Mapping: ");
   for(long a : BitMapFrames){
     System.out.println(String.format("%32s", Long.toBinaryString(a)).replace(' ', '0'));
   }
   System.out.println("");
  //-------------------------------------------------------------
      fw.close();
      VAs.close();
      
 }}