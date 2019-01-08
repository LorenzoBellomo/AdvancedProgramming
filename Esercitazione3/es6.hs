import Data.Char (toUpper)

(|>) f a = a f

upper_first_letter s = (s |> head |> toUpper) : (s |> tail)

upper_first_list [] = []
upper_first_list (x:xs) = (upper_first_letter x) : upper_first_list (xs)

titlecase s = s |> words |> upper_first_list |> unwords

titlecase_combinators = \s -> 
    (map upper_first_letter (s |> words)) |> unwords

