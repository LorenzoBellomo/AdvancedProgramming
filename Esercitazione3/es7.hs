import Data.List (elem)

(|>) f a = a f

isPali str = str == reverse str

countVowels [] = 0
countVowels (x:xs) = if (elem x ['a', 'e', 'i', 'o', 'u']) then 1 + (countVowels xs) else countVowels xs

f = \(n, str) -> if isPali str then n + (countVowels str) else n

-- definition of recursive one
countVowelsPali [] = 0
countVowelsPali (x:xs) = if isPali x then (countVowels x) + (countVowelsPali xs) else (countVowelsPali xs)

-- definition of combinator using one
countVowelPaliCombinator strList = foldl (curry f) 0 strList
