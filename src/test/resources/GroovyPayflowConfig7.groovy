client {
    timeout = 56
    environment = 'live'

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