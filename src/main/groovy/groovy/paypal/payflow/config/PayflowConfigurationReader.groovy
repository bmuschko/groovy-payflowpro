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

import groovy.paypal.payflow.PayflowAccount
import groovy.paypal.payflow.PayflowEnvironment
import groovy.paypal.payflow.PayflowProxyServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Payflow configuration reader.
 *
 * @author Benjamin Muschko
 */
class PayflowConfigurationReader implements ConfigurationReader {
    private final static Logger log = LoggerFactory.getLogger(PayflowConfigurationReader)
    static final String CONFIG_CLASS = 'GroovyPayflowConfig'

    @Override
    def parseConfig(String scriptClassName) {
        Class scriptClass = getScriptClass(scriptClassName)

        if(!scriptClass) {
            return null
        }

        PayflowClientConfiguration configurationHolder = new PayflowClientConfiguration()
        ConfigObject config = new ConfigSlurper().parse(scriptClass)
        parseClient(config, configurationHolder)
        parseAccount(config, configurationHolder)
        parseProxyServer(config, configurationHolder)
        configurationHolder
    }

    private Class getScriptClass(String scriptClassName) {
        try {
            return getClass().classLoader.loadClass(scriptClassName)
        }
        catch(ClassNotFoundException e) {
            log.debug "Payflow configuration script class with name '$scriptClassName' not found."
        }
    }

    private void parseClient(ConfigObject config, PayflowClientConfiguration configuration) {
        if(config.client.containsKey('timeout')) {
            configuration.timeout = config.client.timeout
        }

        if(config.client.containsKey('requestIdStrategy')) {
            def requestIdStrategyClass = config.client.requestIdStrategy as Class
            configuration.requestIdStrategy = requestIdStrategyClass.newInstance()
        }

        if(config.client.containsKey('environment')) {
            configuration.environment = PayflowEnvironment.valueOf(config.client.environment.toUpperCase())
        }
    }

    private void parseAccount(ConfigObject config, PayflowClientConfiguration configuration) {
        if(config.client.containsKey('account')) {
            PayflowAccount account = new PayflowAccount()
            account.partner = config.client.account.partner
            account.vendor = config.client.account.vendor
            account.username = config.client.account.username
            account.password = config.client.account.password
            configuration.account = account
        }
    }

    private void parseProxyServer(ConfigObject config, PayflowClientConfiguration configuration) {
        if(config.client.containsKey('proxyServer')) {
            PayflowProxyServer proxyServer = new PayflowProxyServer()
            proxyServer.address = config.client.proxyServer.address
            proxyServer.port = config.client.proxyServer.port

            if(config.client.proxyServer.containsKey('logonId')) {
                proxyServer.logonId = config.client.proxyServer.logonId
                proxyServer.password = config.client.proxyServer.password
            }

            configuration.proxyServer = proxyServer
        }
    }
}
