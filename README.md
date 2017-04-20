# Congestion-Control
A novel algorithm to reduce the rate at which packets are dropped at every node in a network by incorporating Fairness in a network and hence combatted congestion ( using Java ).

Most routing algorithms try to be optimal. This means that from a particular source to destination, the path taken by a packet will be the same. But In a large network, many links will remain unused. This seems highly plausible at first glance for transmitting a single packet. But lets say we want to transmit, say n( > 1000) packets. For the sake of the argument, lets create a hypothetical network. 

![Figure 1](/images/network.png)

Here, Lets take A as the source and G as the destination. In its populated routing table, A knows that the shortest path to G is through node D, making ADG the optimal path (if we assume number of hops as the measure for distance). Also, lets say that each intermediate node in the network has can queue say, m ( < n) packets.

Several cases arise when considering optimality with respect to congestion:

### I] Rate of Outflux of intermediate node is the same as Source

In such a case, as A sends the first packet to node D, D will forward the same to its neighbor G, which happens to be the destination as well. In the mean time, A would have sent the second packet to D, whcih D can now attend to. The process repeats. So, the as D is transmitting 'l'th (l < n) packet to G, A is transmittin the (l+1) packet to D. If such a smooth flow of packets persists throughout the transmission, we have no problems whatsoever. However, such networks are too ideal!

![Figure 2](/images/network_optimal.png)

### II] Rate of Outflux of intermediate node is less than the Source Node

In this case, A can transmit information at a faster rate than D. But to adhire to the principle of optimality, A MUST transmit all its packets to D. A knows for a fact that 'It is illogical for me to send the packets along any other path because I know for a fact that the best way to reach G is through D'. It would only be a matter of time that the buffers in D are filled. Due to congestion in the network, subsequent packets are lost.


### III] Rate of Outflux of intermediate node is more than the Source Node

There may occur a time where problems in A make it slower than node D. D can handle the load and transmit all packets successfully. But D isnt being used to its limits.

All the cases above suffer from the same drawback : They fail to leverage the resources allotted to the network. Most of the routing algorithms today try to determine the optimal path between 2 nodes, and hence glean that using the same path is an obvious solution. But, what about the other nodes B,C,E,F and H, just doing nothing? Why not use them to our advantage?

It is understood that modern algorithms achieve optimality at the cost of Fairness. But from the cases above (case II in particular),we noticed another major problem of compromising this tradeoff : Congestion.

## Fight against congestion: Fairness in the Network

If we were to incorperate the notion of fairness our network, node A should distribute the n packets equally using ALL the output lines; So, we can transmit the first packet 1 along link AB, packet 2 along link AC, packet 3 along link AD and repeat the process. So, a 4th packet would once again be transmitted on Line AB. So, here is how the fairness routing algorithm should work:

![Figure 3](/images/network_t1.png)

At T0, A decides to send the n packets to node G. A sends packets 1, 2, 3 to AB, AC and AD respectively. At time T1, Nodes B,C and D have successfully recieved the packets 1, 2 and 3 respectively coming from A.

At time T1, Node B will send the first Packet in its Queue (packet 1) to D. C will similarly send its packet to D and D will send its packet(3) to E. In the mean time, A would also send the next set of packets 4, 5 and 6 to B, C and D respectively. So at the time T2, the state of the network is as shown:

![Figure 4](/images/network_t1_t2.png)


At time `TJ`(for some `Jth` iteration), 
- Node A deploys the next set of packets to all of its nodes
- Each node will send queued packets to nodes along least recently used link.
- Each node will, in the mean time recieve packets from other intermediate nodes, which are queued.

![Figure 5](/images/network_t2_t3.png)

But you may wonder, if node G (destination) is adjacent to node D, then why send the packets to other neighbors E and F, instead of directly to the destination? Its obviously the shorter path. The reason lies in the fact that, D is always adjacent to G. So, packets at D would ALWAYS be routed along the link DG. This would means that the other links DE and DF would NEVER be used. If so, we get the same result as a optimality-based algorithm, as discussed previously. Hence, our resources wouldnt be leveraged and congestion would be eminent!

Above, we didnt specify the size of the input buffer for each node. This minimum should be the maximum number of packets that can enter into it in a fixed interval of time `t = [T(i) - T(i-1)]`


## Netowrk Breaks down
Say a Link breaks in the middle of transmission. The effect of breaking down could depend on the topology and exactly which node broke down. Two types of effects node breakdowns can cause - 

### 1. Individual Breakdown 
- Here, only the node that breaks down is avoided.
- Say node E has broken down in middle of transmission. E has 2 input links CE and DE and 2 output links,EF and EG.
- When immediate nodes C and D get to know that E has broken down, They will NOT route their packets to E. 

- Thus, C will route all of its packets to D and D will route its packets between F and G.

- Thus, even though congestion has increased in the network, it is a minimal increase. In a very large network, nodes will have MANY more options to choose from as output links, so congestion is combated.

- When node E is repaired, it should send a small packet to its links to tell its neighbors :"hello! I'm back".

- Nodes C, D , F and G recieve this packet and willmake a note in their routing tables.

- Now, Nodes C and D route again through node E as before.

![Figure 6](/images/individual_breakdown.png)

### 2. Chained Breakdown

- Nodes immediately adjacent to it cannot be routed to despite the fact that they are working!

- In the network shown, lets assume that node H breaks down. 

- F realises this and hence it cannot route packets through H.

- But that is the only output line of F. And it has 3 input lines too. So, incoming packets are stuck at F. Effectively, F is "not working" even though it is perfectly fine!

- F can notify its input nodes B,D and E that "I cannot do anything, so dont send me packets!".

- From here, its just like the case above in which nodes will avaid routing to F and H.

- Now, B can only send packets to D. D can send to both E and G, and E can only send to G.

- Thus, the increase in congestion in this case is more than was in the former when node E had broken down. 

- H will send info to its neighbors informing them about its recovery

![Figure 7](/images/chained_breakdown.png)



