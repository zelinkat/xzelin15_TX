package cz.vutbr.fit.xzelin15.dp.consumer;

public class ClientConstants {

	/*
	 * Number of booked restaurant seats 
	 */
	public final static String RESTAURANT_ARG = "restaurant";
	
	/*
	 * Number of booked circle seats 
	 */
	public final static String THEATRE_STALLS_ARG = "theatrestallscount";
	
	/*
	 * Number of booked stall seats  
	 */
	public final static String THEATRE_CIRCLE_ARG = "theatrecirclecount";
	
	/*
	 * Number of booked theater balcony seats 
	 */
	public final static String THEATRE_BALCONY_ARG = "theatrebalconycount";
	
	/*
	 * A Need of taxi  
	 */
	public final static String TAXI_ARG = "taxi";
	
	
	/*
	 * Type of transaction 
	 */
	public final static String TX_TYPE_ARG = "txType";
	
	
	
	/*
	 * Transaction commands
	 */
	public final static String TX_COMMAND_ARG = "txCommand";
	
	
	/*
	 *  Command to  enlist the participant to transaction
	 */
	public final static String DELIST = "delist";
	
	/*
	 *  Command to not enlist the participant to transaction
	 */
	public final static String ENLIST = "enlist";
	
	
	/*
	 * Circle seats type 
	 */
	public final static int CIRCLESEATS_AREA = 0;
	
	/*
	 * Stall seats type 
	 */
	public final static int STALLEATS_AREA = 1;
	
	/*
	 * Balcony seats type 
	 */
	public final static int BALCONYSEATS_AREA = 2;
	
}
