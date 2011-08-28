package groovy.paypal.payflow

import groovy.paypal.payflow.response.PayflowResponseMap
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
