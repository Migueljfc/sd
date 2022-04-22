/**
 * 
 */
package entities;
import sharedRegions.*;
/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */
public class Waiter extends Thread {
	   
	   
    /**
   *  waiter's State.
   *  @serialField state
   */
   private States state;
   
   /**
   *  Bar
   *  @serialField bar
   */
   private Bar bar;
   
   /**
   *  Table
   *  @serialField table
   */
   private Table table;
   
   /**
   *  Kitchen
   *  @serialField Kitchen
   */
   private Kitchen kitchen;
   
   /**
   *  Repository
   * @serialField Repository
   */
   private GeneralRepository repository;
   
       /**
      *   Instantiation of a Waiter thread.
      *
      *     @param name thread name
      *     @param table reference to the table
      *     @param bar reference to the bar
      *     @param kitchen regerence to the kitchen
      *     @param repo reference to the general repository
      */
   public Waiter(String name, Table table, Bar bar, Kitchen kitchen, GeneralRepository repository){
       super(name);
       state = States.APPRAISING_SITUATION;
       this.table = table;
       this.repository = repository;
       this.bar = bar;
       this.kitchen = kitchen;
       
   }
   
   
    /**
    * Returns the Waiter's state.
    * @return student's current state
    */
   public States getWaiterState(){
       return state;
   }
   
   
   /**
    * Sets the waiter's state.
    * @param s desired state
    */
   public void setWaiterState(States s){
       StackTraceElement[] ste = Thread.currentThread().getStackTrace();
       state = s;
       
   }
   
   @Override
   public void run ()
   {
	   int state;
	   
	   bar.look_around()
	   do{
	       if(first_student)
	          bar.salute_client()
	        bar.presenting_menu()
	     else first_student = False;
	     if(table.callTheWaiter())
	         bar.get_the_pad()
	       kitchen.hand_the_note_to_the_chef()
	       bar.return_to_bar()
	       while(!kitchen.have_all_portions_delivered()){
	       kitchen.collect portion()
	       table.deliver_portion()
	     }
	        bar.return_to_bar() 
	   } while table.have_all_clients_been_served()

	   table.signalTheWaiter()
	   bar.prepare_the_bill()
	   bar.present_the_bill()
	   table.honour_the_bill()
	   bar.return_to_bar()
	   bar.say_godbey() 
       
   }
   
}