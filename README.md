# Groovy client for PayPal Payflow Pro API

This library provides a low-level interface and DSL to the PayPal Payflow Pro API (HTTPS interface). This library
is not a replacement for the [Java SDK](https://cms.paypal.com/us/cgi-bin/?cmd=_render-content&content_ID=developer/library_download_sdks#PayflowPro).
It aims for giving you full control over all request and response parameters but also provides great flexiblity on how to
express your transaction in a natural language. The required HTTPS communication with the Payflow API is hidden but configurable.

__Supported Transactions__

* Sale
* Authorization
* Voice Authorization
* Delayed Capture
* Credit
* Void
* Inquiry

__Recurring Billing__

* Add Profile
* Modify Profile
* Reactivate Profile
* Cancel Profile
* Profile Inquiry
* Retrying Profile Payment

## Usage

You can use the API by creating an instance of the class `groovy.paypal.payflow.GroovyPayflowClient`. The implementation
provides support for configuring the Payflow client. This can either be done by calling methods on the instance or by
using a configuration file.
<br>
### Programmatical configuration

If required you can set a new timeout for the HTTPS communication and the strategy for providing the request ID directly on the fields.
The default timeout is 45 seconds, the default request ID generation strategy is a random UUID. The
request ID generation strategy requires you to implement the interface `groovy.paypal.payflow.PayflowRequestIdStrategy`.

    // Uses a timeout of 45 secs and a random UUID for request ID
    GroovyPayflowClient client = new GroovyPayflowClient()

    class CustomPayflowRequestIdStrategy implements PayflowRequestIdStrategy {
        @Override
        String getRequestId() {
            // your own ID generation
        }
    }

    // Setting a new timeout and custom request ID directly on the fields
    client.httpsSender.timeout = 300
    client.httpsSender.requestIdStrategy = new CustomPayflowRequestIdStrategy()

By default the client uses the testing environment `pilot-payflowpro.paypal.com`. You can easily switch between testing
and live environment by invoking the methods `withTest()` and `withLive()`. If your network infrastructure requires you to use
a proxy server you can define that as well. Usually you will want to use a specific account for your transactions. You can
either preset an account or define it yourself for each transaction as request parameters. All of these operations can be chained.

    // Provide proxy server and account information
    def proxyServer = new PayflowProxyServer(address: 'internal.proxy.com', port: '54603', logonId: 'internal', password: 'secret')
    def account = new PayflowAccount(partner: 'PayPal', vendor: 'groovypayflowpro', username: 'groovypayflowpro', password: 'pwd123')

    // Operations can be chained in any kind of order
    GroovyPayflowClient client = new GroovyPayflowClient().withLive().withProxyServer(proxyServer).useAccount(account)
<br>
### Per environment configuration

The client supports the concept of environments. Based on a given configuration file you can specify the environment to
be used. By default the client searches for the file `GroovyPayflowConfig.groovy` on the root level of your classpath. If the
configuration file cannot be found the default settings are used. You can define a different configuration file sitting in any
package as well as the environment over the constructor of `groovy.paypal.payflow.GroovyPayflowClient`. If no environment
is given it defaults to `development`. You can use any kind of name for the environment as long as you create a closure for it.
There's no limitation to the number of environments you can configure. If a parameter is not defined it falls back to the default values.
The parameters `account` and `proxyServer` only get used if you specified them. As an example consider the following default
definition:

    environments {
        development {
            payflowClient {
                timeout = 56
                server = 'live'

                account {
                    partner = 'Paypal'
                    vendor = 'External'
                    username = 'foo'
                    password = 'bar'
                }

                proxyServer {
                    address = 'internal.server'
                    port = 9999
                    logonId = 'proxyLogon'
                    password = 's3cr3t'
                }
            }
        }
    }
<br>
### Submitting a transaction

There are two ways you can submit a transaction. The default way is to call the transaction method and provide the request
parameters as a `Map`. The key corresponds to the parameter name in the documentation but is case insensitive.

    // Sale transaction providing a Map
    def params = ['TENDER': 'C', 'ACCT': 4111111111111111, 'EXPDATE': '0114', 'AMT': 14.00]
    def response = client.submitSale(params)

Alternatively you can use a dynamic transaction method and provide the parameter values as a list. A dynamic transaction method
name always appends the keyword _With_. Arguments are concatinated by the keyword _And_. Each of the arguments can be defined
in upper or lower case. For a better readability you should capitalize the first letter of each argument name. The number
of arguments you can use is not limited.

    // Dynamic definition of submitting a sale transaction
    def response = client.submitSaleWithTenderAndAcctAndExpdateAndAmt('C', 4111111111111111, '0114', 14.00)
<br>
### Using the response

In both ways the response is a specialized `Map` of type `groovy.paypal.payflow.response.PayflowResponseMap`. If you'd rather
like to work with an object you can manually cast the `Map` to a response object using the `asType` operator. A response
object also provides the correct data type.

    // Using the response as Map
    if(response.approved) {
        assert response.RESPMSG == 'Approved'
        println "Transaction reference identifier: $response.PNREF"
    }

    // Casting the Map to a response object
    def payflowResponse = response as PayflowResponse

    if(payflowResponse.approved) {
        assert payflowResponse.result instanceof Integer
        assert payflowResponse.result == 0
        println "Transaction reference identifier: $payflowResponse.pnref"
    }

Depending on the transaction type and provided parameters you might get back different response fields. Please see the
[Payflow Pro Developer's Guide](https://cms.paypal.com/cms_content/US/en_US/files/developer/PP_PayflowPro_Guide.pdf)
and [Recurring Billing Service Guide for Payflow Pro](https://cms.paypal.com/cms_content/US/en_US/files/developer/PP_PayflowPro_RecurringBilling_Guide.pdf)
for more information. There are various response types you can cast to:

    groovy.paypal.payflow.response.PayflowResponse
    groovy.paypal.payflow.response.PayflowVerbosityResponse
    groovy.paypal.payflow.response.PayflowAddressVerificationResponse
    groovy.paypal.payflow.response.PayflowProfileResponse