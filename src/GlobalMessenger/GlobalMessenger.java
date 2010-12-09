package GlobalMessenger;

import java.io.*;
import java.math.*;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.measure.unit.*;
import org.jscience.mathematics.number.*;
import org.jscience.mathematics.vector.*;
import org.jscience.physics.amount.*;
import repast.simphony.adaptation.neural.*;
import repast.simphony.adaptation.regression.*;
import repast.simphony.context.*;
import repast.simphony.context.space.continuous.*;
import repast.simphony.context.space.gis.*;
import repast.simphony.context.space.graph.*;
import repast.simphony.context.space.grid.*;
import repast.simphony.engine.environment.*;
import repast.simphony.engine.schedule.*;
import repast.simphony.engine.watcher.*;
import repast.simphony.groovy.math.*;
import repast.simphony.integration.*;
import repast.simphony.matlab.link.*;
import repast.simphony.query.*;
import repast.simphony.query.space.continuous.*;
import repast.simphony.query.space.gis.*;
import repast.simphony.query.space.graph.*;
import repast.simphony.query.space.grid.*;
import repast.simphony.query.space.projection.*;
import repast.simphony.parameter.*;
import repast.simphony.random.*;
import repast.simphony.space.continuous.*;
import repast.simphony.space.gis.*;
import repast.simphony.space.graph.*;
import repast.simphony.space.grid.*;
import repast.simphony.space.projection.*;
import repast.simphony.ui.probe.*;
import repast.simphony.util.*;
import simphony.util.messages.*;
import static java.lang.Math.*;
import static repast.simphony.essentials.RepastEssentials.*;

/**
 *
 * This is an agent.
 * 
 * @author Diego Pizzocaro
 * 
 * NB: you need to specify the correct numNodes in GlobalMessenger.rs/context.html
 * otherwise the message buffer will throw an outOfBound exception.
 * If you dont know the total number of Nodes in your simulation 
 * then use an upper bound (hopefully "strict" upperbound).
 * (Changing the numNodes parameter in the GUI interface will not work!) 
 *
 */
public class GlobalMessenger  {

	public ArrayList<Message> queue[]; // Each element contains an ArrayList of Messages for receiver i
	public int msgSent = 0;
	public int msgSentBefore = 0;
	public int msgExchanged = 0;

	@Parameter (displayName = "Msg Queue", usageName = "queue")
	public synchronized ArrayList<Message>[] getQueue() { return queue; }
	public synchronized void setQueue(ArrayList<Message>[] newValue) { queue = newValue;  }

	@Parameter (displayName = "Msg Sent Counter", usageName = "msgSent")
	public synchronized int getMsgSent() { return msgSent; }
	public synchronized void setMsgSent(int newValue) { msgSent = newValue; }


	@Parameter (displayName = "MsgSentBefore", usageName = "msgSentBefore")
	public synchronized int getMsgSentBefore() { return msgSentBefore; }
	public synchronized void setMsgSentBefore(int newValue) { msgSentBefore = newValue; }


	@Parameter (displayName = "msgExchanged", usageName = "msgExchanged")
	public synchronized int getMsgExchanged() { return msgExchanged; }
	public synchronized void setMsgExchanged(int newValue) { msgExchanged = newValue; }




	/**
	 *
	 * This value is used to automatically generate agent identifiers.
	 * @field serialVersionUID
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * This value is used to automatically generate agent identifiers.
	 * @field agentIDCounter
	 *
	 */
	protected static long agentIDCounter = 1;

	/**
	 *
	 * This value is the agent's identifier.
	 * @field agentID
	 *
	 */
	protected String agentID = "GlobalMessenger " + (agentIDCounter++);

	/**
	 *
	 * This is the step behavior.
	 * @method initialize
	 *
	 */
	public void initialize() {
		
		Parameters p = RunEnvironment.getInstance().getParameters();
		int numNodes = (Integer)p.getValue("numNodes");

		queue = new ArrayList[numNodes+1];
		// Note that receiver i = 0 is not used
		// (Ths Node class creates agentIDs starting by 1)
		for(int i = 0; i < queue.length; i++) {
			queue[i] = new ArrayList<Message>();			
		}

		//It's always useful to know which day/time it is
		//(especially if you're a timetraveller!)
		Date todaysDate = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy_HH:mm:ss");
		String formattedDate = formatter.format(todaysDate);
		System.out.println("Today’s date and Time is: "+formattedDate);
		System.out.println("(Useful in case you're a timetraveller)");

	}

	/**
	 *
	 * This is the step behavior.
	 * @method send
	 *
	 */
	public synchronized void send(Message aMsg) {
	
		int numericReceiverID = (int)aMsg.receiver.numericID; //DANGER unsafe conversion long to int
		queue[numericReceiverID].add(aMsg);
		msgSent++;

	}

	/**
	 *
	 * This is the step behavior.
	 * @method receive
	 * 
	 * This method delivers the message to the recipient at the next time step.
	 * If you want to simulate multihop message passing and traffic congestion,
	 * this might be the right place to do it.
	 * E.g.: implement a mechanism to wait to deliver the message after a number of timesteps
	 * equals to the minimum number of hops among two node. You also need to 
	 * increment the counter of messages exchanged inside the GlobalMessenger object.
	 * 
	 *  Note that another more correct way of implementing multihop is to leave the 
	 *  GlobalMessenger as is, since it was designed for exchanging messages one-to-one
	 *  and then embed on the node a routing mechanism.
	 *
	 */
	public synchronized ArrayList<Message> receive(Node receiverNode) {
		
		
		ArrayList<Message> msgsForReceiver = new ArrayList<Message>();
		Iterator queueIterator = queue[(int)receiverNode.numericID].iterator();//DANGER unsafe conversion long to int
		
		while (queueIterator.hasNext()) {

			Message aMessage = (Message)queueIterator.next();

			if (aMessage.timestamp < GetTickCount()) {
				msgsForReceiver.add(aMessage);
				queueIterator.remove();
			} 
		}		
		
		return msgsForReceiver;
	}
	

	/**
	 *
	 * This is the step behavior.
	 * @method step
	 * here you can check your variables for statistics.
	 *  
	 */
	@ScheduledMethod(
			start = 1d,
			interval = 1d,
			priority = -1.7976931348623157E308d, //priority LAST
			shuffle = false
	)
	public synchronized void step() {

		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		double timeDouble = GetTickCount();
		msgExchanged = msgSent - msgSentBefore;
		System.out.println(timeDouble+" "+this+" msgExchanged = "+msgExchanged);
		msgSentBefore = msgSent;


	}






	/**
	 *
	 * This method provides a human-readable name for the agent.
	 * @method toString
	 *
	 */
	@ProbeID()
	public String toString() {

		// Set the default agent identifier.
		String returnValue = this.agentID;		
		return returnValue;

	}


}


