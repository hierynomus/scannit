package nl.javadude.scannit.metadata;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
import javassist.bytecode.*;
import javassist.bytecode.annotation.Annotation;

import java.io.*;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class JavassistHelper {

    public static ClassFile readFile(TFile file) {
        try {
            TFileInputStream tFileInputStream = new TFileInputStream(file);
            return readFromInputStream(tFileInputStream);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find file: " + file, e);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read file: " + file, e);
        }
    }

    public static ClassFile readFromInputStream(InputStream is) throws IOException {
        if (is == null) throw new IllegalArgumentException("Cannot read from a null inputstream");
        DataInputStream in = null;
        try {
            in = new DataInputStream(new BufferedInputStream(is));
            return new ClassFile(in);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new IllegalStateException("Could not close inputstream", e);
                }
            }
        }
    }

    public static List<String> getTypeAnnotations(ClassFile file) {
        AnnotationsAttribute attribute = (AnnotationsAttribute) file.getAttribute(AnnotationsAttribute.visibleTag);
        return readAnnotations(attribute);
    }

    private static List<String> readAnnotations(AnnotationsAttribute attribute) {
        if (attribute == null) return newArrayList();
        Annotation[] annotations = attribute.getAnnotations();
        return Lists.transform(Lists.newArrayList(annotations), new Function<Annotation, String>() {
            public String apply(Annotation input) {
                return input.getTypeName();
            }
        });
    }

    public static List<String> getMethodAnnotations(MethodInfo method) {
        AnnotationsAttribute attribute = (AnnotationsAttribute) method.getAttribute(AnnotationsAttribute.visibleTag);
        return readAnnotations(attribute);
    }

    public static String getDescription(ClassFile file, MethodInfo method) {
        return new StringBuilder(file.getName()).append(".").append(method.getName()).append("(").append(parameters(method)).append(")").toString();
    }

    private static String parameters(MethodInfo method) {
//        Descriptor.method.getDescriptor();
        return null;
    }
}
