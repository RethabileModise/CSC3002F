package molecule;

import molecule.Carbon;
import molecule.Propane;
import molecule.Hydrogen;

public class RunSimulation {

	/**
	 This class is sent the number of 
	 */
	public static void main(String[] args) {
		int no_hydrogens = Integer.parseInt(args[0]);
		int no_carbons = Integer.parseInt(args[1]);
      
      int atomDiff= no_hydrogens - no_carbons;
      int diffMul = atomDiff%5;
      int carboMultiple = no_carbons% 3;
      int hydroMultiple= no_hydrogens% 8;
          
      if ((carboMultiple == 0 && hydroMultiple == 0) && (diffMul == 0))
		{
   		System.out.println("Starting simulation with "+no_hydrogens+" H and "+no_carbons + " C");
   		
   		Carbon[] carbons = new Carbon[no_carbons];
   		Hydrogen[] hydrogens = new Hydrogen[no_hydrogens];
   		Propane sharedPropane = new Propane();
   		
   		for (int i=0;i<no_carbons;i++) {
   			carbons[i]=new Carbon(sharedPropane); // call constructor
   			carbons[i].start(); // start thread
   		}
   		for (int i=0;i<no_hydrogens;i++) {
   			hydrogens[i]= new Hydrogen(sharedPropane); //call constructor
   			hydrogens[i].start(); //start thread
   		}
      
      }
      else {
      System.out.println("Enter correct multiples of atoms");
      
      }
		
		


	}

}
