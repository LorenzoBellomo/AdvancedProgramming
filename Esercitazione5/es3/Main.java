import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
	
	private static Object[] repl(Object[] xs, int n) {

		Object[] arr = Arrays.stream(xs)
			.flatMap(x -> Stream.iterate(x, y -> y).limit(n))
			.toArray();
		
		return arr;
	}

	public static void main(String[] args) {

		List<Integer> l1 = new ArrayList<>();
		l1.add(1);
		List<Integer> l2 = new ArrayList<>();
		l2.add(2);

		Object[] array = {l1, l2};
		
		Object[] arr = repl(array, 5);
		Arrays.stream(arr)
			.forEach(x -> System.out.println(x));
		
	}

}
