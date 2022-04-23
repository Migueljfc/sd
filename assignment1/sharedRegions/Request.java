/**
 * 
 */
package sharedRegions;

import main.SimulPar;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */

/**
 * {@summary} data type that represent a request made to the waiter
 */

public class Request {
	
	/**
	 *
	 */
	public int requestorId;
	
	public int requestId;
	/**
	 * Instantiation of Request
	 *
	 * @param requestorId  id of the person that made the request
	 * @param requestId id of the request [0 - request a menu 1- request a order 2- request a portion 3- request a bill 4- request a good bye
	 */
	public Request(int requestorId, int requestId) {
		assert(requestId>0 && requestId <=4);
		assert(requestorId <= SimulPar.N);
		this.requestorId = requestorId;
		this.requestId = requestId;
	}
}
