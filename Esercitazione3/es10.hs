
trmap f l = 
    let concat acc [] = acc
        concat acc (x:xs) = concat (acc ++ [f x]) xs
     in concat [] l 

trfilter p l = 
    let filt acc [] = acc
        filt acc (x:xs) = if p x then filt (acc ++ [x]) xs else filt acc xs
    in filt [] l