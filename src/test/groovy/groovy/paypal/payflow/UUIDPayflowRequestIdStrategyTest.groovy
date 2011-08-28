package groovy.paypal.payflow

import spock.lang.Specification

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/28/11
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */
class UUIDPayflowRequestIdStrategyTest extends Specification {
    def "Request ID is generated"() {
        given: "a UUID request ID strategy"
            UUIDPayflowRequestIdStrategy requestIdStrategy = new UUIDPayflowRequestIdStrategy()

        when: "request ID is generated"
            String requestId = requestIdStrategy.requestId

        then: "request ID is valid"
            requestId != null
            requestId.length() == 36
            requestId.contains('-')
    }
}
