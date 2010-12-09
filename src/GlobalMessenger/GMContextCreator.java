package GlobalMessenger;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.valueLayer.GridValueLayer;

import static repast.simphony.essentials.RepastEssentials.*;


/**
 * Global Messenger model.
 * 
 * @author Diego Pizzocaro
 * 
 */
public class GMContextCreator implements ContextBuilder {


	public Context build(Context context) {
		int xdim = 20;   // The x dimension of the physical space
		int ydim = 20;   // The y dimension of the physical space

		
		// Create a new 2D continuous space to model the physical space 
		ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
		.createContinuousSpace("ContinuousSpace2D", context, new RandomCartesianAdder(),
				new repast.simphony.space.continuous.StickyBorders() , xdim, ydim);

		
		NetworkFactoryFinder.createNetworkFactory(null).createNetwork("SensorNetwork", context, false);
						
		// The environment parameters contain the user-editable values that appear in the GUI.
		//  Get the parameters p and then specifically the initial numbers of wolves and sheep.
		Parameters p = RunEnvironment.getInstance().getParameters();
		int numNodes = (Integer)p.getValue("numNodes");
				

		// Populate the root context with the initial agents		
        GlobalMessenger theGlobalMessenger = new GlobalMessenger();
        context.add(theGlobalMessenger);
        theGlobalMessenger.initialize();
        System.out.println("init Global Messenger");
        //TODO move GlobalMessenger to corner (these commands cause errors because the context is not yet created!)
        //ContinuousSpace mySpace = (ContinuousSpace)FindProjection("SensorNetworkSimPureJava/ContinuousSpace2D"); 
        //mySpace.moveTo(theGlobalMessenger, 1.0, 1.0);

        for (int i = 1; i <= numNodes; i++) {

            Node aNode = new Node();
            context.add(aNode);
            aNode.initialize(theGlobalMessenger);
            System.out.println("Creating nodes");

        }

               
		return context;                       
	}
}