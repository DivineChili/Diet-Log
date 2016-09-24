package net.WhaleTech.Handlers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Collectors;

public class FileHandler
{
    public static String readFile(File file) throws IOException
    {
        return FileUtils.readFileToString(file, "UTF-8");
    }

    public static String readFile(URL file) throws Exception
    {
        return FileUtils.readFileToString(new File(file.toURI()), "UTF-8");
    }

    public static String readFile(String resourceReference) throws Exception
    {
        InputStream in = ClassLoader.getSystemResourceAsStream(resourceReference);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return reader.readLine();
    }
}
