import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

public class Main {
	
	private static ImmutablePair<Integer, Double> SomeCalculation(List<Double> list) {
		
		long num = list
				.stream()
				.mapToDouble(x -> x)
				.filter(x -> x >= 0.2 && x <= Math.PI)
				.count();
		
		OptionalDouble avg = list
				.stream()
				.mapToDouble(x -> x)
				.filter(x -> x >= 10 && x <= 100)
				.average();
		
		return new ImmutablePair<Integer, Double>((int) num, (avg.isPresent()) ? avg.getAsDouble() : new Double(0));
		
	}

	public static void main(String[] args) {
		
		List<Double> list = new ArrayList<>();		
		Random r = new Random();
		
		for (int i = 0; i < 200; i++) 
			list.add(r.nextDouble() * 101);
		
		ImmutablePair<Integer, Double> pair = SomeCalculation(list);
		System.out.println("Number: " + pair.getA() + ", Average: " + pair.getB());

	}

}
