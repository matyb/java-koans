package beginner;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

import com.sandwich.koan.Koan;

public class AboutVarArgs {

  class ExampleClass {
    public boolean canBeTreatedAsArray(Integer... arguments) { return arguments instanceof Integer[]; }
    public int getLength(Integer... arguments) { return arguments.length; }
    public String verboseLength(String prefix, Object... arguments) { return prefix + arguments.length; }

    // *******
    // The following methods won't compile because Java doesn't allow this type of method declaration
    // *******
    // public String invalidMethodDeclaration(String... arguments, String... otherArguments) { return ""; }
    // public String otherInvalidMethodDeclaration(String... arguments, String otherArgument) { return ""; }
  }

  @Koan
  public void varArgsCanBeTreatedAsArrays() {
    assertEquals(new ExampleClass().canBeTreatedAsArray(1, 2, 3), __);
  }

  @Koan
  public void youCanPassInAsManyArgumentsAsYouLike() {
    assertEquals(new ExampleClass().getLength(1, 2, 3), __);
    assertEquals(new ExampleClass().getLength(1, 2, 3, 4, 5, 6, 7, 8), __);
  }

  @Koan
  public void youCanPassInZeroArgumentsIfYouLike() {
    assertEquals(new ExampleClass().getLength(), __);
  }

  @Koan
  public void youCanHaveOtherTypesInTheMethodSignature() {
    assertEquals(new ExampleClass().verboseLength("This is how many items were passed in: ", 1, 2, 3, 4),
                                                  __);
  }
}
