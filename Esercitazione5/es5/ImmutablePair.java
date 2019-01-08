
public class ImmutablePair<T1, T2> {
	
	T1 t1;
	T2 t2;
	
	public ImmutablePair(T1 a, T2 b) {
		this.t1 = a;
		this.t2 = b;	
	}

	public T1 getA() {
		return t1;
	}
	
	public T2 getB() {
		return t2;
	}
}
