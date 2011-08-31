/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovy.paypal.payflow.util

import java.util.jar.Attributes
import java.util.jar.JarFile
import java.util.jar.Manifest

/**
 * Utility that lets you read attributes from manifest file.
 *
 * @author Benjamin Muschko
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
