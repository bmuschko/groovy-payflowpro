environments {
    development {
        payflowClient {
            timeout = 56
            requestIdStrategy = 'groovy.paypal.payflow.config.PayflowConfigurationReaderTest\$CurrentTimestampPayflowRequestIdStrategy'
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
            }
        }
    }
}