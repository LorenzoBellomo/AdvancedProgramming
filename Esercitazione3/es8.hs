fun f xs e = xs ++ [f e] 

myMap f a = foldl (fun f) [] a