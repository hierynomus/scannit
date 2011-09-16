# Scannit: An (extensible) Java metadata scanner
Scannit is an extensible Java metadata scanner inspired upon [scannotation](http://scannotation.sourceforge.net/) and [reflections](http://code.google.com/p/reflections/). It currently contains scanners which scan the classpath for:

- Class level annotations
- Field level annotations
- Method level annotations
- Sub/Supertype hierarchy

## Technology
Scannit relies on some libraries to provide services, these are:

- [Javassist](http://www.csg.is.titech.ac.jp/~chiba/javassist/): For the bytecode reading of classfiles
- [Truezip](http://truezip.java.net/): For the scanning of the classpath and traversing through jars as though they're a filesystem
- [Guava](http://code.google.com/p/guava-libraries/): For the collections API

## Usage

```
Configuration config = Configuration.config()
    .with(new SubTypeScanner(), new TypeAnnotationScanner())
    .scan("foo.bar");
Scannit scannit = new Scannit(config);
Set<Class<?>> clazzes = scannit.getTypesAnnotatedWith(MyAnnotation.class);
Set<Class<?>> subClazzes = scannit.getSubTypesOf(ParentClass.class);
```

## Extension
Extending Scannit is pretty easy, you can write a new Scanner by extending nl.javadude.scannit.scanner.AbstractScanner and implementing ```void doScan(ClassFile file, Registry registry)```.
