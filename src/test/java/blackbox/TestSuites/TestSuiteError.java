package blackbox.TestSuites;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Test suite: registrazione nuova prenotazione!")
@SelectPackages({"blackbox.RegistraPrenotazione"})
@IncludeTags({"error"})
public class TestSuiteError {
}
