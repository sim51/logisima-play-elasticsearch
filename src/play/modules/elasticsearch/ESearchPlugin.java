/**
 *  This file is part of LogiSima-play-elasticsearch.
 *
 *  LogiSima-play-elasticsearch is free software: you can redistribute it and/or modify
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
package play.modules.elasticsearch;

import play.PlayPlugin;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.modules.elasticsearch.indexing.ESearchIndexing;
import play.modules.elasticsearch.searching.ESearchModelEnhancer;

/**
 * Integrate the Elasticsearch module to the play lifecycle.
 * 
 * @author bsimard
 * 
 */
public class ESearchPlugin extends PlayPlugin {

    @Override
    public void onApplicationStart() {
        ESearchIndexing.init();
    }

    @Override
    public void onEvent(String message, Object context) {
        if (!message.startsWith("JPASupport"))
            return;
        if (message.equals("JPASupport.objectPersisted") || message.equals("JPASupport.objectUpdated")) {
            ESearchIndexing.index(context);
        }
        else
            if (message.equals("JPASupport.objectDeleted")) {
                ESearchIndexing.unIndex(context);
            }
    }

    @Override
    public void enhance(ApplicationClass appClass) throws Exception {
        new ESearchModelEnhancer().enhanceThisClass(appClass);
    }

}
