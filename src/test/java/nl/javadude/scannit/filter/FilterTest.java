package nl.javadude.scannit.filter;

import org.junit.Before;
import org.junit.Test;

import static nl.javadude.scannit.filter.Filter.chain;
import static nl.javadude.scannit.filter.Filter.exclude;
import static nl.javadude.scannit.filter.Filter.include;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FilterTest {

    @Test
    public void shouldIncludeCorrectly() {
        assertThat(include("^nl.*").apply("nl.javadude"), is(true));
        assertThat(include("^nl.*").apply("com.javadude"), is(false));
    }

    @Test
    public void shouldExcludeCorrectly() {
        assertThat(exclude("^nl.*").apply("nl.javadude"), is(false));
        assertThat(exclude("^nl.*").apply("com.javadude"), is(true));
    }

    @Test
    public void shouldChainFiltersCorrectly() {
        Filter.FilterChain chain = chain(include("^nl.*"), exclude(".*foo$"));
        assertThat(chain.apply("nl.javadude"), is(true));
        assertThat(chain.apply("nl.javadude.foo"), is(false));
    }
}
