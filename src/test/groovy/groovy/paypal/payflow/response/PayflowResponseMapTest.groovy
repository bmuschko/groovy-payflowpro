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

import spock.lang.Specification

/**
 * Payflow response map tests.
 *
 * @author Benjamin Muschko
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
