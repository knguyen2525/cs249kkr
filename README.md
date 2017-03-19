Folders:
data - the data that we used to create our heterogenous information network.

matrices - the adjacency matrices for our heterogeneous information network.

pairGeneration - scripts for generating positive and negative node pairs. Also contains csv files that contain the output node pairs. Run GenPairs to generate new positive and negative node pairs based on the matrices in the matrices folder.

parsing - scripts for parsing the data set and generating the adjacency matrices.

To run:
1) numpy and scipy need to be installed
2) run bpcrwfspg.py to calculate similarity scores and the least squared errors using bpcrw as the metric in the model.
3) run hetesimfspg.py to calculate similarity scores and the least squared errors using hetesim as the metric in the model.
