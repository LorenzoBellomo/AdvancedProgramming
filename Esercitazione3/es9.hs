data IntTree = Leaf Int | Node (Int, IntTree, IntTree)

tmap f (Leaf x) = Leaf (f x)
tmap f (Node (x, ts, td)) = Node (f x, tmap f ts, tmap f td)

--testTree = Node(1, Node(2, Leaf 3, Leaf 4), Node(5, Node(6, Leaf(7), Leaf 8), Leaf 9))
succTree t = tmap (\x -> x +1) t
sumSucc t = 
    let sumAccum (Leaf i) = i
        sumAccum (Node (i, st, dt)) = i + sumAccum(st) + sumAccum(dt) 
    in sumAccum (succTree t)

