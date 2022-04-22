/**
 * 
 */
package sharedRegions;
import java.util.Arrays;
import entities.*;
import main.SimulPar;
import genclass.GenericIO;


/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */


public class Kitchen {
	private int portionsReady;
	private int portionsDelivery;
	private int coursesDelivery;
	
	private GeneralRepository repository;
	
	public Kitchen(GeneralRepository repository) {
		this.repository = repository;
		this.portionsReady = 0;
		this.portionsDelivery = 0;
		this.coursesDelivery = 0;
		
	}
	
	public synchronized void watch_news() {
		Chef c = (Chef) Thread.currentThread();
		c.setChefState(States.WAIT_FOR_AN_ORDER);
		repository.setChefState(c.getChefState());
		
		//while(repository.handTheNoteToTheChef()) {
			try {
				wait();
			} catch( InterruptedException e) {
				e.printStackTrace();
			}
		//}		
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
        portionsReady++;
	}
	
	public synchronized void proceed_preparation() {
		Chef c = (Chef) Thread.currentThread();
		c.setChefState(States.PREPARING_A_COURSE);
		repository.setChefState(States.PREPARING_A_COURSE);
	}
	
	public synchronized boolean have_all_portions_been_delivered() {
		while( portionsReady != 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				
			}
		}
        System.out.println("Number of courses delivered: " + coursesDelivery);
        if(portionsDelivery == SimulPar.N){
            coursesDelivery++;
            return true;
        }

        return false;
	}
	
	public synchronized void have_next_portion_ready() {
		Chef c = (Chef) Thread.currentThread();
		c.setChefState(States.DISHING_THE_PORTIONS);
		repository.setChefState(c.getChefState());
		
		portionsReady++;
		
		c = (Chef) Thread.currentThread();
		c.setChefState(States.DELIVERING_THE_PORTIONS);
		repository.setChefState(c.getChefState());
		
		notify();
		
	}
	
	public synchronized boolean has_the_order_been_completed() {
		if(coursesDelivery == SimulPar.M) return true;
		return false;
	}
	
	
	public synchronized void clean_up() {
		Chef c = (Chef) Thread.currentThread();
		c.setChefState(States.CLOSING_SERVICE);
		repository.setChefState(States.CLOSING_SERVICE););
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
