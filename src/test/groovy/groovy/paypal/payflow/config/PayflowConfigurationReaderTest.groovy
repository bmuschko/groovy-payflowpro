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
package groovy.paypal.payflow.config

import groovy.paypal.payflow.PayflowRequestIdStrategy
import groovy.paypal.payflow.PayflowServer
import groovy.paypal.payflow.UUIDPayflowRequestIdStrategy
import spock.lang.Specification
import spock.lang.FailsWith

/**
 * Payflow configuration reader tests.
 *
 * @author Benjamin Muschko
 */
class PayflowConfigurationReaderTest extends Specification {
    ConfigurationReader payflowConfigurationReader

    def setup() {
        payflowConfigurationReader = new PayflowConfigurationReader()
    }

    def cleanup() {
        payflowConfigurationReader = null
    }

    def "Payflow configuration is parsed for default values"() {
        when: "configuration is read"
            PayflowClientConfiguration payflowClientConfiguration = payflowConfigurationReader.parseConfig('development', 'GroovyPayflowConfig1')

        then: "values are correct"
            payflowClientConfiguration.timeout == 45
            payflowClientConfiguration.requestIdStrategy instanceof UUIDPayflowRequestIdStrategy
            payflowClientConfiguration.server == PayflowServer.TEST
            payflowClientConfiguration.account == null
            payflowClientConfiguration.proxyServer == null
    }

    def "Payflow configuration is parsed for just timeout"() {
        when: "configuration is read"
            PayflowClientConfiguration payflowClientConfiguration = payflowConfigurationReader.parseConfig('development', 'GroovyPayflowConfig2')

        then: "values are correct"
            payflowClientConfiguration.timeout == 56
            payflowClientConfiguration.requestIdStrategy instanceof UUIDPayflowRequestIdStrategy
            payflowClientConfiguration.server == PayflowServer.TEST
            payflowClientConfiguration.account == null
            payflowClientConfiguration.proxyServer == null
    }

    def "Payflow configuration is parsed for just timeout and request ID strategy"() {
        when: "configuration is read"
            PayflowClientConfiguration payflowClientConfiguration = payflowConfigurationReader.parseConfig('development', 'GroovyPayflowConfig3')

        then: "values are correct"
            payflowClientConfiguration.timeout == 56
            payflowClientConfiguration.requestIdStrategy instanceof CurrentTimestampPayflowRequestIdStrategy
            payflowClientConfiguration.server == PayflowServer.TEST
            payflowClientConfiguration.account == null
            payflowClientConfiguration.proxyServer == null
    }

    def "Payflow configuration is parsed for just timeout, request ID strategy and environment"() {
        when: "configuration is read"
            PayflowClientConfiguration payflowClientConfiguration = payflowConfigurationReader.parseConfig('development', 'GroovyPayflowConfig4')

        then: "values are correct"
            payflowClientConfiguration.timeout == 56
            payflowClientConfiguration.requestIdStrategy instanceof CurrentTimestampPayflowRequestIdStrategy
            payflowClientConfiguration.server == PayflowServer.LIVE
            payflowClientConfiguration.account == null
            payflowClientConfiguration.proxyServer == null
    }

    def "Payflow configuration is parsed for just timeout, request ID strategy, environment and account"() {
        when: "configuration is read"
            PayflowClientConfiguration payflowClientConfiguration = payflowConfigurationReader.parseConfig('development', 'GroovyPayflowConfig5')

        then: "values are correct"
            payflowClientConfiguration.timeout == 56
            payflowClientConfiguration.requestIdStrategy instanceof CurrentTimestampPayflowRequestIdStrategy
            payflowClientConfiguration.server == PayflowServer.LIVE
            payflowClientConfiguration.account != null
            payflowClientConfiguration.account.partner == 'Paypal'
            payflowClientConfiguration.account.vendor == 'External'
            payflowClientConfiguration.account.username == 'foo'
            payflowClientConfiguration.account.password == 'bar'
            payflowClientConfiguration.proxyServer == null
    }

    def "Payflow configuration is parsed for just timeout, request ID strategy, environment, account and proxy server without username/password"() {
        when: "configuration is read"
            PayflowClientConfiguration payflowClientConfiguration = payflowConfigurationReader.parseConfig('development', 'GroovyPayflowConfig6')

        then: "values are correct"
            payflowClientConfiguration.timeout == 56
            payflowClientConfiguration.requestIdStrategy instanceof CurrentTimestampPayflowRequestIdStrategy
            payflowClientConfiguration.server == PayflowServer.LIVE
            payflowClientConfiguration.account != null
            payflowClientConfiguration.account.partner == 'Paypal'
            payflowClientConfiguration.account.vendor == 'External'
            payflowClientConfiguration.account.username == 'foo'
            payflowClientConfiguration.account.password == 'bar'
            payflowClientConfiguration.proxyServer != null
            payflowClientConfiguration.proxyServer.address == 'internal.server'
            payflowClientConfiguration.proxyServer.port == 9999
    }

    def "Payflow configuration is parsed for just timeout, request ID strategy, environment, account and proxy server with username/password"() {
        when: "configuration is read"
            PayflowClientConfiguration payflowClientConfiguration = payflowConfigurationReader.parseConfig('development', 'GroovyPayflowConfig7')

        then: "values are correct"
            payflowClientConfiguration.timeout == 56
            payflowClientConfiguration.requestIdStrategy instanceof UUIDPayflowRequestIdStrategy
            payflowClientConfiguration.server == PayflowServer.LIVE
            payflowClientConfiguration.account != null
            payflowClientConfiguration.account.partner == 'Paypal'
            payflowClientConfiguration.account.vendor == 'External'
            payflowClientConfiguration.account.username == 'foo'
            payflowClientConfiguration.account.password == 'bar'
            payflowClientConfiguration.proxyServer != null
            payflowClientConfiguration.proxyServer.address == 'internal.server'
            payflowClientConfiguration.proxyServer.port == 9999
            payflowClientConfiguration.proxyServer.logonId == 'proxyLogon'
            payflowClientConfiguration.proxyServer.password == 's3cr3t'
    }

    private class CurrentTimestampPayflowRequestIdStrategy implements PayflowRequestIdStrategy {
        @Override
        String getRequestId() {
            new Date().time.toString()
        }
    }
}
