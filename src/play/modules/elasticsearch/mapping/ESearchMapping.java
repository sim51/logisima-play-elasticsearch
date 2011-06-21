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
package play.modules.elasticsearch.mapping;

import java.io.File;
import java.util.List;

import javax.persistence.Entity;

import play.Play;
import play.modules.elasticsearch.annotation.ESearchIndexed;
import play.modules.elasticsearch.exception.ESearchException;
import play.modules.elasticsearch.utils.ESearchUtils;

/**
 * Main class to generate the elasticsearch mapping configuration.
 * 
 * @author bsimard
 * 
 */
public class ESearchMapping {

    public String mapping;

    /**
     * 
     * 
     * @return
     * @throws ESearchException
     */
    public String generateMapping() throws ESearchException {
        mapping = "{\n";
        mapping += "\t" + ESearchUtils.getIndexName() + " : {\n";

        // we search all entities classes
        List<Class> entities = Play.classloader.getAnnotatedClasses(Entity.class);
        for (Class entity : entities) {
            // if entity has ESearchIndexed annotation
            ESearchIndexed esIndexed = (ESearchIndexed) entity.getAnnotation(ESearchIndexed.class);
            if (esIndexed != null) {
                mapping += ESearchMappingUtils.doMappingEntity(entity, esIndexed.allFields());
            }
        }

        mapping += "\t}\n";
        mapping += "}\n";

        return mapping;
    }

    /**
     * Main method for play command line.
     * 
     * @param args
     * @throws ESearchException
     */
    public static void main(String[] args) throws ESearchException {
        // we initiate play! framework
        File root = new File(System.getProperty("application.path"));
        Play.init(root, System.getProperty("play.id", ""));

        ESearchMapping esMapping = new ESearchMapping();

        System.out.println(esMapping.generateMapping());
    }
}
