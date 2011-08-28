package groovy.paypal.payflow.util

import java.util.jar.Attributes
import java.util.jar.JarFile
import java.util.jar.Manifest

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/27/11
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
final class ManifestUtils {
    private ManifestUtils() {}

    final static Attributes attributes

    static {
        attributes = getManifestAttributes()
    }

    static getManifestAttributes() {
        File file = new File(ManifestUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI())

        if(file.exists()) {
            try {
                JarFile jarFile = new JarFile(file)
                Manifest manifest = jarFile.getManifest()
                manifest.getMainAttributes()
            }
            catch(IOException e) {
                // ignore when not run from JAR file
            }
        }
    }

    static String getVersion() {
        attributes?.getValue('Implementation-Version')
    }
}
