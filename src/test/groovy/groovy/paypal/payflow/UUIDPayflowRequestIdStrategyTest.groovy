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
