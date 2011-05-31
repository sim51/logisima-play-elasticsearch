/**
 *  This file is part of LogiSima-play-solr.
 *
 *  LogiSima-play-solr is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  LogiSima-play-solr is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with LogiSima-play-solr.  If not, see <http://www.gnu.org/licenses/>.
 */
package play.modules.elasticsearch.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import play.Logger;
import play.Play;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;
import play.modules.elasticsearch.annotation.ESearchIndexed;

public class ESearchUtils {

    /** The IGNOR e_ fields. */
    static List<String>         IGNORE_FIELDS  = new ArrayList<String>();
    static {
        IGNORE_FIELDS.add("avoidCascadeSaveLoops");
        IGNORE_FIELDS.add("willBeSaved");
        IGNORE_FIELDS.add("serialVersionId");
        IGNORE_FIELDS.add("serialVersionUID");
    }

    /**
     * Default constant
     */
    public static final float   BOOST_NORMAL   = (float) 1.0;
    public static final boolean STORE          = false;
    public static final String  INDEX          = "not_analyzed";
    public static final boolean INCLUDE_IN_ALL = true;

    /**
     * Method to get the index
     */
    public static String getIndexName() {
        String indexname = "play";
        if (Play.configuration.containsKey("elasticsearch.indexname")) {
            indexname = (String) Play.configuration.get("elasticsearch.indexname");
        }
        return indexname;
    }

    /**
     * Method to determinate if the object can be indexed by Elasticsearch.
     * 
     * @param object
     *            the object to index or not
     * @return Boolean
     */
    public static Boolean isESearchIndexable(Object object) {
        Boolean indexable = Boolean.FALSE;
        // if object is a POJO, and if it has the good annotation , then the
        // object is indexable
        if (object instanceof GenericModel && object.getClass().getAnnotation(ESearchIndexed.class) != null) {
            indexable = Boolean.TRUE;
        }
        return indexable;
    }

    /**
     * Method that return the name type of the object.
     */
    public static String getTypeName(Object object) {
        GenericModel genericModel = (GenericModel) object;
        String typename = null;
        // if the object extend from the play's model class
        if (genericModel instanceof Model) {
            typename = ((Model) genericModel).getClass().getSimpleName();
        }
        return typename;
    }

    /**
     * Method that return an indeitifer for the object.
     * 
     * @param Object
     * @return the id field value
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static String getObjectId(Object object) throws IllegalArgumentException, IllegalAccessException {
        GenericModel genericModel = (GenericModel) object;
        String objectId = null;
        // if the object extend from the play's model class
        if (genericModel instanceof Model) {
            // we take the model id
            objectId = ((Model) genericModel).id.toString();
        }
        // else we try to get value of the field with id annotation
        else {
            // we look up for the field with the id annotation
            Field fieldId = null;
            for (java.lang.reflect.Field field : genericModel.getClass().getFields()) {
                if (field.getAnnotation(Id.class) != null) {
                    fieldId = field;
                }
            }
            if (fieldId != null) {
                objectId = fieldId.get(genericModel).toString();
            }
        }
        Logger.debug("[ESearch]: Id for object is " + objectId);
        return objectId;
    }

}
