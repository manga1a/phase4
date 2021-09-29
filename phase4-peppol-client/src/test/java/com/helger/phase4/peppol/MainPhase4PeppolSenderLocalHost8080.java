/**
 * Copyright (C) 2015-2021 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.phase4.peppol;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.helger.peppolid.IParticipantIdentifier;
import com.helger.phase4.dump.AS4DumpManager;
import com.helger.phase4.dump.AS4IncomingDumperFileBased;
import com.helger.phase4.dump.AS4OutgoingDumperFileBased;
import com.helger.phase4.dump.AS4RawResponseConsumerWriteToFile;
import com.helger.phase4.sender.AbstractAS4UserMessageBuilder.ESimpleUserMessageSendResult;
import com.helger.security.certificate.CertificateHelper;
import com.helger.servlet.mock.MockServletContext;
import com.helger.web.scope.mgr.WebScopeManager;
import com.helger.xml.serialize.read.DOMReader;

/**
 * Special main class with a constant receiver. This one skips the SMP lookup.
 *
 * @author Philip Helger
 */
public final class MainPhase4PeppolSenderLocalHost8080
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MainPhase4PeppolSenderLocalHost8080.class);

  public static void main (final String [] args)
  {
    WebScopeManager.onGlobalBegin (MockServletContext.create ());

    // Dump (for debugging purpose only)
    AS4DumpManager.setIncomingDumper (new AS4IncomingDumperFileBased ());
    AS4DumpManager.setOutgoingDumper (new AS4OutgoingDumperFileBased ());

    try
    {
      final Element aPayloadElement = DOMReader.readXMLDOM (new File ("src/test/resources/examples/base-example.xml"))
                                               .getDocumentElement ();
      if (aPayloadElement == null)
        throw new IllegalStateException ("Failed to read XML file to be send");

      final String strCertificate = "-----BEGIN CERTIFICATE-----\n" +
              "MIIFjTCCA3WgAwIBAgIEAe8QbDANBgkqhkiG9w0BAQwFADB3MQswCQYDVQQGEwJB\n" +
              "VTERMA8GA1UECBMIVmljdG9yaWExEjAQBgNVBAcTCU1lbGJvdXJuZTESMBAGA1UE\n" +
              "ChMJQW5vbnltb3VzMRIwEAYDVQQLEwlBbm9ueW1vdXMxGTAXBgNVBAMTEE1hbmdh\n" +
              "bGEgS29kYWdvZGEwHhcNMjEwOTE2MTEzNjEzWhcNMjMwOTE2MTEzNjEzWjB3MQsw\n" +
              "CQYDVQQGEwJBVTERMA8GA1UECBMIVmljdG9yaWExEjAQBgNVBAcTCU1lbGJvdXJu\n" +
              "ZTESMBAGA1UEChMJQW5vbnltb3VzMRIwEAYDVQQLEwlBbm9ueW1vdXMxGTAXBgNV\n" +
              "BAMTEE1hbmdhbGEgS29kYWdvZGEwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIK\n" +
              "AoICAQCeMCol/mBAXh7P6nQJoWbPl4mUrKbkXI5C1aXdKqIFkshiANXYIx/x6w38\n" +
              "khjXLgrH3cGUo3rpmkJZUmIlLNxifDczoHDNj8vMGnwm5nDsVsLK6ff8Krc+SVvV\n" +
              "C2+z39cJMQTq+blOfMPmtPZWxSDUHGIDxcFl2FWCsdhFGYzh2PlmvcYJwUDXRV7u\n" +
              "q5DIjU1oL4moHEsNw7EzqNXfBu8ZMqWr8Xen5VIMN9C/OhtrmyN6XaG43pMjwuRm\n" +
              "SFQyeqS8x1VgSWqj0t1EqBZXq/1vb7z5BZGReYoKuzjis/2c9B9uknO1DNJNtG+i\n" +
              "fbezB0u+YKXiu0gysVTOgK/sdl4bjvKaWq7aff3oZDiad8veUBRPV4F4dTNXjE0f\n" +
              "29HsKJfP0434vhjP0NQ1kgdSc0StgJ0VuOQCie1uWAH0IYxQ1Sb9j2kDSkXoGtC3\n" +
              "9fZJQkL2xP0luSd9hVXu342WozNpuADczjAA/ucOfgYdZCH5wils18PjRK0jRRNm\n" +
              "UWvvVhMfTBOv/2TXhGaxWUJMmgOxnxWsJ9rcsCLUcYFZzed1nw2APgknF0xgDAw4\n" +
              "ebvMTqL8qxGiDriE1lV4iMzoSUwejNEmhQYxAQ5uJfuOumya18twkQhCNrkgsTJv\n" +
              "whYjeQJ+UhuAZQ8DUC6SMNk3mp//Wqw/QJjbZ+vuiK9zRdiv/QIDAQABoyEwHzAd\n" +
              "BgNVHQ4EFgQUL5EQ/gFKDFSLvos/7WwvHTbX3ZMwDQYJKoZIhvcNAQEMBQADggIB\n" +
              "AD39sMZ7x0v9F2NK1Y+rEsITDpwG4VLyn0PmYrQMn/mtJ7o20ydHx3Rlx1DJNQSJ\n" +
              "cO97LFBGb3nJE4h/VzNgOSRPZ8wiUKdlAq7GWZdzQI49SM5eLn4vBwecfJ8HXaFY\n" +
              "9Kfkf5fIW1YrMQpKJX+fkQDjAtMVbJE6LXlucawrAdPMc6IWEtpQuX444ouDQHQz\n" +
              "c9pIYBPcA4EVvWw9Ld/xy5xNd9NumS4uA7dq+/mgrFPi/RhX7v/s1FPyz0W5v8zg\n" +
              "VSxkW4ysaXk+bfD7vVa1aiLXCBFVUNlkwZ0z0vqjwE6tFEYrRMl1NNlefMKGp+HM\n" +
              "OlSV1W0iTPZa6EvPDobXCh7wPAmzCV7Cp0RIR7kMYH0RG19HrMCR1QH7YhJj18iw\n" +
              "g6xEzsUBxbOBSCeUe10Kz+Y328QQfezBjSy/SbbY2FNvFd0MGnAvq7raf8yma+3m\n" +
              "39puFH6NT4GhqaKOYgvVbl9KKan3ZK6pdYcVo+t8Z9C0wqzERxnfHZtS1cIVcXnJ\n" +
              "ddlp6D9kEz3SzmBqbW8+5hKNL1/r31ru6ga5B7z+0ZvKTjUA6JLGpOfG7D/WEcGt\n" +
              "3YZNuSjCgqNsv5GEWH7rvIxQoDTm3nCJKWystXnt2hcz1k5vvlvGNGYmCWl79yaZ\n" +
              "MCyTUhkgxtj0/nkwkGPOAvSYwnrhVFqlnZ7NC9IpygL3\n" +
              "-----END CERTIFICATE-----";

      // Start configuring here
      final IParticipantIdentifier aReceiverID = Phase4PeppolSender.IF.createParticipantIdentifierWithDefaultScheme ("9915:helger");
      final ESimpleUserMessageSendResult eResult;
      eResult = Phase4PeppolSender.builder ()
                                  .documentTypeID (Phase4PeppolSender.IF.createDocumentTypeIdentifierWithDefaultScheme ("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1"))
                                  .processID (Phase4PeppolSender.IF.createProcessIdentifierWithDefaultScheme ("urn:fdc:peppol.eu:2017:poacc:billing:01:1.0"))
                                  .senderParticipantID (Phase4PeppolSender.IF.createParticipantIdentifierWithDefaultScheme ("9915:phase4-test-sender"))
                                  .receiverParticipantID (aReceiverID)
                                  .senderPartyID ("POP000306")
                                  .payload (aPayloadElement)
                                  .receiverEndpointDetails (CertificateHelper.convertStringToCertficate (strCertificate),
                                                            "http://localhost:8080/as4")
                                  .rawResponseConsumer (new AS4RawResponseConsumerWriteToFile ())
                                  .sendMessageAndCheckForReceipt ();
      LOGGER.info ("Peppol send result: " + eResult);
    }
    catch (final Exception ex)
    {
      LOGGER.error ("Error sending Peppol message via AS4", ex);
    }
    finally
    {
      WebScopeManager.onGlobalEnd ();
    }
  }
}
