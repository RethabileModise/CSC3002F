package molecule;

public class Hydrogen extends Thread {

	private static int hydrogenCounter =0;
	private int id;
	private Propane sharedPropane;
	
	
	public Hydrogen(Propane propane_obj) {
		Hydrogen.hydrogenCounter+=1;
		id=hydrogenCounter;
		this.sharedPropane = propane_obj;
		
	}
	
	public void run() {
	    try {
       // mutex to avoid interruptions 
       sharedPropane.mutex.acquire();
       //hydrogenCounter++;
       sharedPropane.addHydrogen();
       //checking if we have all atoms before proceeding or we are releasing the mutex
       if (sharedPropane.getHydrogen() >= 8  &&  sharedPropane.getCarbon() >= 3)
          {
          System.out.println("---Group ready for bonding---");		
          sharedPropane.hydrogensQ.release(8); // release 8 hydrogensQ 
          sharedPropane.removeHydrogen(8); // reduce by 8 hydrogens
          //hydrogenCounter -= 8;
          sharedPropane.carbonQ.release(3); //release carbonsQ by 3
          sharedPropane.removeCarbon(3); //reduce carbon atoms by 3
          
	    	// you will need to fix below
	    	//System.out.println("---Group ready for bonding---");			 
	    	//sharedPropane.bond("H"+ this.id);
          }
          else if (sharedPropane.getHydrogen() < 8 || sharedPropane.getCarbon() < 3)
			{
            sharedPropane.mutex.release(); //Releasing the mutex semaphore
         }
         else  {
           sharedPropane.mutex.release(); //Releasing the mutex semaphore
           
          }
           
       sharedPropane.hydrogensQ.acquire(); //wait at barrier for all atoms 
      // System.out.println("---Group ready for bonding---");			 
	    sharedPropane.bond("H"+ this.id); //bond an atom with its id  
       sharedPropane.barrier.b_wait();// barrier pass and reset
       sharedPropane.mutex.release(); //Releasing the mutex semaphore
          
	    }
	   catch (InterruptedException ex) { /* not handling this  */}
	    //System.out.println(" ");
	}

}
