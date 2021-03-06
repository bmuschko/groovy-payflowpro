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

/**
 * Payflow medium verbosity response.
 *
 * @author Benjamin Muschko
 */
class PayflowVerbosityResponse {
    String hostCode
    String respText
    String procAvs
    String procCvv2
    String procCardSecure
    String addLmnsgs
    Integer transState
    Date dateToSettle
    Integer batchId
    Date settleDate
    String amexId
    String amexPosData
    String visaCardLevel

    @Override
    String toString() {
        "PayflowVerbosityResponse{" +
        "hostCode='" + hostCode + '\'' +
        ", respText='" + respText + '\'' +
        ", procAvs='" + procAvs + '\'' +
        ", procCvv2='" + procCvv2 + '\'' +
        ", procCardSecure='" + procCardSecure + '\'' +
        ", addLmnsgs='" + addLmnsgs + '\'' +
        ", transState=" + transState +
        ", dateToSettle=" + dateToSettle +
        ", batchId=" + batchId +
        ", settleDate=" + settleDate +
        ", amexId='" + amexId + '\'' +
        ", amexPosData='" + amexPosData + '\'' +
        ", visaCardLevel='" + visaCardLevel + '\'' +
        '}'
    }
}
