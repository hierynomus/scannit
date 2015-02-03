package nl.javadude.scannit.registry;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

public class RegistryHelperTest {
    @Test
    public void shouldGetTypesAnnotatedWithEmptyRegistry() {
        RegistryHelper rh = new RegistryHelper(new Registry());
        assertThat(rh.getTypesAnnotatedWith(Test.class, true), is(empty()));
    }
}