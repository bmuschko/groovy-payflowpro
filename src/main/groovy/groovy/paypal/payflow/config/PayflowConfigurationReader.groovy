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
import groovy.paypal.payflow.PayflowServer
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
    def parseConfig(String environment, String scriptClassName) {
        Class scriptClass = getScriptClass(scriptClassName)

        if(!scriptClass) {
            return null
        }

        PayflowClientConfiguration configurationHolder = new PayflowClientConfiguration()
        ConfigObject config = new ConfigSlurper(environment).parse(scriptClass)
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
            log.warn "Payflow configuration script class with name '$scriptClassName' not found."
        }
    }

    private void parseClient(ConfigObject config, PayflowClientConfiguration configuration) {
        if(config.payflowClient.containsKey('timeout')) {
            configuration.timeout = config.payflowClient.timeout
        }

        if(config.payflowClient.containsKey('requestIdStrategy')) {
            def requestIdStrategyClass = config.payflowClient.requestIdStrategy as Class
            configuration.requestIdStrategy = requestIdStrategyClass.newInstance()
        }

        if(config.payflowClient.containsKey('server')) {
            configuration.server = PayflowServer.valueOf(config.payflowClient.server.toUpperCase())
        }
    }

    private void parseAccount(ConfigObject config, PayflowClientConfiguration configuration) {
        if(config.payflowClient.containsKey('account')) {
            PayflowAccount account = new PayflowAccount()
            account.partner = config.payflowClient.account.partner
            account.vendor = config.payflowClient.account.vendor
            account.username = config.payflowClient.account.username
            account.password = config.payflowClient.account.password
            configuration.account = account
        }
    }

    private void parseProxyServer(ConfigObject config, PayflowClientConfiguration configuration) {
        if(config.payflowClient.containsKey('proxyServer')) {
            PayflowProxyServer proxyServer = new PayflowProxyServer()
            proxyServer.address = config.payflowClient.proxyServer.address
            proxyServer.port = config.payflowClient.proxyServer.port

            if(config.payflowClient.proxyServer.containsKey('logonId')) {
                proxyServer.logonId = config.payflowClient.proxyServer.logonId
                proxyServer.password = config.payflowClient.proxyServer.password
            }

            configuration.proxyServer = proxyServer
        }
    }
}
