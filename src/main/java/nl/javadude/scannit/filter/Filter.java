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

import com.google.common.base.Predicate;

import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public abstract class Filter implements Predicate<String> {

    public static Filter include(String regex) {
        return new IncludeFilter(regex);
    }

    public static Filter exclude(String regex) {
        return new ExcludeFilter(regex);
    }

    public static FilterChain chain() {
        return new FilterChain();
    }

    public static FilterChain chain(Filter... filters) {
        FilterChain chain = new FilterChain();
        for (Filter filter : filters) {
            chain.add(filter);
        }
        return chain;
    }

    /**
     * A Filter that returns true if it is applied to a positive match
     */
    public static class IncludeFilter extends Filter {
        protected Pattern pattern;

        protected IncludeFilter(String regex) {
            this.pattern = Pattern.compile(regex);
        }

        public boolean apply(String match) {
            return pattern.matcher(match).matches();
        }
    }

    /**
     * A Filter that returns true if it is applied to a negative match
     */
    public static class ExcludeFilter extends IncludeFilter {
        protected ExcludeFilter(String regex) {
            super(regex);
        }

        @Override
        public boolean apply(String match) {
            return !super.apply(match);
        }
    }

    /**
     * An 'AND' chain of filters.
     */
    public static class FilterChain extends Filter {
        private List<Filter> filters = newArrayList();

        public boolean apply(String input) {
            boolean applies = true;
            for (Filter filter : filters) {
                applies = applies && filter.apply(input);
            }
            return applies;
        }

        public FilterChain add(Filter filter) {
            filters.add(filter);
            return this;
        }
    }
}

