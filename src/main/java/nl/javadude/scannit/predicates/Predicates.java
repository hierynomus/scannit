package nl.javadude.scannit.predicates;

import java.util.Collection;
import java.util.regex.Pattern;

public class Predicates {

    public static <T> Predicate<T> and(final Predicate<? super T> p1, final Predicate<? super T> p2) {
        return new Predicate<T>() {
            @Override
            public boolean apply(T t) {
                return p1.apply(t) && p2.apply(t);
            }
        };
    }

    public static <T> Predicate<T> or(final Predicate<? super T> p1, final Predicate<? super T> p2) {
        return new Predicate<T>() {
            @Override
            public boolean apply(T t) {
                return p1.apply(t) || p2.apply(t);
            }
        };
    }

    public static <T> Predicate<T> or(final Collection<? extends Predicate<? super T>> ps) {
        return new Predicate<T>() {
            @Override
            public boolean apply(T t) {
                for (Predicate<? super T> p : ps) {
                    if (p.apply(t)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static <T> Predicate<T> not(final Predicate<T> p) {
        return new Predicate<T>() {
            @Override
            public boolean apply(T t) {
                return !p.apply(t);
            }
        };
    }

    public static <T> Predicate<T> equalTo(final T value) {
        return new Predicate<T>() {
            @Override
            public boolean apply(T t) {
                return t.equals(value);
            }
        };
    }

    public static Predicate<CharSequence> containsPattern(final String regex) {
        return new Predicate<CharSequence>() {
            @Override
            public boolean apply(CharSequence t) {
                return Pattern.compile(regex).matcher(t).matches();
            }
        };
    }

    public static <T> Predicate<T> alwaysTrue() {
        return new Predicate<T>() {
            @Override
            public boolean apply(T t) {
                return true;
            }
        };
    }
}
