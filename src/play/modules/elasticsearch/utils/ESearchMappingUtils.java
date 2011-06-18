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

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;

import play.Logger;
import play.modules.elasticsearch.annotation.ESearchField;
import play.modules.elasticsearch.exception.ESearchException;
import antlr.collections.List;

/**
 * 
 * @author bsimard
 * 
 */
public class ESearchMappingUtils {

    public static String doMappingEntity(Class entity, boolean indexedAllFields) throws ESearchException {
        String mapping = "\t\t{\n";

        mapping += "\t\t\t\"" + entity.getSimpleName() + "\" {\n";
        mapping += "\t\t\t\t\"properties\" : {\n";

        // for all field
        for (Field field : entity.getFields()) {
            mapping += doMappingField(field, indexedAllFields);
        }

        mapping += "\t\t\t\t}\n";
        mapping += "\t\t}\t\n";
        return mapping;
    }

    public static String doMappingField(Field field, boolean indexedAllFields) throws ESearchException {

        ESearchField esearchField = field.getAnnotation(ESearchField.class);
        String mapping = "";

        // If field has to be indexing
        if (esearchField != null | indexedAllFields) {

            mapping = "\t\t\t\t\t\"" + field.getName() + "\" : {";
            // mapping for field
            mapping += "\"type\" : \"" + ESearchMappingUtils.getESearchType(field) + "\"";

            // if annotation is specified
            if (esearchField != null) {
                if (esearchField.boost() != ESearchUtils.BOOST_NORMAL) {
                    mapping += ", \"boost\" : \"" + esearchField.boost() + "\"";
                }
                if (esearchField.index() != ESearchUtils.INDEX) {
                    mapping += ", \"index\" : \"analyzed\"";
                }
                if (esearchField.store()) {
                    mapping += ", \"store\" : \"yes\"";
                }
                if (!esearchField.include_in_all()) {
                    mapping += ", \"include_in_all\" : \"false\"";
                }
            }

            mapping += "}\n";
        }
        return mapping;
    }

    public static String getESearchType(Field field) throws ESearchException {
        String type = null;

        // if into annotation type is definied
        ESearchField esearchField = field.getAnnotation(ESearchField.class);
        if (esearchField != null && !esearchField.type().isEmpty()) {
            type = esearchField.type();
            if (type.equalsIgnoreCase("string") || type.equalsIgnoreCase("integer") || type.equalsIgnoreCase("float")
                    || type.equalsIgnoreCase("boolean") || type.equalsIgnoreCase("date") || type.equalsIgnoreCase("ip")
                    || type.equalsIgnoreCase("geo_point") || type.equalsIgnoreCase("attachment")) {
                Logger.debug("[ESearch]: field type for " + field.getName() + " is set by annotation : " + type);
            }
            else {
                throw new ESearchException("Type " + type + " is unknowed for annotation ESearchField");
            }
        }
        // here we guess
        else {
            // boolean
            if (Boolean.class == field.getType() || boolean.class == field.getType()) {
                type = "boolean";
            }
            // integer
            if (Integer.class == field.getType() || int.class == field.getType() || short.class == field.getType()
                    || long.class == field.getType()) {
                type = "integer";
            }
            // float
            if (float.class == field.getType() || Double.class == field.getType()) {
                type = "float";
            }
            // date
            if (Date.class == field.getType() || Double.class == field.getType()) {
                type = "date";
            }
            // list or map
            if (List.class == field.getType() || Map.class == field.getType()) {

            }
            // JPA Object
            if (Entity.class == field.getType()) {

            }
            // default value
            if (type == null) {
                type = "string";
            }
        }

        return type;
    }
}
