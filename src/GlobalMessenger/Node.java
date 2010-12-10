/*
   Copyright (c) 2010 Diego Pizzocaro
   Permission is hereby granted, free of charge, to any person
   obtaining a copy of this software and associated documentation
   files (the "Software"), to deal in the Software without
   restriction, including without limitation the rights to use,
   copy, modify, merge, publish, distribute, sublicense, and/or sell
   copies of the Software, and to permit persons to whom the
   Software is furnished to do so, subject to the following
   conditions:

   The above copyright notice and this permission notice shall be
   included in all copies or substantial portions of the Software.

   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
   OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
   HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
   WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
   FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
   OTHER DEALINGS IN THE SOFTWARE.
   

 * ------------------------------------------------------------
 * "THE BEERWARE LICENSE" (Revision 42):
 * Diego Pizzocaro wrote this code. As long as you retain this 
 * notice, you can do whatever you want with this stuff. If we
 * meet someday, and you think this stuff is worth it, you can
 * buy me a beer in return.
 * ------------------------------------------------------------
 */

package GlobalMessenger;


import java.io.*;
import java.math.*;
import java.util.*;
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
 */
public class Node  {

	public int msgSent = 0;
	public ArrayList<Message> msgReceived;
	public GlobalMessenger globalMessenger;
	public double communicationRange = 60;
	public double energy = 0;
	public ArrayList<Message> msgToSendList;


	@Parameter (displayName = "Msg Sent", usageName = "msgSent")
	public int getMsgSent() { return msgSent; }
	public void setMsgSent(int newValue) { msgSent = newValue; }


	@Parameter (displayName = "Msg Received list", usageName = "msgReceived")
	public ArrayList<Message> getMsgReceived() { return msgReceived; }
	public void setMsgReceived(ArrayList<Message> newValue) { msgReceived = newValue; }


	@Parameter (displayName = "Global Messenger", usageName = "globalMessenger")
	public GlobalMessenger getGlobalMessenger() { return globalMessenger; }
	public void setGlobalMessenger(GlobalMessenger newValue) { globalMessenger = newValue; }


	@Parameter (displayName = "Comm Range", usageName = "communicationRange")
	public double getCommunicationRange() { return communicationRange;  }
	public void setCommunicationRange(double newValue) { communicationRange = newValue; }


	@Parameter (displayName = "Energy", usageName = "energy")
	public double getEnergy() { return energy; }
	public void setEnergy(double newValue) { energy = newValue; }

	@Parameter (displayName = "Msg To Send List", usageName = "msgToSendList")
	public ArrayList<Message> getMsgToSendList() { return msgToSendList; }
	public void setMsgToSendList(ArrayList<Message> newValue) { msgToSendList = newValue; }


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
	protected long numericID = agentIDCounter;
	protected String agentID = "Node " + (agentIDCounter++);



	/**
	 *
	 * This is the step behavior.
	 * @method initialize
	 *
	 */
	public void initialize(GlobalMessenger aGlobalMessenger) {

		setMsgReceived(new ArrayList<Message>());
		setMsgSent(0);
		setGlobalMessenger(aGlobalMessenger);
		setEnergy(RandomDraw(1,100));
		setMsgToSendList(new ArrayList<Message>());

	}


	/**
	 *
	 * @method updateNeighbors
	 * This method updates the SensorNetwork neighbors
	 * and it runs at the start of each timestep (Priority = FIRST).
	 * This is useful in case the nodes are moving. 
	 *
	 */
	@ScheduledMethod(
			start = 1d,
			priority = 1.7976931348623157E308d, //priority = FIRST
			shuffle = false
	)
	public void updateNeighbors() {

		Network network = (Network)FindProjection("GlobalMessenger/SensorNetwork");
		Iterator netNeighbors = new NetworkAdjacent(network, this).query().iterator();

		while (netNeighbors.hasNext()) {
			Object oldNeighbor = netNeighbors.next();
			RemoveEdge("GlobalMessenger/SensorNetwork", this, oldNeighbor);
		}

		ContinuousSpace mySpace = (ContinuousSpace)FindProjection("GlobalMessenger/ContinuousSpace2D");
		Iterator list = new ContinuousWithin(mySpace, this, communicationRange).query().iterator();
		double myX = mySpace.getLocation(this).getX();
		double myY = mySpace.getLocation(this).getY();

		while (list.hasNext()) {

			Object o = list.next();

			if (o instanceof Node) {
				CreateEdge("GlobalMessenger/SensorNetwork", this, o, 1.0);
			} 
		}


	}



	/**
	 *
	 * This is the step behavior.
	 * @method step
	 * Note that each node HAS TO execute the operations in this order compulsory:
	 * 1) RECEIVE msgs
	 * 2) PROCESS msgs
	 * 3) SEND msgs	 
	 *  
	 * (Otherwise you screw up the simulation given the design of the GlobalMessenger)
	 *
	 */
	@ScheduledMethod(
			start = 1d,
			interval = 1d,
			priority = 0, // NORMAL priority 
			shuffle = false
	)	
	public void step() {

		// RECEIVE msgs 
		ArrayList<Message> msgReceivedTmp = globalMessenger.receive(this);

		// PROCESS msgs
		if (msgReceivedTmp.size() > 0) {
			msgReceived.addAll(msgReceivedTmp);
		}        
		setMsgToSendList(hello());

		for (int i = 0; i < msgToSendList.size(); i++) {
			globalMessenger.send(msgToSendList.get(i));

		}

		// SEND msgs
		setMsgToSendList(new ArrayList<Message>());
	}



	/**
	 *
	 * This is the step behavior.
	 * @method hello
	 *
	 * Process received messages and 
	 * returns an ArrayList of Message to send.
	 *
	 */
	public ArrayList<Message> hello() {

		ArrayList<Message> returnValue = new ArrayList<Message>();
		double timeDouble = GetTickCount();

		if (msgReceived.size() > 0) {

			for (int i = 0; i < msgReceived.size(); i++) {

				Message aMsgReceived = (Message)msgReceived.get(i);
				returnValue.add(new Message(this, aMsgReceived.getSender(), new String("Hello back from "+this+ " to " + aMsgReceived.getSender()), timeDouble));
				System.out.println(GetTickCount() + " - "+ this+" RECEIVED from "+ aMsgReceived.getSender() +": "+(String)aMsgReceived.getContent() + " (timestamp: "+aMsgReceived.getTimestamp()+")");

			}

			setMsgReceived(new ArrayList<Message>());

		} else  {

			Network network = (Network)FindProjection("GlobalMessenger/SensorNetwork");
			Iterator netNeighbors = new NetworkAdjacent(network, this).query().iterator();

			while (netNeighbors.hasNext()) {
				Node aNetNeighbor = (Node)netNeighbors.next();
				returnValue.add(new Message(this, aNetNeighbor, new String("Hello from "+this+" to "+ aNetNeighbor), timeDouble));
			}


		}

		return returnValue;

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
		// Return the results.
		return returnValue;

	}


}

