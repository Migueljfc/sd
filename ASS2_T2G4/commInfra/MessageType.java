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
     *  Server shutdown (service request).
     */

   SHUT,

    /**
     *  Server was shutdown (reply).
     */

    SHUTDONE,

    /**
     *  Set barber state (service request).
     */

    STBST,

    /**
     *  Set customer state (service request).
     */

    STCST,

    /**
     *  Set barber and customer states (service request).
     */

    STBCST,

    /**
     *  Setting acknowledged (reply).
     */

    SACK,
}
