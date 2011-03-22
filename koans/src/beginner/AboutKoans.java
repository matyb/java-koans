package beginner;

import static com.sandwich.util.Assert.fail;

import com.sandwich.koan.Koan;

public class AboutKoans {

	@Koan
	public void findAboutKoansFile(){
		fail("delete this line");
	}
	
	@Koan
	public void definitionOfKoanCompletion(){
		boolean koanIsComplete = false;
		if(!koanIsComplete){
			fail("what if koanIsComplete was true?");
		}
	}
	
}
