'''

                            Online Python Compiler.
                Code, Compile, Run and Debug python program online.
Write your code in this editor and press "Run" button to execute it.

'''
import sys
sys.setrecursionlimit(100000)

from collections import deque
from copy import deepcopy

# p1 = deque([43,19])
# p2 = deque([2,29,14])
# p1 = deque([9, 2, 6, 3, 1])
# p2 = deque([5, 8, 4, 7, 10])
p1 = deque([27,29,30,44,50,5,33,47,34,38,36,4,2,18,24,16,32,21,17,9,3,22,41,31,23])
p2 = deque([25,1,15,46,6,13,20,12,10,14,19,37,40,26,43,11,48,45,49,28,35,7,42,39,8])

res_map = {}
def combat(p1, p2, vis_map, g):
    if (len(p1) == 0):
        #print("Game end", g)
        return (2, p2)
    if (len(p2) == 0):
        #print("Game end", g)
        return (1, p1)
    
    h1 = hash(str(list(p1)))
    h2 = hash(str(list(p2)))
    hkey = str(h1) + "-" + str(h2)
    hkeyrev = str(h2) + "-" + str(h1)
    # if hkey in res_map:
    #     print("Cache result", p1, p2)
    #     return res_map[hkey]
    # if hkeyrev in res_map:
    #     print("Rev-cache", p1, p2)
    #     (x,y) = res_map[hkeyrev]
    #     if x == 1:
    #         return (2, y)
    #     return (1, y)
        
    if hkey in vis_map:
        #print("Found", p1, p2)
        res_map[hkey] = (1, p1)
        return (1, p1)
    # if hkeyrev in vis_map:
    #     res_map[hkeyrev] = (1, p1)
    #     return (2, p1)
    vis_map[hkey] = 1
    # vis_map[hkeyrev] = 2

    if (p1[0] < len(p1) and p2[0] < len(p2)):
        # recursive
        #print("rec", p1, p2)
        v1 = p1[0]
        v2 = p2[0]
        p1.popleft()
        p2.popleft()
        (p, res) = combat(deepcopy(deque(list(p1)[:v1])), deepcopy(deque(list(p2)[:v2])), {}, g+1)
        if (p == 1):
            p1.append(v1)
            p1.append(v2)
        else:
            p2.append(v2)
            p2.append(v1)
    else:
        # non-recursive
        v1 = p1[0]
        v2 = p2[0]
        p1.popleft()
        p2.popleft()
        if (v1 > v2):
            p1.append(v1)
            p1.append(v2)
        else:
            p2.append(v2)
            p2.append(v1)
    (x, y) = combat(p1,p2, vis_map, g)
        # h1 = hash(str(list(p1)))
        # h2 = hash(str(list(p2)))
        # hkey = str(h1) + "-" + str(h2)
        # hkeyrev = str(h2) + "-" + str(h1)
        # res_map[hkey] = (x, y)
    # if x == 1:
    #     res_map[hkeyrev] = (2,y)
    # else:
    #     res_map[hkeyrev] = (1,y)
    return (x,y)
    

(p, res) = combat(p1, p2, {}, 0)
print("Got out")
print(p, res)

val = 0
for i in range(0, len(res)):
    val += (i+1) * res[len(res)-i-1]

print(val)
