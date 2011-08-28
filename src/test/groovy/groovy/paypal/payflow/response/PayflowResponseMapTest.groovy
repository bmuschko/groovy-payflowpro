package groovy.paypal.payflow.response

import spock.lang.Specification

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/28/11
 * Time: 12:59 PM
 * To change this template use File | Settings | File Templates.
 */
class PayflowResponseMapTest extends Specification {
    def "Check successful approval"() {
        given: "a Map with result key"
            def params = ['RESULT': '0']
            PayflowResponseMap response = new PayflowResponseMap(params)

        when: "asked for approval status"
            boolean approved = response.isApproved()

        then: "reports approved"
            approved == true
    }

    def "Check failed approval"() {
        given: "a Map with result key"
            def params = ['RESULT': '24']
            PayflowResponseMap response = new PayflowResponseMap(params)

        when: "asked for approval status"
            boolean approved = response.isApproved()

        then: "reports not approved"
            approved == false
    }

    def "Check unresolvable approval"() {
        given: "a Map without result key"
            def params = ['UNKNOW': 'test']
            PayflowResponseMap response = new PayflowResponseMap(params)

        when: "asked for approval status"
            boolean approved = response.isApproved()

        then: "reports not approved"
            approved == false
    }
}
