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
package play.modules.elasticsearch;

import play.PlayPlugin;

/**
 * Integrate the Elasticsearch module to the play lifecycle, for catching event
 * on JPDA Object.
 * 
 * @author bsimard
 * 
 */
public class ESearchPlugin extends PlayPlugin {

    @Override
    public void onApplicationStart() {
        ESearch.init();
    }

    @Override
    public void onEvent(String message, Object context) {
        if (!message.startsWith("JPASupport"))
            return;
        if (message.equals("JPASupport.objectPersisted") || message.equals("JPASupport.objectUpdated")) {
            ESearch.index(context);
        }
        else
            if (message.equals("JPASupport.objectDeleted")) {
                ESearch.unIndex(context);
            }
    }

}
