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
package play.modules.elasticsearch.searching;

import javassist.CtClass;
import play.classloading.ApplicationClasses.ApplicationClass;
import play.classloading.enhancers.Enhancer;

/**
 * Enhance Model class to add searching method for Elasticsearch.
 * 
 * @author bsimard
 * 
 */
public class ESearchModelEnhancer extends Enhancer {

    public void enhanceThisClass(ApplicationClass appClass) throws Exception {
        CtClass ctClass = makeClass(appClass);

        // Only enhance model classes.
        if (!ctClass.subtypeOf(classPool.get("play.db.jpa.JPABase"))) {
            return;
        }

        // adding esFind
        // final CtMethod esFind = CtMethod.make(ESearchModelEnhancer.ES_FIND_CODE, ctClass);
        // ctClass.addMethod(esFind);
        //
        // // adding esFindById
        // final CtMethod esFindById = CtMethod.make(ESearchModelEnhancer.ES_FIND_BY_ID_CODE, ctClass);
        // ctClass.addMethod(esFindById);
        //
        // // adding esFindAll
        // final CtMethod esFindAll = CtMethod.make(ESearchModelEnhancer.ES_FIND_ALL_CODE, ctClass);
        // ctClass.addMethod(esFindAll);
        //
        // // adding esSave
        // final CtMethod esSave = CtMethod.make(ESearchModelEnhancer.ES_SAVE_CODE, ctClass);
        // ctClass.addMethod(esSave);
        //
        // // adding esDelete
        // final CtMethod esDelete = CtMethod.make(ESearchModelEnhancer.ES_DELETE_CODE, ctClass);
        // ctClass.addMethod(esDelete);

        // Done - update class.
        appClass.enhancedByteCode = ctClass.toBytecode();
        ctClass.defrost();
    }

    private static final String ES_FIND_CODE       = "";
    private static final String ES_FIND_BY_ID_CODE = "";
    private static final String ES_FIND_ALL_CODE   = "";
    private static final String ES_SAVE_CODE       = "";
    private static final String ES_DELETE_CODE     = "";
}
