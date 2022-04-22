/**
 * 
 */
package sharedRegions;
import java.util.Arrays;
import entities.*;
import main.SimulPar;



/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */


public class Kitchen {
	private boolean portionReady[];
	private boolean portionDelivery[];
	private boolean courseDelivery[];
	
	private GeneralRepository repository;
	
	public Kitchen(GeneralRepository repository) {
		this.repository = repository;
		Arrays.fill(portionReady, false);
		Arrays.fill(portionDelivery, false);
		Arrays.fill(courseDelivery, false);
		


	}
	
	public synchronized void whatch_news() {
		Chef c = (Chef) Thread.currentThread();
		c.setChefState(States.WAIT_FOR_AN_ORDER);
		repository.setChefState(c.getChefState());
		
		while(repository.handTheNoteToTheChef()) {
			try {
				wait();
			} catch( InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public synchronized void start_preparation() {
		Chef c = (Chef) Thread.currentThread();
		c.setChefState(States.PREPARING_A_COURSE);
		repository.setChefState(States.PREPARING_A_COURSE);
        notifyAll();
	}
	
	public synchronized void continue_preparation() {
		Chef c = (Chef) Thread.currentThread();
		c.setChefState(States.DISHING_THE_PORTIONS);
		repository.setChefState(States.DISHING_THE_PORTIONS);
        numberOfCookedPortions++;
	}
	
	public synchronized void proceed_preparation() {
		Chef c = (Chef) Thread.currentThread();
		c.setChefState(States.PREPARING_A_COURSE);
		repository.setChefState(States.PREPARING_A_COURSE);
	}
	
	public synchronized boolean have_all_portions_been_delivered() {
		while( numberOfPortionsReady != 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				
			}
		}
        System.out.println("Number of courses delivered: "+numberOfServedPortions);
        if(numberOfPortionsServed == SimulPar.N){
            numberOfCoursesServed++;
            return true;
        }

        return false;
	}
	
	public synchronized boolean have_next_portion_ready() {
		System.out.println("Number of courses delivered: "+numberOfServedCourses);
		//Check if all courses have been delivered
		if (numberOfCoursesServed == SimulPar.M)
			return true;
		return false;
	}
	
	public synchronized void has_the_order_been_completed() {
		//Update chefs state
				((Chef) Thread.currentThread()).setChefState(States.DISHING_THE_PORTIONS);
				repository.setChefState(((Chef) Thread.currentThread()).getChefState());
				
				//Update numberOfPortionsCooked
				numberOfCookedPortions++;
				
				//Update chefs state
				((Chef) Thread.currentThread()).setChefState(States.DELIVERING_THE_PORTIONS);
				repository.setChefState(((Chef) Thread.currentThread()).getChefState());
				
				//Notify Waiter that there is a portion waiting to be delivered
				notifyAll();
	}
	
	public synchronized void clean_up() {
		((Chef) Thread.currentThread()).setChefState(States.CLOSING_SERVICE);
		repository.setChefState(States.CLOSING_SERVICE);
	}
	
	public synchronized void collect_portion()
	{
		((Waiter) Thread.currentThread()).setWaiterState(States.WAITING_FOR_AN_PORTION);
		repository.setWaiterState(((Waiter) Thread.currentThread()).getWaiterState());
		
		//If there are no portions to deliver waiter must block
		while ( numberOfPortionsReady == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Update number of portions ready and delivered
		numberOfPortionsReady--;
		numberOfPortionsDelivered++;
		if(numberOfPortionsDelivered > ExecuteConst.N)
			numberOfPortionsDelivered = 1;
		
		repository.setnPortions(numberOfPortionsDelivered);
		repository.setnCourses(numberOfCoursesDelivered+1);
		
		//Signal chef that portion was delivered
		notifyAll();
		
	}
}
