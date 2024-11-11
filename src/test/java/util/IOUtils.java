package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Provides I/O related helper methods.
 * <p>
 */
public class IOUtils {

    private IOUtils() {
        //do nothing
    }

    /**
     * Reads all data from <code>inputStream</code> and converts to array of bytes
     *
     * @param inputStream instance of {@link InputStream}
     * @return an array of bytes
     */
    public static byte[] readAll(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();

    }


}
