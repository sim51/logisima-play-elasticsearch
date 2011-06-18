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
package play.modules.elasticsearch.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import play.modules.elasticsearch.utils.ESearchUtils;

/**
 * The ESearchFieldIgnore annotation.
 * 
 * @author bsimard
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ESearchField {

    float boost() default ESearchUtils.BOOST_NORMAL;

    boolean store() default ESearchUtils.STORE;

    /**
     * 
     * analyzed (default) or not_analyzed
     */
    String index() default ESearchUtils.INDEX;

    boolean include_in_all() default ESearchUtils.INCLUDE_IN_ALL;

    /**
     * 
     * Types are : string, integer, float, boolean, date, ip, geo_point, attachment
     * 
     */
    String type();

}
