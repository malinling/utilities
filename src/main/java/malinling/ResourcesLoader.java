package malinling;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

public class ResourcesLoader {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ResourcesLoader.class);

    private ResourcesLoader() {
    }

    /**
     * Load resource into string for the given path
     *
     * @param resource
     * @return resource content string
     * @throws IOException
     */
    public static String readString(String resource) throws IOException {
        InputStream input = getResourceAsStream(resource);
        if (input != null) {
            try {
                return IOUtils.toString(input);
            } finally {
                IOUtils.closeQuietly(input);
            }
        }
        return null;
    }

    /**
     * load resource as InputStream for the given path
     *
     * @param resource
     * @return resource content input stream
     * @throws IOException
     */
    public static InputStream getResourceAsStream(String resource) throws IOException {
        URL url = getResource(resource);
        if (url != null) {
            return url.openStream();
        }
        return null;
    }

    /**
     * get the resource URL from the given path
     *
     * @param resource
     * @return
     */
    public static URL getResource(String resource) {
        ClassLoader classLoader = null;
        URL url = null;

        try {
            classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader != null) {
                LOGGER.info(String.format(Locale.US, "Trying to find [%s] using context classloader %s.", resource, classLoader));
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }

            // We could not find resource. Ler us now try with the
            // classloader that loaded this class.
            classLoader = ResourcesLoader.class.getClassLoader();
            if (classLoader != null) {
                LOGGER.info(String.format(Locale.US, "Trying to find [%s] using %s class loader.", resource, classLoader));
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
        } catch (Exception t) {
            LOGGER.warn("Caught Exception while in ResourcesLoader.getResource.", t);
        }

        return ClassLoader.getSystemResource(resource);
    }

    /**
     * Loading properties from the given resource path
     *
     * @param resource
     * @return
     * @throws IOException
     */
    public static Properties loadResourceProperties(String resource) throws IOException {
        InputStream input = null;
        Properties props = new Properties();
        try {
            input = getResourceAsStream(resource);
            if (input != null) {
                props.load(input);
            }
        } finally {
            IOUtils.closeQuietly(input);
        }
        return props;
    }
}

