package java7;

import com.sandwich.koan.Koan;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutStringsInSwitch {

    @Koan
    public void stringsInSwitchStatement () {
        String[] animals = {"Dog", "Cat", "Tiger", "Elephant", "Zebra"};
        String dangerous=null;
        String notDangerous=null;
        for (String animal : animals){
            switch (animal) {
                case "Tiger":
                        dangerous = animal;
                case "Dog" :case "Cat" :case "Elephant" :case "Zebra":
                    notDangerous = animal;
            }
        }
        assertEquals(notDangerous, __);
        assertEquals(dangerous, __);
    }

}
