module Ex5
( joinLB
, returnLB
, bindLB
) where

import Ex1
import Ex2
import Test.HUnit

{-
In order to define a monad, in the most recent versions of Haskell, I have to make ListBag both an instance 
Functor and of Applicative. I already recognized Functor as impossible to be defined in a well-formed aware 
way, so I already know I can stop, since I will not be able to make ListBag a monad. The one condition to
relax would be the same as for Functor, in that being the Eq constraint. Notice that, in the following 
exercise (Ex6), I provide another implementation of MultiSet which doesn't have this kind of issues, so where
the condition is relaxed. So for an actual instance of a MultiSet monad, just check the next implementation.
-}

-- I still define joinLB, returnLB and bindLB in order to see the Eq type costraint that I'm expecting
-- joinLB flattens a multi level container in a lower level one (like flatMap but not generic)
joinLB :: Eq a => ListBag (ListBag a) -> ListBag a
joinLB (LB []) = empty
joinLB (LB (((LB xs), 1):ys)) = sumBag (LB xs) (joinLB (LB ys))
joinLB (LB (((LB xs), i):ys)) = sumBag (LB xs) (joinLB (LB (((LB xs), i-1):ys)))

-- With return I simply put the element in an empty container
returnLB :: a -> ListBag a
returnLB x = singleton x

-- With bind I join the result of every possible computation given a function
bindLB :: Eq a => ListBag t -> (t -> ListBag a) -> ListBag a
bindLB a@(LB x) g = joinLB (mapLB g a)

--If I try and un-comment the following Monad instance I get 2 types of errors:
-- One is because I have the Eq constraint, the other is because I am missing Functor and Applicative

--instance Monad ListBag where
  --return x = returnLB x
  --y >>= g = bindLB y g

-- below some tests for the three functions are written
    
-- LB [(24,2),(23,1),(22,1),(21,1)]
aLB = fromList [21, 22, 23, 24, 24]

-- LB [(LB [('d',1),('f',1)],2),(LB [('e',1),('d',1)],1)]
bLB = fromList [fromList ['d', 'e'], fromList ['f','d'], fromList ['f', 'd']]

testJoin = TestCase $ assertEqual "test Join" (toList (joinLB bLB)) ("dddffe")
testReturn = TestCase $ assertEqual "test Return" (toList (returnLB 'z')) ("z")
testBind = TestCase $ assertEqual "test Bind" (toList (bindLB aLB (\x -> singleton (x+1)))) [25,25,24,23,22]

testlist = TestList [TestLabel "testJoin" testJoin,
                    TestLabel "testReturn" testReturn,
                    TestLabel "testBind" testBind
                    ]

main :: IO ()
main = do
  runTestTT testlist
  return ()