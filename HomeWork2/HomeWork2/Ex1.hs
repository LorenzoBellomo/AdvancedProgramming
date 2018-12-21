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
, cons
, insertCardinality
) where

-- Data definition
data ListBag a = LB [(a, Int)]
    deriving (Show, Eq)

------------------------- MY UTILITIES ---------------------------

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

-- inserts the element in the list bag, according to the provided cardinality (like insert, but cardinality aware)
insertCardinality :: Eq a => ListBag a -> (a, Int) -> ListBag a
insertCardinality (LB []) x = (LB [x])
insertCardinality (LB ((r, i) : xs)) (e, j) = if e == r then cons (LB xs)  (r, i+j) else cons ((insertCardinality (LB xs)) (e, j)) (r, i)

-- well formed List Bag check
-- The check is performed by checking if every element is present in the sublist next to him, and then recurring
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
-- Here insert is used, which checks if an element is already present in LB
fromList :: Eq a => [a] -> ListBag a
fromList [] = LB []
fromList (x:xs) = insert (fromList xs) x

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
-- if insertCardinality finds a duplicate, it sums the multiplicities
sumBag :: Eq a => ListBag a -> ListBag a -> ListBag a
sumBag (LB []) (LB []) = LB []
sumBag lb (LB []) = lb
sumBag lb (LB (y:ys)) = sumBag (insertCardinality lb y) (LB ys)