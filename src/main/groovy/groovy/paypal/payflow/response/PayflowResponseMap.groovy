package groovy.paypal.payflow.response

import org.codehaus.groovy.runtime.DefaultGroovyMethods

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/28/11
 * Time: 7:13 AM
 * To change this template use File | Settings | File Templates.
 */
class PayflowResponseMap implements Map {
    @Delegate
    Map inner = [:]

    PayflowResponseMap(Map source) {
        if(source) {
            inner.putAll(source)
        }
    }

    @Override
    Object asType(Class type) {
        if(type == PayflowResponse) {
            return new PayflowResponse(result: inner.RESULT.toInteger(), responseMessage: inner.RESPMSG, pnRef: inner.PNREF, authCode: inner.AUTHCODE)
        }

        DefaultGroovyMethods.asType(inner, type)
    }

    boolean isApproved() {
        inner.containsKey('RESULT') ? inner.RESULT.toInteger() == 0 : false
    }
}
