package clientSide.entities;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 * @summary Class that represents the states of the 3 entities
 */
public class States {

    //Chef States
    public static final int WAIT_FOR_AN_ORDER = 0;
    public static final int PREPARING_A_COURSE = 1;
    public static final int DISHING_THE_PORTIONS = 2;
    public static final int DELIVERING_THE_PORTIONS = 3;
    public static final int CLOSING_SERVICE = 4;

    //Waiter States
    public static final int APPRAISING_SITUATION = 5;
    public static final int PRESENTING_THE_MENU = 6;
    public static final int TAKING_THE_ORDER = 7;
    public static final int PLACING_THE_ORDER = 8;
    public static final int WAITING_FOR_AN_PORTION = 9;
    public static final int PROCESSING_THE_BILL = 10;
    public static final int RECEIVING_PAYMENT = 11;

    //Student States
    public static final int GOING_TO_THE_RESTAURANT = 12;
    public static final int TAKING_A_SEAT_AT_THE_TABLE = 13;
    public static final int SELECTING_THE_COURSES = 14;
    public static final int ORGANIZING_THE_ORDER = 15;
    public static final int CHATING_WITH_COMPANIONS = 16;
    public static final int ENJOYING_THE_MEAL = 17;
    public static final int PAYING_THE_BILL = 18;
    public static final int GOING_HOME = 19;

}


