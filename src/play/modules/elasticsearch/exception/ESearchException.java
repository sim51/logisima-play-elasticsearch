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
package play.modules.elasticsearch.exception;

/**
 * Exception class for elasticsearch module.
 * 
 * @author bsimard
 * 
 */
public class ESearchException extends Exception {

    /**
     * serialUID.
     */
    private static final long serialVersionUID = 8900000567753471797L;

    /**
     * Construct a <code>ESearchException</code> with the specified detail message.
     * 
     * @param msg the detail message
     */
    public ESearchException(String message) {
        super(message);
    }

    /**
     * Construct a <code>ESearchException</code> with the specified detail message and nested exception.
     * 
     * @param msg the detail message
     * @param cause the nested exception
     */
    public ESearchException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct a <code>ESearchException</code> with the snested exception.
     * 
     * @param cause the nested exception
     */
    public ESearchException(Throwable cause) {
        super(cause);
    }

}
