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
package play.modules.elasticsearch;

import java.util.List;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import play.Logger;
import play.Play;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;
import play.modules.elasticsearch.annotation.ESearchIndexed;
import play.modules.elasticsearch.utils.ESearchUtils;
import play.modules.elasticsearch.utils.EsearchConvertorUtils;

/**
 * 
 * @author bsimard
 * 
 */
public class ESearch {

    /**
     * elasticsearch client.
     */
    private static Client client;

    /**
     * The initialization method for elasticsearch client.
     */
    public static void init() {
        Logger.info("[ESearch]: Plugin initialization");

        // we create a transportClient
        Builder settings = ImmutableSettings.settingsBuilder();
        // if sniff mode is activated
        if (Play.configuration.containsKey("elasticsearch.sniff")
                && Play.configuration.getProperty("elasticsearch.sniff").equalsIgnoreCase("false")) {
            settings.put("client.transport.sniff", false);
        }
        else {
            settings.put("client.transport.sniff", true);
        }
        settings.build();
        TransportClient transportClient = new TransportClient(settings);

        // we read configuration to khnow cluster nodes
        int nb = 1;
        String confLine;
        String host;
        Integer port;
        while (Play.configuration.containsKey("elasticsearch." + nb + ".host")) {
            confLine = (String) Play.configuration.getProperty("elasticsearch." + nb + ".host");
            host = confLine.split(":")[0];
            port = Integer.valueOf(confLine.split(":")[1]);
            transportClient.addTransportAddress(new InetSocketTransportAddress(host, port.intValue()));
            nb++;
        }

        client = transportClient;
    }

    /**
     * Method to add the object to the elasticsearch index.
     * 
     * @param object the object to index
     */
    public static void index(Object object) {
        Logger.debug("[ESearch]: Start to index object " + object);
        Boolean indexable = ESearchUtils.isESearchIndexable(object);
        Logger.info("[ESearch]: object is indexable :" + indexable);
        if (indexable) {
            try {
                IndexResponse response = client
                        .prepareIndex(ESearchUtils.getIndexName(), ESearchUtils.getTypeName(object),
                                ESearchUtils.getObjectId(object))
                        .setSource(EsearchConvertorUtils.objectToXContentBuilder(object)).execute().actionGet();
                Logger.debug("[ESearch]: indexing response is : " + response.toString());
            } catch (Exception e) {
                Logger.error("[ESearch]: the object " + object + "cannot be indexed ! ->" + e.toString());
            }
        }
    }

    /**
     * Method to delete the object from the SolR index.
     * 
     * @param object the object to unindex
     */
    public static void unIndex(Object object) {
        Logger.info("[ESearch]: Start to delete object " + object);
        try {
            DeleteResponse response = client
                    .prepareDelete(ESearchUtils.getIndexName(), ESearchUtils.getTypeName(object),
                            ESearchUtils.getObjectId(object)).execute().actionGet();
            Logger.debug("[ESearch]: deleting response is : " + response.toString());
        } catch (Exception e) {
            Logger.error("[ESearch]: the object " + object + "cannot be deleted !->" + e.toString());
        }
    }

    /**
     * Method to delete elasticsearch index (delete everything).
     */
    public static void deleteIndex() {
        Logger.info("[ESearch]: Start to delete index");

        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest();
        deleteIndexRequest.indices(ESearchUtils.getIndexName());
        DeleteIndexResponse response = client.admin().indices().delete(deleteIndexRequest).actionGet();

        Logger.debug("[ESearch]: delete index response is : " + response.toString());
    }

    /**
     * Method to reset elasticsearch index (delete & index all model).
     */
    public static void resetIndex() {
        Logger.info("[ESearch]: Start to clean indexes");

        // we delete all things
        deleteIndex();

        // we search all classes with the annotation ESearchIndexed
        List<ApplicationClass> classes = Play.classes.getAnnotatedClasses(ESearchIndexed.class);
        Logger.debug("[ESearch]: There is " + classes.size() + " ESearch indexable classes");
        for (ApplicationClass applicationClass : classes) {
            // we search all the object for the class
            List<GenericModel> objects = (List<GenericModel>) JPA.em()
                    .createQuery("select e from " + applicationClass.javaClass.getCanonicalName() + " as e")
                    .getResultList();
            for (GenericModel genericModel : objects) {
                index(genericModel);
            }
        }

        Logger.info("[ESearch]: clean index finished");
    }

    /**
     * Method to configure the index mapping for elasticsearch
     */
    public static void doMapping() {

    }
}
