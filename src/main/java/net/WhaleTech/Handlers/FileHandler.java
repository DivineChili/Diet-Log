package net.WhaleTech.Handlers;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;

/**
 * Basic function class to control file IO
 */
public class FileHandler
{
    /**
     * Reads all the content of a file
     * @param file
     *          the {@link File} which will be read.
     * @return
     *          all the text from the {@link File}
     * @throws IOException
     */
    public static String readFile(File file) throws IOException
    {
        return FileUtils.readFileToString(file, "UTF-8");
    }

    /**
     * Reads all the content of a file from URL location
     * @param file
     *          the {@link URL} to a file.
     * @return
     *          all the text from the {@link File}
     * @throws Exception
     */
    public static String readFile(URL file) throws Exception
    {
        return FileUtils.readFileToString(new File(file.toURI()), "UTF-8");
    }

    /**
     * Used to read all the content from the resource directory.
     * @param resourceReference
     *          the path of the resource
     * @return
     *          all the text from the {@link File}
     * @throws Exception
     */
    public static String readFile(String resourceReference) throws Exception
    {
        InputStream in = ClassLoader.getSystemResourceAsStream(resourceReference);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return reader.readLine();
    }
}
