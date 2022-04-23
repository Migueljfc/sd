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
      *     @param kitchen reference to the kitchen
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
	   int requestId;
	   
	   requestId = bar.look_arround();
	   
	   if(requestId == 0) {
		   bar.salute_client();
		   table.presenting_menu();
	   }
	   else if (requestId == 1) {
		   table.get_the_pad();
		   kitchen.hand_note_to_the_chef();
		   kitchen.return_to_bar();
	   }
	   else if (requestId == 2) {
		   while(!table.have_all_portions_delivered()) {
			   kitchen.collect_portion();
		       table.deliver_portion();
		   }
		   bar.return_to_bar();
	   }
	   else if(requestId == 3) {
		   bar.prepare_the_bill();
		   bar.present_the_bill();
		   table.return_to_bar();
	   }
	   else if (requestId == 4) {
		   bar.say_godbey();
	   }
		
	}
   
}