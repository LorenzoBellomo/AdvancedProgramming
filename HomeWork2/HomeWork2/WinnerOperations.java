import java.util.ArrayList; 
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * File WinnerOperations. It implements the 5 static methods to be implemented.
 * @author Lorenzo Bellomo
 *
 */
public class WinnerOperations {
	
	/**
	 * Given a stream of winners, returns the stream of names of winners that 
	 * are older than 35 sorted alphabetically
	 * 
	 * @param input the winner stream to filter
	 * @return the stream of String representing names
	 */
	public static Stream<String> oldWinners(Stream<Winner> input) {

		 // This function just keeps out the winners who won only at young age (<35)
		Stream<String> result = input
				.filter(x -> x.getWinnerAge() >= 35)
				.map(x -> x.getWinnerName())
				.sorted();
		
		return result;
		
	}
	
	/**
	 * given a Stream<Winner> returns a Stream<String> containing the names of 
	 * all the youngest and of all the oldest winners, sorted in inverse alphabetical ordering.
	 * 
	 * @param input the winner stream to process
	 * @return the string stream with the names
	 */
	public static Stream<String> extremeWinners(Stream<Winner> input) {
		/*
		 * This function collects the elements of the stream, in order to then retrieve the 
		 * minimum and the maximum according to the winner age. Then it filters all winners 
		 * who have ages a in the range min < a < max
		 */
		Collection<Winner> coll = input
			.collect(Collectors.toCollection(ArrayList::new));
		int max = Collections.max(coll, (e1, e2) -> e1.getWinnerAge() - e2.getWinnerAge()).getWinnerAge();
		int min = Collections.min(coll, (e1, e2) -> e1.getWinnerAge() - e2.getWinnerAge()).getWinnerAge();

		Stream <String> result = coll.stream()
				.filter(x -> x.getWinnerAge() == min || x.getWinnerAge() == max)
				.map(x -> x.getWinnerName())
				.sorted((e1, e2) -> -e1.compareTo(e2));
				
		return result;
		
	}
	
	/**
	 * given a Stream<Winner> returns a Stream<String> containing the titles of 
	 * films who won two prizes. The elements of the stream must be in chronological order
	 * 
	 * @param input the winner stream to process
	 * @return the string stream with the names
	 */
	public static Stream<String> multiAwardedFilm(Stream<Winner> input) {
		/*
		 * This function groups the Winners on the film title, building a map where
		 * the keys are the titles, and the values are lists containing 1 Winner object for every 
		 * film with that title. Then I retrieve the collection of values (of Lists), filtering 
		 * in order to keep only the ones with at least 2 elements, retrieving only one of those 
		 * (in order to not print the same film twice), sort them and getting the film title.
		 */
		Stream<String> result = input
				.collect(Collectors.groupingBy(Winner::getFilmTitle))
				.values().stream()
				.filter(x -> x.size() > 1)
				.map(x -> x.get(0))
				.sorted((e1, e2) -> e1.getYear() - e2.getYear())
				.map(x -> x.getFilmTitle());
		return result;
	}
	
	/**
	 * given a Stream<Function<Stream<T>,Stream<U>>> of jobs and a Collection<T> 
	 * coll returns a Stream<U> obtained by concatenating the results of the 
	 * execution of all the jobs on the data contained in coll
	 * 
	 * @param job the stream of functions to be applied, every function takes a Stream<T>
	 * and returns a stream<U>
	 * @param coll the collection on which to apply them
	 * @return the stream of results computed
	 */
	public static <T, U> Stream<U> runJobs(Stream<Function<Stream<T>, Stream<U>>> job, Collection<T> coll) {
		/*
		 * Starting from the input function stream, every function is applied to the streams
		 * in the collection provided as argument. The Stream of Stream<U> is then mapped 
		 * with flatMap to a unique Stream<U>
		 */
		
		Stream<U> result = job
				.flatMap(f -> f.apply(coll.stream()));
		
		return result;
	} 
	
	public static void main(String[] args) {
		
		// names of the files to parse
		String[] toLoad = {"oscar_age_female.csv","oscar_age_male.csv"};
		// There I will put the functions to pass to runJobs
		ArrayList<Function<Stream<Winner>, Stream<String>>> func = new ArrayList<>();
		// the collection of Winners computed using loadData
		Collection<Winner> coll = WinnerImpl.loadData(toLoad);
		// I create the Function objects and put them in the func array List
		func.add(stream -> oldWinners(stream));
		func.add(stream -> extremeWinners(stream));
		func.add(stream -> multiAwardedFilm(stream));
		
		// I process the stream and concatenate the result using runJobs
		Stream<String> result = runJobs(func.stream(), coll);
		// I print the result
		result.forEach(System.out::println);
	}
	
}
