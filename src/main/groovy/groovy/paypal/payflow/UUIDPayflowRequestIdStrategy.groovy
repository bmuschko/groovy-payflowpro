package groovy.paypal.payflow

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/20/11
 * Time: 8:06 AM
 * To change this template use File | Settings | File Templates.
 */
class UUIDPayflowRequestIdStrategy implements PayflowRequestIdStrategy {
    @Override
    String getRequestId() {
        UUID.randomUUID()
    }
}
