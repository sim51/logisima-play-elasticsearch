package play.modules.elasticsearch.utils;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Elasticsearch constant class.
 * 
 * @author bsimard
 * 
 */
public class ESearchConstant {

    /** The IGNOR e_ fields. */
    public static List<String>  IGNORE_FIELDS  = new ArrayList<String>();
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
}
