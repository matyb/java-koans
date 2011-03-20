package beginner;

import com.sandwich.koan.Koan;

public class AboutKoans {

	@Koan
	public void findAboutKoansFile(){
		throw new RuntimeException("delete this line");
	}
	
	@Koan
	public void definitionOfKoanCompletion(){
		boolean koanIsComplete = false;
		if(!koanIsComplete){
			throw new RuntimeException("what if koanIsComplete was true?");
		}
	}
	
}
