package groovy.paypal.payflow

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/20/11
 * Time: 10:19 AM
 * To change this template use File | Settings | File Templates.
 */
class PayflowApiException extends RuntimeException {
    PayflowApiException(String s) {
        super(s)
    }
}
