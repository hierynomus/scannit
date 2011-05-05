# Scannit: A Java metadata scanner
Scannit is a Java metadata scanner inspired upon [scannotation](http://scannotation.sourceforge.net/) and [reflections](http://code.google.com/p/reflections/). It currently contains scanners which scan the classpath for:

- Class level annotations
- Field level annotations
- Method level annotations
- Sub/Supertype hierarchy

## Reasoning
Reflections offers much of the same functionality as Scannit, however it hasn't been maintained much recently. There are a lot of open issues against it, to which the author hasn't responded much. Furthermore it contained some bugs which we needed fixed, like supporting spaces in classpaths. So we decided to roll our own.

## Technology
Scannit relies on some libraries to provide services, these are:

- [Javassist](http://www.csg.is.titech.ac.jp/~chiba/javassist/): For the bytecode reading of classfiles
- [Truezip](http://truezip.java.net/): For the scanning of the classpath and traversing through jars as though they're a filesystem
- [Guava](http://code.google.com/p/guava-libraries/): For the collections API
