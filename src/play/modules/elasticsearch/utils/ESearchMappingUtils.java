package play.modules.elasticsearch.utils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;

import play.Logger;
import play.modules.elasticsearch.annotation.ESearchField;
import play.modules.elasticsearch.exception.ESearchException;
import antlr.collections.List;

public class ESearchMappingUtils {

    public static String doMappingEntity(Class entity) throws ESearchException {
        String mapping = "{\n";

        mapping += "\t\"" + entity.getSimpleName() + "\" {\n";
        mapping += "\t\t\"properties\" : {\n";

        // for all field
        for (Field field : entity.getClass().getFields()) {
            doMappingField(field);
        }

        mapping += "\t}\n";
        mapping += "}\n";
        return mapping;
    }

    public static String doMappingField(Field field) throws ESearchException {
        String mapping = "\t\t\t\"" + field.getName() + "\" : {";

        // mapping for field
        mapping += "\"type\" : \"" + ESearchMappingUtils.getESearchType(field) + "\"";

        ESearchField esearchField = field.getAnnotation(ESearchField.class);
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
            if (Boolean.class.isInstance(field) || boolean.class.isInstance(field)) {
                type = "boolean";
            }
            // integer
            if (Integer.class.isInstance(field) || int.class.isInstance(field) || short.class.isInstance(field)
                    || long.class.isInstance(field)) {
                type = "integer";
            }
            // float
            if (float.class.isInstance(field) || Double.class.isInstance(field)) {
                type = "float";
            }
            // date
            if (Date.class.isInstance(field) || Double.class.isInstance(field)) {
                type = "float";
            }
            // list or map
            if (List.class.isInstance(field) || Map.class.isInstance(field)) {

            }
            // JPA Object
            if (Entity.class.isInstance(field)) {

            }
            // default value
            if (type == null) {
                type = "string";
            }
        }

        return type;
    }
}
