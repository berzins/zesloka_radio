package utilities;

import org.jetbrains.annotations.Nullable;

import java.io.*;

public class Util {

    /**
     * Perform deep cloning through memory serialization, on failure return null
     */
    @Nullable
    public static <T> T serializedCopy(T obj) {
        ObjectOutputStream out;
        ObjectInputStream in;
        T copy = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            in = new ObjectInputStream(bis);
            copy = (T) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return copy;
    }


    /**
     * Buffered file writing, on failure return false.
     */
    public static <T> boolean writeToFile(String fileName, T obj) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
            out.writeObject(obj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Buffered file reading, return null if reading fails.
     */
    public static <T> T readFromFile(String fileName) {
        try {
            return (T) new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName))).readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public interface Executable {
        void execute();
    }
    public static final void executeInMainThread() {

    }


}
