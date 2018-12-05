module Ex1
( 
ListBag (LB)
, wf
, empty
, singleton
, fromList
, isEmpty
, mul
, toList
, sumBag
) where

-- Data definition
data ListBag a = LB [(a, Int)]
    deriving (Show, Eq)

------------------------- UTILITIES ---------------------------

-- Checks if the element is in the bag, meaning it checks with the first component
isElem :: Eq a => a -> ListBag a -> Bool
isElem e (LB []) = False
isElem e (LB ((r, i) : xs)) = if r == e then True else isElem e (LB xs)

-- concatenates the list in the list bag with the following element
cons :: ListBag a -> (a, Int) -> ListBag a
cons (LB xs) e = LB (e:xs)

-- inserts the element in the list bag, if the element is found it just updates the cardinality
insert :: Eq a => ListBag a -> a -> ListBag a
insert (LB []) e = singleton e
insert (LB ((r, i) : xs)) e = if e == r then cons (LB xs) (r, i+1) else cons ((insert (LB xs)) e) (r, i)

-- inserts the element in the list bag, according to the provided cardinality
insertCardinality :: Eq a => ListBag a -> (a, Int) -> ListBag a
insertCardinality (LB []) x = (LB [x])
insertCardinality (LB ((r, i) : xs)) (e, j) = if e == r then cons (LB xs)  (r, i+j) else cons ((insertCardinality (LB xs)) (e, j)) (r, i)

-- well formed List Bag check
wf :: Eq a => ListBag a -> Bool
wf (LB []) = True
wf (LB ((e, i) : xs)) = (not (isElem e (LB xs))) && (wf (LB xs))

------------------------- CONSTRUCTORS ---------------------------

-- empty list bag constructor
empty :: ListBag a
empty = LB []

-- singleton list bag constructor
singleton :: a -> ListBag a
singleton v = LB[(v, 1)]

-- creates a List Bag from a list
fromList :: (Foldable t1, Eq t2) => t1 t2 -> ListBag t2
fromList xs = foldl insert (LB []) xs

------------------------- OPERATIONS ---------------------------

-- checks if the list bag is empty
isEmpty :: ListBag a -> Bool
isEmpty (LB []) = True
isEmpty (LB xs) = False

-- checks the cardinality of element v in the given list bag
mul :: Eq a => a -> ListBag a -> Int
mul v (LB []) = 0
mul v (LB ((e, i) : xs)) = if e == v then i else mul v (LB xs)

-- creates a list with all the elements in the multiset, repeating i times every time an element
-- with i cardinality
toList :: ListBag a -> [a]
toList (LB []) = []
toList (LB ((e, i) : xs)) = (replicate i e) ++ (toList (LB xs))

-- creates a ListBag adding the elements of the second one in the first one
sumBag :: Eq a => ListBag a -> ListBag a -> ListBag a
sumBag (LB []) (LB []) = LB []
sumBag lb (LB []) = lb
sumBag lb (LB (y:ys)) = sumBag (insertCardinality lb y) (LB ys)

-----------------------------------------------------------------------------------------

b = LB [('a', 1), ('b', 2)]
c = LB [('a', 2), ('b', 3)]
d = LB [('a', 1), ('c', 4)]
a = fromList [1, 1, 2, 3, 4]