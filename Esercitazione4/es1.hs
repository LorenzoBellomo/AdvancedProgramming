
data Expr a = Const a | Sum (Expr a) (Expr a) | Mul (Expr a) (Expr a)

eval (Const a) = a
eval (Sum a b) = eval a + eval b
eval (Mul a b) = eval a * eval b

--eval (Sum (Mul (Const 2) (Const 3)) (Const 4)) 