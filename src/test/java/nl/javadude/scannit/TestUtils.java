package nl.javadude.scannit;

import javassist.bytecode.ClassFile;
import nl.javadude.scannit.metadata.JavassistHelper;

import java.io.IOException;
import java.io.InputStream;

public class TestUtils {
    public static InputStream getStreamFromClass(Class<?> aClass) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(aClass.getName().replaceAll("\\.", "/") + ".class");
    }

    public static ClassFile readClass(Class<?> aClass) throws IOException {
        InputStream resourceAsStream = getStreamFromClass(aClass);
        return JavassistHelper.readFromInputStream(resourceAsStream);
    }


}
