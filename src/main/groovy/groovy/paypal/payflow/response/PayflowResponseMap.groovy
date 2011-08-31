/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovy.paypal.payflow.response

import org.codehaus.groovy.runtime.DefaultGroovyMethods

/**
 * Payflow response Map. Allows for converting the values into response objects.
 *
 * @author Benjamin Muschko
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
