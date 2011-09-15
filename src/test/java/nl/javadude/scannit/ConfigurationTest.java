package nl.javadude.scannit;

import nl.javadude.scannit.scanner.SubTypeScanner;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ConfigurationTest {

    @Test
    public void shouldAddFilterToScannersForNewPrefix() {
        SubTypeScanner subTypeScanner = new SubTypeScanner();
        assertThat(subTypeScanner.accepts("foo.bar"), equalTo(true));
        Configuration with = Configuration.config().with(subTypeScanner);
        with.scan("nl.javadude").wireScanners();
        assertThat(subTypeScanner.accepts("foo.bar"), equalTo(false));
    }
    
    @Test
    public void shouldAddFiltersForPrefixesToNewScanner() {
        Configuration scan = Configuration.config().scan("nl.javadude");
        SubTypeScanner subTypeScanner = new SubTypeScanner();
        assertThat(subTypeScanner.accepts("foo.bar"), equalTo(true));
        scan.with(subTypeScanner).wireScanners();
        assertThat(subTypeScanner.accepts("foo.bar"), equalTo(false));
    }
	
	@Test
	public void shouldFilterPrefixesWithOr() {
		SubTypeScanner subTypeScanner = new SubTypeScanner();
		assertThat(subTypeScanner.accepts("foo.bar"), equalTo(true));
		Configuration.config().scan("nl.javadude").scan("foo").with(subTypeScanner).wireScanners();
		assertThat(subTypeScanner.accepts("foo.bar"), equalTo(true));
	}
}
