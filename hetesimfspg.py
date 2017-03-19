from sklearn import linear_model
import math
import numpy as np
import csv

mats = {}
leaves = []
used = []

from datetime import datetime
startTime = datetime.now()

def printMatrix(a):
    for x in np.nditer(a.T):
        print x

def cos(m,r):
    return np.dot(np.array(m),np.array(r))/(np.linalg.norm(m)*np.linalg.norm(r))

class Node:
    def __init__(self, path):
        self.path = path
        self.priority = -1
        self.pairs = []
        self.scores = []
        self.children = []

    def compute_score(self,s,e,st):
        return hetesim(s,e,[st] + self.path)

    def insert(self,s,e,st,et):
        score = self.compute_score(s,e,st)
        self.pairs.append((s,e,st,et))
        self.scores.append(score)

    def calculate_priority(self,r,starts):
        numeratorSum =  0.0
        squareSum = 0.0
        for i in range(len(self.pairs)):
            s = self.pairs[i][0]
            if s in starts:
                numeratorSum += self.scores[i]*starts[s]
            squareSum += self.scores[i]**2
        self.priority = numeratorSum*(0.5**(len(self.path)))/(math.sqrt(squareSum)*np.linalg.norm(np.array(r)))

def loadMats():
    global mats
    mats[(0,1)] = loadMat("matrices/papersToAuthors.csv")
    mats[(1,0)] = loadMat("matrices/authorsToPapers.csv")
    mats[(0,2)] = loadMat("matrices/papersToVenues.csv")
    mats[(2,0)] = loadMat("matrices/venuesToPapers.csv")
    mats[(0,3)] = loadMat("matrices/papersToCites.csv")
    mats[(3,0)] = loadMat("matrices/citesToPapers.csv")

def loadMat(fname):
    return [list(map(int,rec)) for rec in csv.reader(open(fname,"rU"), delimiter=',')]

def GreedyTree(pos_pairs, neg_pairs, r):
    global leaves
    starts = {}
    pairs = pos_pairs + neg_pairs
    for i in range(len(pos_pairs)):
        s = pos_pairs[i][0]
        if s not in starts:
            starts[s] = r[i]
        else:
            starts[s] = max(r[i],starts[s])
    if not leaves:
        root = Node([])
        for (s,e,st,et) in pos_pairs:
            root.insert(s,s,st,st)
        for (s,e,st,et) in neg_pairs:
            root.insert(s,s,st,st)
        root.calculate_priority(r,starts)
        leaves.append(root)
    for leaf in leaves:
        leaf.calculate_priority(r,starts)
    while True:
        maxPriority = float('-inf')
        mnode = None
        for leaf in leaves:
            if leaf.priority > maxPriority:
                maxPriority = leaf.priority
                mnode = leaf
        m = [0.0]*len(r)
        for i in range(len(mnode.pairs)):
            for j in range(len(pairs)):
                if mnode.pairs[i] == pairs[j]:
                    m[j] = mnode.scores[i]
        altered = False
        for el in m:
            if el != 0.0:
                altered = True
                break
        moveOn = True
        if altered:
            moveOn = False
            greatest = True
            mnode.priority = cos(m,r)
            for leaf in leaves:
                if mnode.priority < leaf.priority:
                    greatest = False
                    break
            if greatest:
                return mnode.path, m
                #if mnode.path not in used:
                #    used.append(mnode.path)
                #    return mnode.path, m
                moveOn = True
        if moveOn:
            nNodes = {}
            for (s,e,st,et) in mnode.pairs:
                if et == 0:
                    for num in (1,2,3):
                        if num not in nNodes:
                            nNodes[num] = Node(mnode.path+[num])
                        for el in mats[(et,num)][e]:
                            if el != -1:
                                nNodes[num].insert(s,el,st,num)
                else:
                    if 0 not in nNodes:
                        nNodes[0] = Node(mnode.path+[0])
                    for el in mats[(et,0)][e]:
                        if el != -1:
                            nNodes[0].insert(s,el,st,0)
            leaves.remove(mnode)
            for k in nNodes:
                nNodes[k].calculate_priority(r,starts)
                leaves.append(nNodes[k])

def bpcrw(s,e,types):
    alpha = 0.3
    if len(types) == 1:
        if s == e:
            return 1
        else:
            return 0
    if (types[0],types[1]) not in mats:
        return 0.0
    mat = mats[(types[0],types[1])]
    nextNodes = [n for n in mat[s] if n >= 0]
    if len(nextNodes) == 0:
        return  0
    bpcrwsum = 0.0
    for n in nextNodes:
        bpcrwsum += bpcrw(n,e,types[1:])
    return bpcrwsum/(len(nextNodes)**alpha)

def hetesim(s,e,types):
    if len(types) == 1:
        if s == e and s != -1:
            return 1.0
        else:
            return 0.0
    if (types[0],types[1]) not in mats or (types[len(types) - 1], types[len(types) - 2]) not in mats:
        return 0.0
    lmat = mats[(types[0],types[1])]
    rmat = mats[(types[len(types) - 1], types[len(types) - 2])]
    left = [l for l in lmat[s] if l >= 0]
    right = [r for r in rmat[e] if r >= 0]
    if len(left) == 0 or len(right) == 0:
        return 0.0
    total = len(left)*len(right)
    hetesum = 0.0
    newTypes = types[1:-1]
    if len(types) == 2:
        for snext in left:
            if snext == e:
                hetesum += 1.0
        for enext in right:
            if enext == s:
                hetesum += 1.0
        hetesum = hetesum/2.0
        return hetesum/total
    for snext in left:
        for enext in right:
            hetesum += hetesim(snext,enext,newTypes)
    return hetesum/total

loadMats()
pos_pairs = [tuple(map(int,rec[0:-1])) for rec in csv.reader(open("pairGeneration/positivePairs.csv","rU"), delimiter=',')]
neg_pairs = [tuple(map(int,rec[0:-1])) for rec in csv.reader(open("pairGeneration/negativePairs.csv","rU"), delimiter=',')]
r = [1.0]*len(pos_pairs)
r = np.array(r + [-1.0]*len(pos_pairs))
nr = r


clf = linear_model.Lars()
M = None
paths = []
for i in range(5):
    print i;
    path, scores = GreedyTree(pos_pairs,neg_pairs,nr)
    paths.append(path)
    #print path, scores
    if M == None:
        M = np.asmatrix(scores).T
    else:
        M = np.c_[M,scores]
    clf.fit(M, r)
    nr = nr - clf.predict(M)
total_err = 0.0
retVal = clf.predict(M)
for i in range(len(r)):
    total_err += (r[i] - retVal[i])**2
print total_err

test_pos = [tuple(map(int,rec[0:-1])) for rec in csv.reader(open("pairGeneration/positivePairs.csv","rU"), delimiter=',')]

test_pairs = test_pos
nM = None
print paths
for path in paths:
    scores = []
    for (s,e,st,et) in test_pairs:
        if et != path[-1]:
            scores.append(0.0)
        else:
            scores.append(hetesim(s,e,[st]+path))
    if nM == None:
        nM = np.asmatrix(scores).T
    else:
        nM = np.c_[nM,scores]

print clf.predict(nM)
