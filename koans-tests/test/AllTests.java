import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.sandwich.koan.AnonymousInnerClassTest;
import com.sandwich.koan.KoansResultTest;
import com.sandwich.koan.TestUtilsTest;
import com.sandwich.koan.cmdline.CommandLineArgumentBuilderTest;
import com.sandwich.koan.constant.ArgumentTypeTest;
import com.sandwich.koan.path.PathToEnlightenmentTest;
import com.sandwich.koan.path.RbVariableInjectorTest;
import com.sandwich.koan.runner.AppLauncherTest;
import com.sandwich.koan.runner.AppReadinessForDeploymentTest;
import com.sandwich.koan.runner.CommandLineTestCaseTest;
import com.sandwich.koan.runner.ConsolePresenterTest;
import com.sandwich.koan.runner.ui.AbstractSuitePresenterTest;
import com.sandwich.util.KoanComparatorTest;
import com.sandwich.util.StringsTest;
import com.sandwich.util.io.DirectoryManagerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({AnonymousInnerClassTest.class, KoansResultTest.class, TestUtilsTest.class, 
	CommandLineArgumentBuilderTest.class, ArgumentTypeTest.class, CommandLineTestCaseTest.class, 
	PathToEnlightenmentTest.class, RbVariableInjectorTest.class, AppLauncherTest.class, 
	AppReadinessForDeploymentTest.class, ConsolePresenterTest.class, AbstractSuitePresenterTest.class, 
	KoanComparatorTest.class, StringsTest.class, DirectoryManagerTest.class})
public class AllTests {

}
