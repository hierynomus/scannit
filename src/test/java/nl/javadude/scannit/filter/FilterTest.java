/* License added by: GRADLE-LICENSE-PLUGIN
 *
 *    Copyright 2011 Jeroen van Erp (jeroen@javadude.nl)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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

