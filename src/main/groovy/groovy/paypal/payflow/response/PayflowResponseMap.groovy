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
package groovy.paypal.payflow.response

import org.codehaus.groovy.runtime.DefaultGroovyMethods

/**
 * Payflow response Map. Allows for converting the values into response objects.
 *
 * @author Benjamin Muschko
 */
class PayflowResponseMap implements Map {
    @Delegate
    Map inner = [:]

    PayflowResponseMap(Map source) {
        if(source) {
            inner.putAll(source)
        }
    }

    @Override
    Object asType(Class type) {
        if(type == PayflowResponse) {
            return new PayflowResponse(pnref: inner.PNREF, ppref: inner.PPREF, result: inner.RESULT?.toInteger(), cvv2Match: inner.CVV2MATCH,
                                       respMsg: inner.RESPMSG, authCode: inner.AUTHCODE, correlationId: inner.CORRELATIONID,
                                       balamt: inner.BALAMT, cardSecure: inner.CARDSECURE)
        }
        else if(type == PayflowVerbosityResponse) {
            return new PayflowVerbosityResponse(hostCode: inner.HOSTCODE, respText: inner.RESPTEXT, procCvv2: inner.PROCCVV2,
                                                procCardSecure: inner.PROCCARDSECURE, addLmnsgs: inner.ADDLMSGS, transState: inner.TRANSSTATE?.toInteger(),
                                                dateToSettle: parseDate(inner.DATE_TO_SETTLE), batchId: inner.BATCHID?.toInteger(),
                                                settleDate: parseDate(inner.SETTLE_DATE), amexId: inner.AMEXID, amexPosData: inner.AMEXPOSDATA)
        }
        else if(type == PayflowAddressVerificationResponse) {
            return new PayflowAddressVerificationResponse(avsAddr: inner.AVSADDR, avsZip: inner.AVSZIP, iavs: inner.IAVS,
                                                          procAvs: inner.PROCAVS)
        }
        else if(type == PayflowProfileResponse) {
            return new PayflowProfileResponse(profileId: inner.PROFILEID, rpref: inner.rpref, trxPnref: inner.trxPnref,
                                              trxResult: inner.TRXRESULT?.toInteger(), trxRespmsg: inner.TRXRESPMSG)
        }

        DefaultGroovyMethods.asType(inner, type)
    }

    boolean isApproved() {
        inner.containsKey('RESULT') ? inner.RESULT.toInteger() == 0 : false
    }

    private Date parseDate(String value) {
        value ?: Date.parse('yyyy-MM-dd HH:mm:ss', value)
    }
}
