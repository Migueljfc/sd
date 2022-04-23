/**
 * 
 */
package sharedRegions;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */
public class Request {
	
	public int studentId;
	
	public int requestId;
	
	public Request(int studentId, int requestId) {
		this.studentId = studentId;
		this.requestId = requestId;
	}
}
