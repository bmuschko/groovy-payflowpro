package groovy.paypal.payflow

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/20/11
 * Time: 7:27 AM
 * To change this template use File | Settings | File Templates.
 */
interface HttpsSender {
    def sendPost(String url, Map<String, String> params)
}
