
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    private static String titlecase(String s) {
        String result = Arrays.stream(s.split(" "))
        	.filter(x -> !x.isEmpty())
        	.map(x -> x.substring(0, 1).toUpperCase() + x.substring(1, x.length()))
        	.collect(Collectors.joining(""));
        	
        return result;
        
    }

    public static void main(String[] args) {
    	String[] array = {"ase boi", "jwE", " ", "QWEQW woie AAAAAAA fewf       ffff"};
        Arrays.stream(array).forEach(x -> System.out.println(titlecase(x)));
    }
}

