
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * WinnerImpl implements the interface Winner and also implements 2 static methods, 
 * loadData that takes the content of the file and puts it in a collection of WinnerImpl, 
 * and buildWinner that creates a Winner object from a given string.
 * 
 * @author Lorenzo Bellomo
 *
 */
public class WinnerImpl implements Winner {
	
	// private fields of a Winner object
	private int year;
	private int age;
	private String name;
	private String title;
	
	/**
	 * Constructor, takes as parameters the ones needed to build a Winner instance
	 * @param year
	 * @param age
	 * @param name
	 * @param title
	 */
	public WinnerImpl(int year, int age, String name, String title) {
		this.year = year;
		this.age = age;
		this.name = name;
		this.title = title;
	}
	

	/**
	 * Given an array of String paths, the function reads the content of given files and 
	 * extracts the needed content in order to fill the Winner collection
	 * 
	 * @param paths array of String paths of files
	 * @return the collection of winners extracted from the files
	 */
	public static Collection<Winner> loadData(String[] paths) {
		
		// the collection to return
		Collection<Winner> coll = new ArrayList<>();

		// for every file
		for(String path : paths) {
			// I create a stream to read line by line its content
			try (Stream<String> stream = Files.lines(Paths.get(path))) {

				// Now I actually parse the elements
				stream.forEach(x -> {
					// if it starts with "#" than it is the format line
					if (!x.startsWith("#")) 
						coll.add(buildWinner(x));
				});
				
			} catch (IOException e) {
				e.printStackTrace();
				// I proceed to next file
				continue;
			}
		}
		// scanned all possibile files, returning the collection
		return coll;
	}
	
	/**
	 * Given a string (intuitively a line of one file), it parses it according to
	 * the standard format to build a Winner object
	 * 
	 * @param s the string to parse
	 * @return the winner object built
	 */
	private static Winner buildWinner(String s) {
		
		// I split on the comma to find the first three int values
		String[] commaSplit = s.split(",");
		// and I split on the " character, because I could find commas in the film title
		// parsing on " makes it possible to avoid inconsistencies
		String[] quoteSplit = s.split("\"");
		
		// I prepare the variables to initialize the Winner object
		int year = 0, age = 0;
		String name = null, title = null;
		
		// the format is <Index,Year,Age...>, so year and age are in position
		// 1 and 2 of the split array
		year = Integer.parseInt(commaSplit[1]);
		age = Integer.parseInt(commaSplit[2]);
		// the format is <..."Name","Title">, so Name and title are in position 1 and 3 
		// of the split array
		name = quoteSplit[1];
		title = quoteSplit[3];
		
		// parsed, returning the Winner object now
		return new WinnerImpl(year, age, name, title);
	}

	// below follows the getter methods for the fields
	@Override
	public int getYear() {
		return year;
	}

	@Override
	public int getWinnerAge() {
		return age;
	}

	@Override
	public String getWinnerName() {
		return name;
	}

	@Override
	public String getFilmTitle() {
		return title;
	}

}
