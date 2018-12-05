
data Expr a = Const a | Sum (Expr a) (Expr a) | Mul (Expr a) (Expr a) | Div (Expr a) (Expr a)

safeEval (Const a) = Just a

safeEval (Sum a b) = do
    first <- safeEval a
    second <- safeEval b
    return (first + second)

safeEval (Mul a b) = do
    first <- safeEval a
    second <- safeEval b
    return (first * second)

safeEval (Div a b) = do
    first <- safeEval a
    second <- safeEval b
    if second == 0 then Nothing else return (first / second)

a = safeEval (Sum (Mul (Div (Const 0) (Const 0)) (Const 3)) (Const 4)) -- Nothing
b = safeEval (Sum (Mul (Div (Const 0) (Const 2)) (Const 3)) (Const 4)) -- 4
c = safeEval (Sum (Mul (Const 2) (Const 3)) (Const 4)) -- 10
