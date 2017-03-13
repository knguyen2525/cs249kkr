import collections.defaultdict as defaultdict

def pathscore(s,e,mats,types):
    if len(types) < 2:
        return 0.0
    sMap = defaultdict(int)
    eMap = defaultdict(int)
    for i in range(len(types) - 1):
        index = (types[i], types[i+1])
        revIndex = (types[i+1], types[i])
        if i == 0:
            for col in mats[index][s]:
                sMap[col] = 1
            for col in mats[index][e]:
                eMap[col] = 1
        else:
            sMap = product(sMap,mats[revIndex])
            eMap = product(eMap,mats[revIndex])
            
    return 2.0*sMap[e]/(sMap[s] + eMap[e])    
    
def product(rMap,mat):
    outMap = defaultdict(int)
    for i in range(len(mat)):
        outMap[i] = 0
        for j in mat[i]:
            outMap[i] += rMap[j]
    return outMap