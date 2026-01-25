package blackbox.TestSuites;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Test suite: flitro clienti")
@SelectPackages({"blackbox.RegistraImpiegato", "blackbox.RegistraPrenotazione", "blackbox.CambiaStatoCamera"})
@IncludeTags({"fail"})
public class TestSuiteFails {

}
