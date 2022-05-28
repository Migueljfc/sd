package commInfra;



/**
 *   Type of the exchanged messages.
 *the
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */

public enum MessageType
{
    /**
     *  Initialization of the logging file name and the number of iterations (service request).
     */

    SETNFIC,

    /**
     *  Logging file was initialized (reply).
     */

    NFICDONE,

    /**
     *  Alert the waiter (service request).
     */

    ALREQ,

    /**
     *  Waiter was alerted (reply).
     */

    ALDONE,

    /**
     *  Enter (request).
     */

    ENTREQ,

    /**
     *  Student has enter (reply).
     */

    ENTDONE,

    /**
     *  Call the waiter (request).
     */

    CWREQ,

    /**
     *  Called the waiter (reply).
     */

    CWDONE,

    /**
     *  Signal waiter (request).
     */

    SWREQ,

    /**
     *  Signal waiter done (reply).
     */

    SWDONE ,

    /**
     *  Exit request (REQUEST).
     */

    EXITREQ ,

    /**
     *  Student has Exit  (REPLY)
     */

    EXITDONE ,

    /**
     *  Look around (REQUEST)
     */

    LAREQ ,

    /**
     *  Look around (REPLY)
     */

    LADONE ,

    /**
     *  Say goodbye (REQUEST)
     */

    SGREQ ,

    /**
     *  Say goodbye (REPLY)
     */

    SGDONE ,

    /**
     *  Prepare bill (REQUEST)
     */

    PBREQ ,

    /**
     *  Prepare bill (REPLY)
     */

    PBDONE ,

    /**
     *  Watch the news (REQUEST)
     */

    WTNREQ ,

    /**
     *  Watch the news (REPLY)
     */

    WTNDONE ,

    /**
     *  Start preparation (REQUEST)
     */

    SPREQ ,

    /**
     *  Start preparation (REPLY)
     */

    SPDONE ,

    /**
     *  Proceed preparation (REQUEST)
     */

    PPREQ ,

    /**
     *  Proceed preparation (REPLY)
     */

    PPDONE ,

    /**
     *  Have next portion ready (REQUEST)
     */

    HNPRREQ ,

    /**
     *  Have next portion ready (REPLY)
     */

    HNPRDONE ,

    /**
     *  continue preparation (REQUEST)
     */

    CPREQ ,

    /**
     *  continue preparation (REPLY)
     */

    CPDONE ,

    /**
     *  Have all portions been delivered (REQUEST)
     */

    HAPBDREQ ,

    /**
     *  Have all portions been delivered (REPLY)
     */

    HAPBDDONE ,

    /**
     *  Has the order been completed (REQUEST)
     */

    HTOBCREQ ,

    /**
     *  Has the order been completed (REPLY)
     */

    HTOBCDONE ,

    /**
     *  Clean up (REQUEST)
     */

    CUREQ ,

    /**
     *  Clean up (REPLY)
     */

    CUDONE ,

    /**
     *  Return from table to bar (REQUEST)
     */

    RTBREQ ,

    /**
     *  Returned from table to bar (REPLY)
     */

    RTBDONE ,
    /**
     *  Hand note to the chef (REQUEST)
     */

    HNTCREQ ,

    /**
     *  Hand note to the chef (REPLY)
     */

    HNTCDONE ,

    /**
     *  Collect portion (REQUEST)
     */

    CPOREQ ,

    /**
     *  Collect portion (REPLY)
     */

    CPODONE ,

    /**
     *  Return from kitchen to bar (REQUEST)
     */

    RBREQ ,

    /**
     *  Return from kitchen to bar (REPLY)
     */

    RBDONE ,

    /**
     *  Salute client (REQUEST)
     */

    SCREQ ,

    /**
     *  Salute client (REPLY)
     */

    SCDONE ,


    /**
     *  Get the pad (REQUEST)
     */

    GTPREQ ,

    /**
     *  Get the pad (REPLY)
     */

    GTPDONE ,

    /**
     *  Have all portions delivered (REQUEST)
     */

    HAPDREQ ,

    /**
     *  Have all portions delivered (REPLY)
     */

    HAPDDONE ,

    /**
     *  Deliver portion (REQUEST)
     */

    DPREQ ,

    /**
     *  Deliver portion (REPLY)
     */

    DPDONE ,

    /**
     *  Present the bill (REQUEST)
     */

    PREBREQ ,

    /**
     *  Bill presented (REPLY)
     */

    PREBDONE ,

    /**
     *  Get first student (REQUEST)
     */

    GFSREQ ,

    /**
     *  Get first student (REPLY)
     */

    GFSDONE ,

    /**
     *  Get last student (REQUEST)
     */

    GLSREQ ,

    /**
     *  Get last student (REPLY)
     */

    GLSDONE ,


    /**
     *  Seat at Table (REQUEST)
     */

    SATREQ ,

    /**
     *  Seat at table (REPLY)
     */

    SATDONE ,

    /**
     *  readMenu (REQUEST)
     */

    RMREQ ,

    /**
     *  readMenu (REPLY)
     */

    RMDONE ,

    /**
     *  prepare the order (REQUEST)
     */

    POREQ ,

    /**
     *  prepare the order (REPLY)
     */

    PODONE ,

    /**
     *  has everybody chosen (REQUEST)
     */

    HECREQ ,

    /**
     *  has everybody chosen  (REPLY)
     */

    HECDONE ,

    /**
     *  Add up ones choices (REQUEST)
     */

    AUOCREQ ,

    /**
     *  Add up ones choices (REPLY)
     */

    AUOCDONE ,

    /**
     *  Describe the order (REQUEST)
     */

    DOREQ ,

    /**
     *  Describe the order (REPLY)
     */

    DODONE ,

    /**
     *  Joint the task (REQUEST)
     */

    JTREQ ,

    /**
     *  Join the Talk (REPLY)
     */

    JTDONE ,

    /**
     *  Inform companions (REQUEST)
     */

    ICREQ ,

    /**
     *  Inform companions (REPLY)
     */

    ICDONE ,

    /**
     *  Start eating (REQUEST)
     */

    SEREQ ,

    /**
     *  Start eating (REPLY)
     */

    SEDONE ,

    /**
     *  End eating (REQUEST)
     */

    EEREQ ,

    /**
     *  End eating (REPLY)
     */

    EEDONE ,

    /**
     *  Has everybody finished (REQUEST)
     */

    HEFREQ ,

    /**
     *  has everybody finished (REPLY)
     */

    HEFDONE ,

    /**
     *  honour the bill (REQUEST)
     */

    HBREQ ,

    /**
     *  honour the bill (REPLY)
     */

    HBDONE ,

    /**
     *  Have all courses Delivery (REQUEST)
     */

    HACDREQ ,

    /**
     *  Have all courses Delivery (REPLY)
     */

    HACDDONE ,

    /**
     *  Should have arrived earlier (REQUEST)
     */

    SHAEREQ ,

    /**
     *  Should have arrived earlier (REPLY)
     */

    SHAEDONE ,

    /**
     *  Get current student  (REQUEST)
     */

    GCSREQ ,

    /**
     *  Get current student (REPLY)
     */

    GCSDONE ,

    /**
     *  Server shutdown (service request).
     */

    SHUT ,

    /**
     *  Server was shutdown (reply).
     */

    SHUTDONE ,

    /**
     *  Set student state (REQUEST).
     */

    STSST ,

    /**
     *  Set waiter State (REQUEST).
     */

    STWST ,

    /**
     *  Set Chef State (REQUEST).
     */

    STCST ,

    /**
     * Set first student (REQUEST)
     */
    STFS,

    /**
     * Set first student (REPLY)
     */
    FSDONE,
    /**
     * Set last student (REQUEST)
     */
    STLS,
    /**
     * Set last student (REPLY)
     */
    LSDONE,
    /**
     * Set student seat (REQUEST)
     */
    STSS,

    /**
     * Set student seat (REPLY)
     */
    SSDONE,

    /**
     * Get student seat (REQUEST)
     */
    GSSREQ,

    /**
     * Get student seat (REPLY)
     */
    GSSDONE,
    /**
     * Set portions (REQUEST)
     */
    STPOR,

    /**
     * Set portions (REPLY)
     */
    PORDONE,


    /**
     * Set courses (REQUEST)
     */

    STCOR,

    /**
     * Set courses (REPLY)
     */

    CORDONE,
    /**
     *  Setting acknowledged (REPLY).
     */

    SACK ,
}
