# The-Great-Tournament-TCP-

There is a permanent tournament in the local network. Each of the players (agents) must play with every other agent a round of a “sailor game”. For a given pair of agents A and B the game consists in selection of the two numbers nA and nB. The winner is selected by
counting from 1 to nA + nB.
A new player can enter the tournament at any moment. It can also leave the tournament at any moment but only if it already played with all the other players, which were in the tournament then.

The tournament takes place in the local network between independent processes, which can be run potentially on different machines. The only allowed communication mechanism are messages sent through TCP connections.
