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

import groovy.paypal.payflow.response.PayflowResponseMap
import spock.lang.FailsWith
import spock.lang.Specification

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/28/11
 * Time: 9:44 AM
 * To change this template use File | Settings | File Templates.
 */
class GroovyPayflowClientTest extends Specification {
    GroovyPayflowClient client

    def setup() {
        client = new GroovyPayflowClient()
    }

    def cleanup() {
        client = null
    }

    def "Create client"() {
        when: "the Payflow client is created"
            client.withTest()

        then: "the HTTPS sender should be set up the environment should be set to test"
            client.httpsSender != null
            client.httpsSender.timeout == 45
            client.httpsSender.payflowRequestIdStrategy != null
            client.httpsSender.payflowRequestIdStrategy instanceof UUIDPayflowRequestIdStrategy
            client.payflowEnvironment == PayflowEnvironment.TEST
    }

    def "Set Payflow test environment"() {
        when: "the Payflow environment is set to test"
            client.withTest()

        then: "the environment should be test"
            client.payflowEnvironment == PayflowEnvironment.TEST
    }

    def "Set Payflow live environment"() {
        when: "the Payflow environment is set to live"
            client.withLive()

        then: "the environment should be live"
            client.payflowEnvironment == PayflowEnvironment.LIVE
    }

    def "Use account information"() {
        given: "an account"
            PayflowAccount account = createAccount()

        when: "the account information is set"
            client.useAccount(account)

        then: "the client should use the information as parameters"
            client.accountParams.size() == 4
            client.accountParams.PARTNER == 'partner'
            client.accountParams.VENDOR == 'vendor'
            client.accountParams.USER == 'username'
            client.accountParams.PWD == 'password'
    }

    def "Submit sale with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'S']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "sale is submitted"
            PayflowResponseMap response = client.submitSale(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit sale with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'S']
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "sale is submitted"
            PayflowResponseMap response = client.submitSaleWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit authorization with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'A']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "authorization is submitted"
            PayflowResponseMap response = client.submitAuthorization(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit authorization with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'A']
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "authorization is submitted"
            PayflowResponseMap response = client.submitAuthorizationWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit delayed capture with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'D']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "delayed capture is submitted"
            PayflowResponseMap response = client.submitDelayedCapture(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit delayed capture with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'D']
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "delayed capture is submitted"
            PayflowResponseMap response = client.submitDelayedCaptureWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit voice authorization with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'F']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "voice authorization is submitted"
            PayflowResponseMap response = client.submitVoiceAuthorization(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit voice authorization with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'F']
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "voice authorization is submitted"
            PayflowResponseMap response = client.submitVoiceAuthorizationWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit credit with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'C']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "credit is submitted"
            PayflowResponseMap response = client.submitCredit(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit credit with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'C']
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "credit is submitted"
            PayflowResponseMap response = client.submitCreditWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit void with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'V']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "void is submitted"
            PayflowResponseMap response = client.submitVoid(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit void with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'V']
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "void is submitted"
            PayflowResponseMap response = client.submitVoidWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit inquiry with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'I']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "inquiry is submitted"
            PayflowResponseMap response = client.submitInquiry(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit inquiry with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'I']
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "inquiry is submitted"
            PayflowResponseMap response = client.submitInquiryWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit reference transaction with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TRXTYPE': 'A', 'TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = [:]
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "reference transaction is submitted"
            PayflowResponseMap response = client.submitReferenceTransaction(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Submit reference transaction with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = [:]
            def extra = ['TRXTYPE': 'A', 'TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "reference transaction is submitted"
            PayflowResponseMap response = client.submitReferenceTransactionWithTrxtypeAndTenderAndAcctAndExpdateAndAmt('A', 'C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Add profile with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'R', 'ACTION': 'A']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "profile is added"
            PayflowResponseMap response = client.addProfile(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Add profile with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'R', 'ACTION': 'A']
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "profile is added"
            PayflowResponseMap response = client.addProfileWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Modify profile with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'R', 'ACTION': 'M']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "profile is modified"
            PayflowResponseMap response = client.modifyProfile(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Modify profile with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'R', 'ACTION': 'M']
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "profile is modified"
            PayflowResponseMap response = client.modifyProfileWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Reactivate profile with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'R', 'ACTION': 'R']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "profile is reactivated"
            PayflowResponseMap response = client.reactivateProfile(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Reactivate profile with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'R', 'ACTION': 'R']
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "profile is reactivated"
            PayflowResponseMap response = client.reactivateProfileWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Cancel profile with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'R', 'ACTION': 'C']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "profile is canceled"
            PayflowResponseMap response = client.cancelProfile(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Cancel profile with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'R', 'ACTION': 'C']
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "profile is canceled"
            PayflowResponseMap response = client.cancelProfileWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Send profile inquiry with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'R', 'ACTION': 'I']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "profile inquiry is sent"
            PayflowResponseMap response = client.sendProfileInquiry(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Send profile inquiry with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'R', 'ACTION': 'I']
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "profile inquiry is sent"
            PayflowResponseMap response = client.sendProfileInquiryWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Retry profile payment with Map parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "extra parameters"
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'R', 'ACTION': 'P']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "profile payment is retried"
            PayflowResponseMap response = client.retryProfilePayment(extra)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    def "Retry profile payment with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = ['TRXTYPE': 'R', 'ACTION': 'P']
            def extra = ['TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "profile payment is retried"
            PayflowResponseMap response = client.retryProfilePaymentWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "the client should use the information as parameters and respond successfully"
            response.size() == 1
            response.RESULT == '0'
    }

    @FailsWith(IllegalArgumentException)
    def "Submit unknown transaction with dynamic parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        and: "Payflow sends approved response"
            def params = [:]
            def extra = ['TRXTYPE': 'A', 'TENDER': 'C', 'ACCT': '4111111111111111', 'EXPDATE': '0114', 'AMT': '14.00']
            addAllParams(params, extra, createAccountMap())
            HttpsSender mockHttpsSender = Mock()
            client.httpsSender = mockHttpsSender
            mockHttpsSender.sendPost(PayflowEnvironment.TEST.url, params) >> ['RESULT': '0']

        when: "the account information is set"
            client.useAccount(account)

        and: "unknown transaction is submitted"
            client.submitUnknownWithTenderAndAcctAndExpdateAndAmt('C', '4111111111111111', '0114', 14.00)

        then: "an exception is thrown"
    }

    @FailsWith(IllegalArgumentException)
    def "Submit transaction with dynamic parameters but less arguments than parameters"() {
        given: "an account"
            PayflowAccount account = createAccount()

        when: "the account information is set"
            client.useAccount(account)

        and: "unknown transaction is submitted"
            client.submitReferenceTransactionWithTenderAndAcct('C', '4111111111111111', '0114')

        then: "an exception is thrown"
    }

    @FailsWith(IllegalArgumentException)
    def "Submit transaction with dynamic parameters but less parameters than arguments"() {
        given: "an account"
            PayflowAccount account = createAccount()

        when: "the account information is set"
            client.useAccount(account)

        and: "unknown transaction is submitted"
            client.submitReferenceTransactionWithTenderAndAcct('C')

        then: "an exception is thrown"
    }

    private PayflowAccount createAccount() {
        new PayflowAccount(partner: 'partner', vendor: 'vendor', username: 'username', password: 'password')
    }

    private Map createAccountMap() {
        ['PARTNER': 'partner', 'VENDOR': 'vendor', 'USER': 'username', 'PWD': 'password']
    }

    private void addAllParams(Map params, Map extra, Map account) {
        params.putAll(extra)
        params.putAll(account)
    }
}
