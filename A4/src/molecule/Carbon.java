package molecule;

public class Carbon extends Thread {
	
	private static int carbonCounter =0;
	private int id;
	private Propane sharedPropane;
	
	public Carbon(Propane propane_obj) {
		Carbon.carbonCounter+=1;
		id=carbonCounter; 
		this.sharedPropane = propane_obj;
	}
	
	public void run() {
	    try {
        // mutex to avoid interruptions 	 
       sharedPropane.mutex.acquire();
       //carbonCounter++;
       sharedPropane.addCarbon();
         
       //checking if we have all atoms before proceeding or we are releasing the mutex
       if (sharedPropane.getHydrogen() >= 8){
       System.out.println("---Group ready for bonding---");
       sharedPropane.carbonQ.release(3); //release carbonsQ by 3
		 sharedPropane.removeCarbon(3); //reduce carbon atoms by 3
       
       sharedPropane.hydrogensQ.release(8); // release 8 hydrogensQ 
		 sharedPropane.removeHydrogen(8); // // reduce by 8 hydrogens
       
      }
      else {
      sharedPropane.mutex.release(); ////Releasing the mutex semaphore
      
      }
      sharedPropane.carbonQ.acquire(); //wait at barrier for all atoms 
		sharedPropane.bond("C"+ this.id); //bond identified atom
	   sharedPropane.barrier.b_wait(); // barrier pass and reset
		sharedPropane.mutex.release(); //Releasing the mutex semaphore
		}
      
	    catch (InterruptedException ex) { /* not handling this  */}
	   // System.out.println(" ");
	}

}
