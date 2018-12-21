module Ex2 (mapLB) where

import Ex1

-- Implementing Foldable, so implementing foldr
instance Foldable ListBag where
    foldr f acc (LB []) = acc
    foldr f acc (LB ((e, i):xs)) = f e (foldr f acc (LB xs))

-- Trying to define a NON well-formed aware version of fmap
mapLB :: (t -> a) -> ListBag t -> ListBag a
mapLB f (LB []) = empty
mapLB f (LB ((e, i):xs)) = cons (mapLB f (LB xs)) (f e, i)

{-
(2.3)
mapLB cannot be used as an implementation of fmap because mapLB could create non-well formed ListBags. 
For example by mapping function (x -> 1) to any ListBag with at least two elements will build a 
ListBag containing two times element 1, which is against the definition of well formed ListBag.
Then I might try to make the result of mapLB well formed, by providing a function (like the one below
named makeWellFormed), which takes a non well formed bag and returns a well formed one. Doing so, 
and implementing fmap like makeWellFormed (mapLB...), the new type constraint Eq prevents me from
implementing a "well formed aware" version of fmap with the original type signature.
In fact, if I try and un-comment the instance Functor below, it will not accept the proposed version
of fmap for this particular reason
-}

(|>) f a = a f

-- This methods makes a non well formed List bag into a Wellformed one, by exploiting insert cardinality, 
-- which is provided in Ex1 and checks for the cardinalities of elements
makeWellFormed :: Eq a => ListBag a -> ListBag a
makeWellFormed (LB []) = LB []
makeWellFormed (LB (x:xs)) = insertCardinality (LB xs) x

-- If the implementation below gets un-commented, the following error is raised:
--           No instance for (Eq b) arising from a use of `makeWellFormed'
--instance Functor ListBag where
    --fmap f (LB xs) = mapLB f (LB []) |> makeWellFormed