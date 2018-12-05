import java.util.List;
import java.util.ArrayList;

public class SumOdd {

	public static void main(String[] args) {
		
		List<Integer> list = new ArrayList<>();	 
		
		for (String s : args) 
			list.add(Integer.parseInt(s));
		
		System.out.print("Non stream method: ");
		int sumNS = 0;
		for(Integer i : list) {
			if(i%2 != 0)
				sumNS += i;
		}
		System.out.println(sumNS);
		
		System.out.print("Stream method: ");
		int sumS = list
			.stream()
			.mapToInt(x -> x)
			.filter(x -> x%2 != 0)
			.sum();
		
		System.out.println(sumS);
		
		assert sumS == sumNS;
			
	}

}
