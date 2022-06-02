package commInfra;

/**
 * Type of the exchanged messages.
 * the
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class MessageType {
    //____________________________________________
    // ________________BAR MESSAGES_______________

    /**
     * Alert the waiter (service request).
     */
    public static final int ALREQ = 1;

    /**
     * Waiter was alerted (reply).
     */

    public static final int ALDONE = 2;

    /**
     * Look around (REQUEST)
     */

    public static final int LAREQ = 3;

    /**
     * Look around (REPLY)
     */

    public static final int LADONE = 4;
    /**
     * Prepare bill (REQUEST)
     */

    public static final int PBREQ = 5;

    /**
     * Prepare bill (REPLY)
     */

    public static final int PBDONE = 6;

    /**
     * Say goodbye (REQUEST)
     */

    public static final int SGREQ = 7;

    /**
     * Say goodbye (REPLY)
     */

    public static final int SGDONE = 8;

    /**
     * Enter (request).
     */

    public static final int ENTREQ = 9;

    /**
     * Student has enter (reply).
     */

    public static final int ENTDONE = 10;


    /**
     * Call the waiter (request).
     */
    public static final int CWREQ = 11;

    /**
     * Called the waiter (reply).
     */

    public static final int CWDONE = 12;

    /**
     * Signal waiter (request).
     */
    public static final int SWREQ = 13;

    /**
     * Signal waiter done (reply).
     */

    public static final int SWDONE = 14;

    /**
     * Exit request (REQUEST).
     */

    public static final int EXITREQ = 15;

    /**
     * Student has Exit  (REPLY)
     */

    public static final int EXITDONE = 16;


    /**
     * Get current student  (REQUEST)
     */

    public static final int GCSREQ = 17;

    /**
     * Get current student (REPLY)
     */

    public static final int GCSDONE = 18;

    /**
     * Bar shutdown (REQUEST)
     */
    public static final int BSREQ = 19;
    /**
     * Bar shutdown (REPLY)
     */
    public static final int BSDONE = 20;


    //___________________________________________________________________________________________
    //______________________________________________KITCHEN MESSAGES_____________________________

    /**
     * Watch the news (REQUEST)
     */

    public static final int WTNREQ = 21;

    /**
     * Watch the news (REPLY)
     */

    public static final int WTNDONE = 22;

    /**
     * Start preparation (REQUEST)
     */

    public static final int SPREQ = 23;

    /**
     * Start preparation (REPLY)
     */

    public static final int SPDONE = 24;

    /**
     * Proceed preparation (REQUEST)
     */

    public static final int PPREQ = 25;

    /**
     * Proceed preparation (REPLY)
     */

    public static final int PPDONE = 26;

    /**
     * Have all portions been delivered (REQUEST)
     */

    public static final int HAPBDREQ = 27;

    /**
     * Have all portions been delivered (REPLY)
     */

    public static final int HAPBDDONE = 28;


    /**
     * Has the order been completed (REQUEST)
     */

    public static final int HTOBCREQ = 29;

    /**
     * Has the order been completed (REPLY)
     */

    public static final int HTOBCDONE = 30;

    /**
     * continue preparation (REQUEST)
     */

    public static final int CPREQ = 31;

    /**
     * continue preparation (REPLY)
     */

    public static final int CPDONE = 32;


    /**
     * Have next portion ready (REQUEST)
     */

    public static final int HNPRREQ = 33;

    /**
     * Have next portion ready (REPLY)
     */

    public static final int HNPRDONE = 34;


    /**
     * Clean up (REQUEST)
     */

    public static final int CUREQ = 35;

    /**
     * Clean up (REPLY)
     */

    public static final int CUDONE = 36;

    /**
     * Hand note to the chef (REQUEST)
     */

    public static final int HNTCREQ = 37;

    /**
     * Hand note to the chef (REPLY)
     */

    public static final int HNTCDONE = 38;


    /**
     * Return from kitchen to bar (REQUEST)
     */

    public static final int RBREQ = 39;

    /**
     * Return from kitchen to bar (REPLY)
     */

    public static final int RBDONE = 40;


    /**
     * Collect portion (REQUEST)
     */

    public static final int CPOREQ = 41;

    /**
     * Collect portion (REPLY)
     */

    public static final int CPODONE = 42;

    /**
     * Kitchen shutdown (REQUEST)
     */
    public static final int KSREQ = 43;
    /**
     * Kitchen shutdown (REPLY)
     */
    public static final int KSDONE = 44;

    //___________________________________________________________________________________
    //_______________________________TABLE MESSAGES______________________________________

    /**
     * Salute client (REQUEST)
     */

    public static final int SCREQ = 45;

    /**
     * Salute client (REPLY)
     */

    public static final int SCDONE = 46;

    /**
     * Return from table to bar (REQUEST)
     */

    public static final int RTBREQ = 47;

    /**
     * Returned from table to bar (REPLY)
     */

    public static final int RTBDONE = 48;


    /**
     * Get the pad (REQUEST)
     */

    public static final int GTPREQ = 49;

    /**
     * Get the pad (REPLY)
     */

    public static final int GTPDONE = 50;

    /**
     * Have all portions delivered (REQUEST)
     */

    public static final int HAPDREQ = 51;

    /**
     * Have all portions delivered (REPLY)
     */

    public static final int HAPDDONE = 52;

    /**
     * Deliver portion (REQUEST)
     */

    public static final int DPREQ = 53;

    /**
     * Deliver portion (REPLY)
     */

    public static final int DPDONE = 54;

    /**
     * Present the bill (REQUEST)
     */

    public static final int PREBREQ = 55;

    /**
     * Bill presented (REPLY)
     */

    public static final int PREBDONE = 56;


    /**
     * Seat at Table (REQUEST)
     */

    public static final int SATREQ = 57;

    /**
     * Seat at table (REPLY)
     */

    public static final int SATDONE = 58;

    /**
     * readMenu (REQUEST)
     */

    public static final int RMREQ = 59;

    /**
     * readMenu (REPLY)
     */

    public static final int RMDONE = 60;

    /**
     * prepare the order (REQUEST)
     */

    public static final int POREQ = 61;

    /**
     * prepare the order (REPLY)
     */

    public static final int PODONE = 62;

    /**
     * has everybody chosen (REQUEST)
     */

    public static final int HECREQ = 63;

    /**
     * has everybody chosen  (REPLY)
     */

    public static final int HECDONE = 64;

    /**
     * Add up ones choices (REQUEST)
     */

    public static final int AUOCREQ = 65;

    /**
     * Add up ones choices (REPLY)
     */

    public static final int AUOCDONE = 66;

    /**
     * Describe the order (REQUEST)
     */

    public static final int DOREQ = 67;

    /**
     * Describe the order (REPLY)
     */

    public static final int DODONE = 68;

    /**
     * Joint the task (REQUEST)
     */

    public static final int JTREQ = 69;

    /**
     * Join the Talk (REPLY)
     */

    public static final int JTDONE = 70;

    /**
     * Inform companions (REQUEST)
     */

    public static final int ICREQ = 71;

    /**
     * Inform companions (REPLY)
     */

    public static final int ICDONE = 72;

    /**
     * Start eating (REQUEST)
     */

    public static final int SEREQ = 73;

    /**
     * Start eating (REPLY)
     */

    public static final int SEDONE = 74;

    /**
     * End eating (REQUEST)
     */

    public static final int EEREQ = 75;

    /**
     * End eating (REPLY)
     */

    public static final int EEDONE = 76;

    /**
     * Has everybody finished (REQUEST)
     */

    public static final int HEFREQ = 77;

    /**
     * has everybody finished (REPLY)
     */

    public static final int HEFDONE = 78;

    /**
     * honour the bill (REQUEST)
     */

    public static final int HBREQ = 79;

    /**
     * honour the bill (REPLY)
     */

    public static final int HBDONE = 80;

    /**
     * Have all courses Delivery (REQUEST)
     */

    public static final int HACDREQ = 81;

    /**
     * Have all courses Delivery (REPLY)
     */

    public static final int HACDDONE = 82;

    /**
     * Should have arrived earlier (REQUEST)
     */

    public static final int SHAEREQ = 83;

    /**
     * Should have arrived earlier (REPLY)
     */

    public static final int SHAEDONE = 84;

    /**
     * Table shutdown (REQUEST)
     */
    public static final int TSREQ = 85;
    /**
     * Table shutdown (REPLY)
     */
    public static final int TSDONE = 86;

    //___________________________________________________________________________
    //_________________________GENERAL REPOSITORY MESSAGES_______________________

    /**
     * Set Chef State (REQUEST).
     */

    public static final int STCST = 87;

    /**
     * Set Chef State (REPLY).
     */

    public static final int CSTDONE = 88;

    /**
     * Set waiter State (REQUEST).
     */

    public static final int STWST = 89;

    /**
     * Set waiter State (REPLY).
     */

    public static final int WSDONE = 90;
    /**
     * Set student state 1 (REQUEST).
     */

    public static final int STSST1 = 91;

    /**
     * Set student state 1 (REPLY).
     */

    public static final int SST1DONE = 92;

    /**
     * Set student state 2 (REQUEST).
     */

    public static final int STSST2 = 93;

    /**
     * Set student state 2 (REPLY).
     */

    public static final int SST2DONE = 94;


    /**
     * Set portions (REQUEST)
     */
    public static final int STPOR = 95;

    /**
     * Set portions (REPLY)
     */
    public static final int PORDONE = 96;


    /**
     * Set courses (REQUEST)
     */
    public static final int STCOR = 97;

    /**
     * Set courses (REPLY)
     */

    public static final int CORDONE = 98;

    /**
     * Set student seat (REQUEST)
     */
    public static final int STSS = 99;

    /**
     * Set student seat (REPLY)
     */
    public static final int SSDONE = 100;

    /**
     * Set student seat when exit (REQUEST)
     */
    public static final int STSSWE = 101;

    /**
     * Set student seat when exit (REPLY)
     */
    public static final int SSWEDONE = 102;

    /**
     * Get first student (REQUEST)
     */

    public static final int GFSREQ = 103;

    /**
     * Get first student (REPLY)
     */

    public static final int GFSDONE = 104;

    /**
     * Get last student to eat (REQUEST)
     */

    public static final int GLSREQ = 105;

    /**
     * Get last student to eat (REPLY)
     */

    public static final int GLSDONE = 106;


    /**
     * Set first student (REQUEST)
     */
    public static final int STFS = 107;

    /**
     * Set first student (REPLY)
     */
    public static final int FSDONE = 108;
    /**
     * Set last student (REQUEST)
     */
    public static final int STLS = 109;
    /**
     * Set last student (REPLY)
     */
    public static final int LSDONE = 110;


    /**
     * Get student seat (REQUEST)
     */
    public static final int GSSREQ = 111;

    /**
     * Get student seat (REPLY)
     */
    public static final int GSSDONE = 112;


    /**
     * GeneralRepository shutdown (REQUEST)
     */
    public static final int GRSREQ = 113;
    /**
     * GeneralRepository shutdown (REPLY)
     */
    public static final int GRSDONE = 114;

}

