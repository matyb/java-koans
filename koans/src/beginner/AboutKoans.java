package beginner;

import com.sandwich.koan.Koan;

public class AboutKoans {

	@Koan
	public void findAboutKoansFile(){
		throw new RuntimeException("delete this line");
	}
	
	@Koan
	public void definitionOfCompleteKoan(){
		boolean koanIsIncomplete = true;
		if(koanIsIncomplete){
			throw new RuntimeException("if koanIsIncomplete was false - this koan would be complete");
		}
	}

}
