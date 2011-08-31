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

import groovy.paypal.payflow.util.ManifestUtils
import groovyx.net.http.HTTPBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.Method.POST

/**
 * HTTP sender implementation that is targeted towards the Payflow API. It provides all necessary header attributes
 * and converts the request parameters and the expected format.
 *
 * @author Benjamin Muschko
 */
class PayflowHttpsSender implements HttpsSender {
    static final Logger log = LoggerFactory.getLogger(PayflowHttpsSender)
    static final String CLIENT_IDENTIFIER = 'groovy-payflowpro'
    static final String CONNECTION = 'close'
    static final String CONTENT_TYPE = 'text/namevalue'

    int timeout
    PayflowRequestIdStrategy payflowRequestIdStrategy
    PayflowProxyServer proxyServer

    PayflowHttpsSender(int timeout, PayflowRequestIdStrategy payflowRequestIdStrategy) {
        this.timeout = timeout
        this.payflowRequestIdStrategy = payflowRequestIdStrategy
    }

    @Override
    void setProxyServer(PayflowProxyServer proxyServer) {
        this.proxyServer = proxyServer
    }

    @Override
    def sendPost(String url, Map<String, String> params) {
        log.debug "Sending HTTP POST request to url $url using parameters $params"
        def httpBuilder = new HTTPBuilder(url)

        httpBuilder.request(POST) {
            requestContentType = URLENC
            headers.'X-VPS-REQUEST-ID' = payflowRequestIdStrategy.getRequestId()
            headers.'X-VPS-CLIENT-TIMEOUT' = timeout
            headers.'X-VPS-Timeout' = timeout
            headers.'X-VPS-INTEGRATION-PRODUCT' = CLIENT_IDENTIFIER

            // Use library version from manifest file
            if(ManifestUtils.version) {
                headers.'X-VPS-INTEGRATION-VERSION' = ManifestUtils.version
            }

            headers.'X-VPS-VIT-OS-NAME' = System.getProperty('os.name')
            headers.'Connection' = CONNECTION
            headers.'Content-Type' = CONTENT_TYPE

            // Set proxy server if defined
            if(proxyServer) {
                headers.'PROXYADDRESS' = proxyServer.address
                headers.'PROXYPORT' = proxyServer.port

                if(proxyServer.logonId) {
                    headers.'PROXYLOGON' = proxyServer.logonId
                    headers.'PROXYPASSWORD' = proxyServer.password
                }
            }

            log.debug "Request headers: $headers"
            body = createRequestBodyFromParams(params)

            response.success = { resp, reader ->
                return createMapFromResponse(reader.text)
            }

            response.failure = { resp, reader ->
                throw new PayflowApiException("($resp.statusLine): $reader.text")
            }
        }
    }

    private String createRequestBodyFromParams(Map<String, String> params) {
        StringBuilder body = new StringBuilder()

        params.eachWithIndex { param, index ->
            body <<= param.key.toUpperCase()
            body <<= "[${param.value.length()}]="
            body <<= param.value

            if(index < params.size() - 1) {
                body <<= '&'
            }
        }

        body.toString()
    }

    private Map createMapFromResponse(String response) {
        def map = [:]

        if(response) {
            response.split('&').each { param ->
                def nameAndValue = param.split('=')
                map[nameAndValue[0]] = nameAndValue[1]
            }
        }

        map
    }
}
