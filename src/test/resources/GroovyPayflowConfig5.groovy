client {
    timeout = 56
    requestIdStrategy = 'groovy.paypal.payflow.config.PayflowConfigurationReaderTest\$CurrentTimestampPayflowRequestIdStrategy'
    environment = 'live'

    account {
        partner = 'Paypal'
        vendor = 'External'
        username = 'foo'
        password = 'bar'
    }
}