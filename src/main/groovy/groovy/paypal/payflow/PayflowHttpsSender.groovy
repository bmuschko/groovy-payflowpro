package groovy.paypal.payflow

import groovy.paypal.payflow.util.ManifestUtils
import groovyx.net.http.HTTPBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.Method.POST

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/20/11
 * Time: 7:39 AM
 * To change this template use File | Settings | File Templates.
 */
class PayflowHttpsSender implements HttpsSender {
    static final Logger log = LoggerFactory.getLogger(PayflowHttpsSender)
    static final String CLIENT_IDENTIFIER = 'groovy-payflowpro'
    static final String CONNECTION = 'close'
    static final String CONTENT_TYPE = 'text/namevalue'

    int timeout
    PayflowRequestIdStrategy payflowRequestIdStrategy

    PayflowHttpsSender(int timeout, PayflowRequestIdStrategy payflowRequestIdStrategy) {
        this.timeout = timeout
        this.payflowRequestIdStrategy = payflowRequestIdStrategy
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

            if(ManifestUtils.version) {
                headers.'X-VPS-INTEGRATION-VERSION' = ManifestUtils.version
            }

            headers.'X-VPS-VIT-OS-NAME' = System.getProperty('os.name')
            headers.'Connection' = CONNECTION
            headers.'Content-Type' = CONTENT_TYPE

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
