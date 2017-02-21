# GraphSpace
A graph-based representation of a non-euclidean space

Main idea:

* Partition a plane into regular squares
* Each square is a node, sides of the squares are connections
* As such, each node has four connections
* To avoid creation of empty nodes on the borders, the space is looped onto itself (it's closed and infinite)
* Nodes can be rearranged in such a way that the plane is no longer an Euclidean plane

This project is supposed to provide only the implementation of the space itself; objects to be stored inside the space should be provided by whoever uses the space.

## Wiki
For more information, visit the Wiki: https://github.com/slemonide/GraphSpace/wiki
