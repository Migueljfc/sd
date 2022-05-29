/**
 * 
 */
package clientSide.entities;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 @summary Enum class that represents the states of the 3 entities
 */
public enum States {
	
	//Chef States
    WAIT_FOR_AN_ORDER,
    PREPARING_A_COURSE,
    DISHING_THE_PORTIONS,
    DELIVERING_THE_PORTIONS,
    CLOSING_SERVICE,
    
    //Waiter States
    APPRAISING_SITUATION,
    PRESENTING_THE_MENU,
    TAKING_THE_ORDER,
    PLACING_THE_ORDER,
    WAITING_FOR_AN_PORTION,
    PROCESSING_THE_BILL,
    RECEIVING_PAYMENT,
    
    //Student States
    GOING_TO_THE_RESTAURANT,
    TAKING_A_SEAT_AT_THE_TABLE,
    SELECTING_THE_COURSES,
    ORGANIZING_THE_ORDER,
    CHATING_WITH_COMPANIONS,
    ENJOYING_THE_MEAL,
    PAYING_THE_BILL,
    GOING_HOME

}


