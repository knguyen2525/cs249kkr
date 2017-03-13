def hetesim(s,e,mats,types):
    if len(types) == 1:
        if s == e:
            return 1.0
        else:
            return 0.0
    lmat = mats[(types[0],types[1])]
    rmat = mats[(types[len(types) - 1], types[len(types) - 2])]
    left = lmat[s]
    right = rmat[e]
    total = len(left)*len(right)
    hetesum = 0.0
    newTypes = types[1:-1]
    for snext in left:
        for enext in right:
            hetesum += hetesim(snext,enext,mats,newTypes)
    return hetesum/total