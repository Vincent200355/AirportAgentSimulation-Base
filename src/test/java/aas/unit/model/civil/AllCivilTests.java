package aas.unit.model.civil;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AircraftTest.class,
	CheckInTest.class,
	DomainNameServerTest.class,
	SimplePaxTest.class
})
public class AllCivilTests {}
