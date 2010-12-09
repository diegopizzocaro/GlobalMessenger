# Global Messenger

This is a [REPAST Symphony 2.0 model](http://repast.sourceforge.net/repast_simphony.html)
simulating nodes exchanging messages in a communication network.
Note that the GlobalMessenger main agent was designed for 1-to-1 communication, it delivers a message to each recipient in the timestep following the one during which the message was sent.
Note also that each Node agent during each timestep should execute 3 actions in this particular order:
1) RECEIVE messages (querying the GlobalMessenger)
2) PROCESS messages received
3) SEND messages (through the GlobalMessenger)

by [Diego Pizzocaro](http://users.cs.cf.ac.uk/D.Pizzocaro),Twitter handle:[@diegostream](http://twitter.com/diegostream)

Thanks/Acknowledgements to:
- [Matthew J. Williams](http://users.cs.cf.ac.uk/M.J.Williams),Twitter handle:[@voxmjw](http://twitter.com/voxmjw) for discussions on how to improve the data structure used by the GlobalMessenger class.
