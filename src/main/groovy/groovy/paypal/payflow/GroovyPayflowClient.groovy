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

import groovy.paypal.payflow.config.PayflowClientConfiguration
import groovy.paypal.payflow.config.PayflowConfigurationReader
import groovy.paypal.payflow.response.PayflowResponseMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Groovy client that provides an API and DSL for sumitting Payflow transactions.
 *
 * @author Benjamin Muschko
 */
class GroovyPayflowClient implements PayflowClient {
    private final static Logger log = LoggerFactory.getLogger(GroovyPayflowClient)
    static final String WITH_DELIMITER = 'With'
    static final String AND_DELIMITER = 'And'
    HttpsSender httpsSender
    PayflowServer server
    def accountParams = [:]

    GroovyPayflowClient() {
        this('development', PayflowConfigurationReader.CONFIG_CLASS)
    }

    GroovyPayflowClient(String environment) {
        this(environment, PayflowConfigurationReader.CONFIG_CLASS)
    }

    GroovyPayflowClient(String environment, String configClass) {
        configureClient(environment, configClass)
    }

    private void configureClient(String environment, String configClass) {
        PayflowClientConfiguration configuration = resolveConfiguration(environment, configClass)
        httpsSender = new PayflowHttpsSender(configuration.timeout, configuration.requestIdStrategy)
        server = configuration.server

        if(log.isDebugEnabled()) {
            log.debug "Configured Payflow server $configuration.server"
        }

        if(configuration.account) {
            useAccount(configuration.account)
        }

        if(configuration.proxyServer) {
            useProxyServer(configuration.proxyServer)
        }
    }

    private PayflowClientConfiguration resolveConfiguration(String environment, String configClass) {
        PayflowClientConfiguration configuration = new PayflowConfigurationReader().parseConfig(environment, configClass)
        configuration ?: new PayflowClientConfiguration()
    }

    GroovyPayflowClient withTest() {
        server = PayflowServer.TEST
        this
    }

    GroovyPayflowClient withLive() {
        server = PayflowServer.LIVE
        this
    }

    GroovyPayflowClient useAccount(account) {
        accountParams.put('PARTNER', account.partner)
        accountParams.put('VENDOR', account.vendor)
        accountParams.put('USER', account.username)
        accountParams.put('PWD', account.password)
        this
    }

    GroovyPayflowClient useProxyServer(PayflowProxyServer proxyServer) {
        httpsSender.proxyServer = proxyServer
        this
    }

    /**
     * Submits a Sale transaction which charges the specified amount against the account, and
     * marks the transaction for immediate fund transfer during the next settlement period.
     *
     * @param extra Extra parameters
     * @return Response
     */
    @Override
    PayflowResponseMap submitSale(Map extra = [:]) {
        sendRequestToPayflow(PayflowTransaction.SALE.type, extra)
    }

    /**
     * Submits an Authorization transaction places a hold on the cardholder’s open-to-buy
     * limit, lowering the cardholder’s limit by the amount of the transaction. It does not transfer
     * funds.
     *
     * @param extra Extra parameters
     * @return Response
     */
    @Override
    PayflowResponseMap submitAuthorization(Map extra = [:]) {
        sendRequestToPayflow(PayflowTransaction.AUTHORIZATION.type, extra)
    }

    /**
     * Submits a Delayed Capture transaction is performed after an Authorization to capture
     * the original Authorization amount. The Delayed Capture is scheduled for settlement during the
     * next settlement period.
     *
     * @param extra Extra parameters
     * @return Response
     */
    @Override
    PayflowResponseMap submitDelayedCapture(Map extra = [:]) {
        sendRequestToPayflow(PayflowTransaction.DELAYED_CAPTURE.type, extra)
    }

    /**
     * Submits Account Verification, also known as zero dollar Authorization, verifies credit
     * card information.
     *
     * @param extra Extra parameters
     * @return Response
     */
    @Override
    PayflowResponseMap submitVoiceAuthorization(Map extra = [:]) {
        sendRequestToPayflow(PayflowTransaction.VOICE_AUTHORIZATION.type, extra)
    }

    /**
     * Submits a Credit transaction that refunds the specified amount to the cardholder.
     *
     * @param extra Extra parameters
     * @return Response
     */
    @Override
    PayflowResponseMap submitCredit(Map extra = [:]) {
        sendRequestToPayflow(PayflowTransaction.CREDIT.type, extra)
    }

    /**
     * Submits a Void transaction which prevents a transaction from being settled.
     *
     * @param extra Extra parameters
     * @return Response
     */
    @Override
    PayflowResponseMap submitVoid(Map extra = [:]) {
        sendRequestToPayflow(PayflowTransaction.VOID.type, extra)
    }

    /**
     * Submits an Inquiry transaction returns the result and status of a transaction.
     *
     * @param extra Extra parameters
     * @return Response
     */
    @Override
    PayflowResponseMap submitInquiry(Map extra = [:]) {
        sendRequestToPayflow(PayflowTransaction.INQUIRY.type, extra)
    }

    /**
     * Submits a reference transaction takes the existing credit card information that is on file and reuses it.
     * If you need to recharge a credit card and you are not storing the credit card information in your
     * local database, you can perform a reference transaction.
     *
     * @param extra Extra parameters
     * @return Response
     */
    @Override
    PayflowResponseMap submitReferenceTransaction(Map extra = [:]) {
        sendRequestToPayflow(PayflowTransaction.REFERENCE_TRANSACTION.type, extra)
    }

    /**
     * Create a new profile.
     *
     * @param extra Extra parameters
     * @return Response
     */
    @Override
    PayflowResponseMap addProfile(Map extra = [:]) {
        sendRecurringProfileRequestToPayflow(PayflowRecurringBillingAction.ADD.action, extra)
    }

    /**
     * Make changes to an existing profile. If the profile is currently
     * inactive, then the Modify action reactivates it.
     *
     * @param extra Extra parameters
     * @return Response
     */
    @Override
    PayflowResponseMap modifyProfile(Map extra = [:]) {
        sendRecurringProfileRequestToPayflow(PayflowRecurringBillingAction.MODIFY.action, extra)
    }

    /**
     * Reactivate an inactive profile.
     *
     * @param extra Extra parameters
     * @return Response
     */
    PayflowResponseMap reactivateProfile(Map extra = [:]) {
        sendRecurringProfileRequestToPayflow(PayflowRecurringBillingAction.REACTIVATE.action, extra)
    }

    /**
     * Deactivate an existing profile.
     *
     * @param extra Extra parameters
     * @return Response
     */
    @Override
    PayflowResponseMap cancelProfile(Map extra = [:]) {
        sendRecurringProfileRequestToPayflow(PayflowRecurringBillingAction.CANCEL.action, extra)
    }

    /**
     * Sends an Inquiry action which enables you to view either of the following sets of data about a customer.
     *
     * @param extra Extra parameters
     * @return Response
     */
    @Override
    PayflowResponseMap sendProfileInquiry(Map extra = [:]) {
        sendRecurringProfileRequestToPayflow(PayflowRecurringBillingAction.INQUIRY.action, extra)
    }

    /**
     * Retries a previously failed payment.
     *
     * @param extra Extra parameters
     * @return Response
     */
    @Override
    PayflowResponseMap retryProfilePayment(Map extra = [:]) {
        sendRecurringProfileRequestToPayflow(PayflowRecurringBillingAction.PAYMENT.action, extra)
    }

    private PayflowResponseMap sendRequestToPayflow(String transactionType, Map extra) {
        def params = transactionType ? ['TRXTYPE': transactionType] : [:]
        params.putAll(extra)
        params.putAll(accountParams)
        new PayflowResponseMap(httpsSender.sendPost(server.url, params))
    }

    private PayflowResponseMap sendRecurringProfileRequestToPayflow(String action, Map extra) {
        def params = ['TRXTYPE': 'R', 'ACTION': action]
        params.putAll(extra)
        params.putAll(accountParams)
        new PayflowResponseMap(httpsSender.sendPost(server.url, params))
    }

    def methodMissing(String name, args) {
        if(name.startsWith(PayflowTransaction.SALE.dynamicMethodName)) {
            Map params = prepareParameters(PayflowTransaction.SALE.dynamicMethodName, name, args)
            return submitSale(params)
        }
        else if(name.startsWith(PayflowTransaction.AUTHORIZATION.dynamicMethodName)) {
            Map params = prepareParameters(PayflowTransaction.AUTHORIZATION.dynamicMethodName, name, args)
            return submitAuthorization(params)
        }
        else if(name.startsWith(PayflowTransaction.DELAYED_CAPTURE.dynamicMethodName)) {
            Map params = prepareParameters(PayflowTransaction.DELAYED_CAPTURE.dynamicMethodName, name, args)
            return submitDelayedCapture(params)
        }
        else if(name.startsWith(PayflowTransaction.VOICE_AUTHORIZATION.dynamicMethodName)) {
            Map params = prepareParameters(PayflowTransaction.VOICE_AUTHORIZATION.dynamicMethodName, name, args)
            return submitVoiceAuthorization(params)
        }
        else if(name.startsWith(PayflowTransaction.CREDIT.dynamicMethodName)) {
            Map params = prepareParameters(PayflowTransaction.CREDIT.dynamicMethodName, name, args)
            return submitCredit(params)
        }
        else if(name.startsWith(PayflowTransaction.VOID.dynamicMethodName)) {
            Map params = prepareParameters(PayflowTransaction.VOID.dynamicMethodName, name, args)
            return submitVoid(params)
        }
        else if(name.startsWith(PayflowTransaction.INQUIRY.dynamicMethodName)) {
            Map params = prepareParameters(PayflowTransaction.INQUIRY.dynamicMethodName, name, args)
            return submitInquiry(params)
        }
        else if(name.startsWith(PayflowTransaction.REFERENCE_TRANSACTION.dynamicMethodName)) {
            Map params = prepareParameters(PayflowTransaction.REFERENCE_TRANSACTION.dynamicMethodName, name, args)
            return submitReferenceTransaction(params)
        }
        else if(name.startsWith(PayflowRecurringBillingAction.ADD.dynamicMethodName)) {
            Map params = prepareParameters(PayflowRecurringBillingAction.ADD.dynamicMethodName, name, args)
            return addProfile(params)
        }
        else if(name.startsWith(PayflowRecurringBillingAction.MODIFY.dynamicMethodName)) {
            Map params = prepareParameters(PayflowRecurringBillingAction.MODIFY.dynamicMethodName, name, args)
            return modifyProfile(params)
        }
        else if(name.startsWith(PayflowRecurringBillingAction.REACTIVATE.dynamicMethodName)) {
            Map params = prepareParameters(PayflowRecurringBillingAction.REACTIVATE.dynamicMethodName, name, args)
            return reactivateProfile(params)
        }
        else if(name.startsWith(PayflowRecurringBillingAction.CANCEL.dynamicMethodName)) {
            Map params = prepareParameters(PayflowRecurringBillingAction.CANCEL.dynamicMethodName, name, args)
            return cancelProfile(params)
        }
        else if(name.startsWith(PayflowRecurringBillingAction.INQUIRY.dynamicMethodName)) {
            Map params = prepareParameters(PayflowRecurringBillingAction.INQUIRY.dynamicMethodName, name, args)
            return sendProfileInquiry(params)
        }
        else if(name.startsWith(PayflowRecurringBillingAction.PAYMENT.dynamicMethodName)) {
            Map params = prepareParameters(PayflowRecurringBillingAction.PAYMENT.dynamicMethodName, name, args)
            return retryProfilePayment(params)
        }

        int withDelimiterIndex = name.indexOf(WITH_DELIMITER)
        String transactionName = withDelimiterIndex != -1 ? name.substring(0, withDelimiterIndex) : name
        throw new IllegalArgumentException("Unknown dynamic Payflow transaction name: $transactionName")
    }

    private Map prepareParameters(String dynamicMethodName, String name, args) {
        String concatinatedParams = name.substring(dynamicMethodName.length(), name.length())
        String[] params = concatinatedParams != '' ? concatinatedParams.split(AND_DELIMITER) : new String[0]

        if(params.length < args.size()) {
            throw new IllegalArgumentException('More parameters provided than arguments')
        }
        else if(params.length > args.size()) {
            throw new IllegalArgumentException('Less parameters provided than arguments')
        }

        def paramsAndValues = [:]

        params.eachWithIndex { param, index ->
            paramsAndValues.put(param.toUpperCase(), args[index].toString())
        }

        paramsAndValues
    }
}
