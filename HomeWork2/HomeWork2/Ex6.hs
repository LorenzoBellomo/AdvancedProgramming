module Ex6
(
OnlyListBag (OLB)
, empty
, singleton
, fromList
, isEmpty
, mul
, toList
, sumBag
) where

import qualified Ex1
import qualified Ex2

(|>) :: t1 -> (t1 -> t2) -> t2
f |> a = a f

-- What follows is the abstract data type definition for MultiSet
{-
Some comments about this definitions. fromList and sumBag have an Eq condition, which is driven by
the well formed aware implementation that was defined in Ex1. The following implementation (OnlyListBag,
presented just below in this file) does not have this condition. Nevertheless the condition was put in the
abstract data type definition in order to be able to make ListBag MultiSet compatible. Otherwise, the 
alternative is to get rid of those two Eq bounds (in sumBag and fromList, while mul bound cannot be relaxed),
and then either make the ListBag implementation of fromList and sumBag non wellformed-aware, or forcing 
ListBag elements to be all Eq instances (in the data type definition) like this:
        data (Eq a) => ListBag a = LB [(a, Int)]
            deriving (Show, Eq)
The prefered version for this exercise was to keep the implementation of the Ex1 as requested from the 
assignament, and over-bind the function definitions.
-}
-- (1)
class MultiSet ms where
    empty :: ms a
    singleton :: a -> ms a
    fromList :: Eq a => [a] -> ms a
    isEmpty :: ms a -> Bool
    mul :: Eq a => a -> ms a -> Int
    toList :: ms a -> [a]
    sumBag :: Eq a => ms a -> ms a -> ms a

-- (2)
-- What follows is the process needed to make ListBag (from Ex1) an instance of MultiSet
instance MultiSet Ex1.ListBag where
    empty = Ex1.empty
    singleton = Ex1.singleton
    fromList = Ex1.fromList
    isEmpty = Ex1.isEmpty
    mul = Ex1.mul
    toList = Ex1.toList
    sumBag = Ex1.sumBag

-- (3)
{-
What now follows is the new constructor class, named OnlyListBag (in the sense that the implementation
underlying is only a List of elements, with respect to the list of tuples as proposed in Ex1). 
-}
data OnlyListBag a = OLB [a]
    deriving (Show, Eq)

-- method implementation for the same ones as ListBag

-- creates an empty OnlyListBag
emptyOLB :: OnlyListBag a
emptyOLB = OLB []

-- creates an OnlyListBag with only 1 item
singletonOLB :: a -> OnlyListBag a
singletonOLB x = OLB [x]

-- creates an OnlyListBag starting from a List
fromListOLB :: [a] -> OnlyListBag a
fromListOLB xs = OLB xs

--checks if the OnlyListBag is empty 
isEmptyOLB :: OnlyListBag a -> Bool
isEmptyOLB (OLB []) = True
isEmptyOLB (OLB xs) = False

-- checks the multiplicity of an element in a listBag by summing 1 for each element equal to x
mulOLB :: (Eq a1, Num a2) => a1 -> OnlyListBag a1 -> a2
mulOLB x (OLB xs) = foldl (\acc e -> if e == x then acc + 1  else acc) 0 xs

-- returns a list with all the element present in the OnlyListBag
toListOLB :: OnlyListBag a -> [a]
toListOLB (OLB xs) = xs

-- merges two OnlyListBag in one
sumBagOLB :: OnlyListBag a -> OnlyListBag a -> OnlyListBag a
sumBagOLB (OLB xs) (OLB ys) = fromListOLB (xs ++ ys)

-- What follows is the process needed to make OnlyListBag an instance of MultiSet
instance MultiSet OnlyListBag where

    empty = emptyOLB
    singleton = singletonOLB
    fromList = fromListOLB
    isEmpty = isEmptyOLB
    mul = mulOLB
    toList = toListOLB
    sumBag = sumBagOLB

{-
No wf (wellFormed) predicate is provided, since there is no problem with this implementation
in the concept of well-formity. Having this relaxed constraint, I can also actually provide an implementation
of the Functor type class, and also the monad implementation, without having to keep the Eq constraint in order
to check well-formity
-}
    
-- implementing foldr
instance Foldable OnlyListBag where
    -- I simply pass to the fold of lists.
    foldr f acc (OLB xs) = foldr f acc xs

-- implementing fmap, first I create mapOLB (the dual for mapLB)
mapOLB :: (a1 -> a2) -> OnlyListBag a1 -> OnlyListBag a2
mapOLB f (OLB xs) = (fmap f xs) |> fromListOLB

instance Functor OnlyListBag where
    fmap f a@(OLB xs) = mapOLB f a

-- Now I implement join, return and bind in order to then create the monad implementation
-- join takes a "higher order" container, and flattens it into the underlying level
joinOLB :: OnlyListBag (OnlyListBag a) -> OnlyListBag a
joinOLB (OLB []) = emptyOLB
joinOLB (OLB ((OLB x):xs)) = sumBagOLB (OLB x) (joinOLB (OLB xs))

returnOLB :: a -> OnlyListBag a
returnOLB x = singletonOLB x

bindOLB :: OnlyListBag a1 -> (a1 -> OnlyListBag a) -> OnlyListBag a
bindOLB a@(OLB xs) f = joinOLB (mapOLB f a) 

-- I also need (from the most recent versions of Haskell), need to define Applicative, in order to 
-- create the monad
-- I define applOLB, which will be the <*> operator in the Applicative. It works in a way which 
-- is similar to the fmap, but with a "container of functions"
applOLB :: OnlyListBag (a1 -> a2) -> OnlyListBag a1 -> OnlyListBag a2
applOLB (OLB []) ys = emptyOLB
applOLB (OLB (f:xs)) ys = sumBagOLB (fmap f ys) (applOLB (OLB xs) ys)

instance Applicative OnlyListBag where
    pure  = returnOLB
    (<*>) = applOLB

-- then actually implementing the monad ones
instance Monad OnlyListBag where
    return = returnOLB
    (>>=) = bindOLB

-- (4)

-- There follows a simple function, that given a MultiSet, returns the list of the concatenation 
-- of the multiset and itself. Ideally, it replicates every element on the multiset (doubles its 
-- cardinality), and then puts all the elements in a list
simpleFunction :: (MultiSet ms, Eq a) => ms a -> [a]
simpleFunction ms = toList (sumBag ms ms)

-- If I apply the same function to a ListBag and an OnlyListBag  with the same multiset as input, 
-- I expect the result to have the same elements, counting the multeplicity, but potentially in
-- a different order (depending on the order of execution and the internal policies of implementation)

-- "aavvoorrpp" is the evaluation of retLB, so it correctly doubled every letter in the string "prova"
retLB = simpleFunction (Ex1.fromList "prova")
-- "provaprova" is the evaluation of retOLB, so it correctly doubled every letter in the string "prova"
retOLB = simpleFunction (fromListOLB "prova")