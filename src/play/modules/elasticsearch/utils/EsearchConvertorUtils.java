/**
 *  This file is part of LogiSima-play-elasticsearch.
 *
 *  LogiSima-play-solr is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  LogiSima-play-elasticsearch is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with LogiSima-play-elasticsearch.  If not, see <http://www.gnu.org/licenses/>.
 */
package play.modules.elasticsearch.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import play.Logger;
import play.modules.elasticsearch.annotation.ESearchFieldIgnore;

/**
 * 
 * @author bsimard
 * 
 */
public class EsearchConvertorUtils {

    /**
     * Method to convert a JPA object to a ESearch Input Document.
     * 
     * @param object the object to convert
     * @return return the conversion of object as a solrInputDocument
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static XContentBuilder objectToXContentBuilder(Object object) {
        XContentBuilder xContent = null;

        try {
            xContent = XContentFactory.jsonBuilder().startObject();
            // for all fields into the object with the ESearchField annotation, we
            // added it into the solr document
            for (java.lang.reflect.Field field : object.getClass().getFields()) {
                ESearchFieldIgnore ESearchFieldIgnore = field.getAnnotation(ESearchFieldIgnore.class);
                String name = field.getName();

                // if field is not ignore and it's not a complexe type
                if (ESearchFieldIgnore == null && !field.getType().isArray()
                        && !field.getType().isAssignableFrom(Collection.class)
                        && (ESearchConstant.IGNORE_FIELDS.contains(name) == false)) {

                    Logger.debug("[ESearch]: Indexing field " + name);
                    String value = null;

                    if (field.getType().equals(String.class)) {
                        value = (String) field.get(object);
                    }
                    else {
                        value = field.get(object).toString();
                    }
                    if (value != null) {
                        xContent.field(name, value);
                    }
                }
            }
        } catch (IOException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } finally {
            xContent.close();
        }
        return xContent;
    }

    /**
     * Convert a file to an array of bytes.
     * 
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static byte[] converteFileToByte(File file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); // no doubt here is 0
            }
        } catch (IOException ex) {
            Logger.error("[ESearch]: Convert file to bytes error -> " + ex.toString());
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

}
