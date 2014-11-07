package eu.citysdk.participation.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RequestEntryTests.class, 
		UserEntryTests.class,
		ModelIntegrationTests.class })
public class AllTests {

}
