/**
 * Copyright (C) 2015-2017 Philip Helger (www.helger.com)
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
package com.helger.as4.model;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;
import com.helger.commons.string.StringHelper;

/**
 * Defines the available Message Exchange Patterns (MEPs).
 *
 * @author Philip Helger
 */
public enum EMEP implements IHasID <String>
{
  /**
   * The One-Way MEP which governs the exchange of a single User Message Unit
   * unrelated to other User Messages.
   */
  ONE_WAY ("oneway", 1, "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/oneWay"),
  /**
   * The Two-Way MEP which governs the exchange of two User Message Units in
   * opposite directions, the first one to occur is labeled "request", the other
   * one "reply". In an actual instance, the "reply" must reference the
   * "request" using eb:RefToMessageId. Or referenced to as The Two-Way/Sync
   * MEP.
   */
  TWO_WAY ("twoway", 2, "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/twoWay"),
  /**
   * The Two-Way/Push-and-Push MEP composes the choreographies of two
   * One-Way/Push MEPs in opposite directions, the User Message unit of the
   * second referring to the User Message unit of the first via
   * eb:RefToMessageId.
   */
  TWO_WAY_PUSH_PUSH ("pushpush", 2, "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/pushAndPush"),
  /**
   * The Two-Way/Push-and-Pull MEP composes the choreography of a One-Way/Push
   * MEP followed by the choreography of a One-Way/Pull MEP, both initiated from
   * the same MSH (Initiator). The User Message unit in the "pulled" message
   * must refer to the previously "pushed" User Message unit.
   */
  TWO_WAY_PUSH_PULL ("pushpull", 2, "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/pushAndPull"),
  /**
   * The Two-Way/Pull-and-Push MEP composes the choreography of a One-Way/Pull
   * MEP followed by the choreography of a One-Way/Push MEP, with both MEPs
   * initiated from the same MSH. The User Message unit in the "pushed" message
   * must refer to the previously "pulled" User Message unit.
   */
  TWO_WAY_PULL_PUSH ("pullpush", 2, "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/pullAndPush");

  private final String m_sID;
  private final int m_nMsgCount;
  private final String m_sURI;

  private EMEP (@Nonnull @Nonempty final String sID,
                @Nonnegative final int nMsgCount,
                @Nonnull @Nonempty final String sURI)
  {
    m_sID = sID;
    m_nMsgCount = nMsgCount;
    m_sURI = sURI;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnegative
  public int getMessageCount ()
  {
    return m_nMsgCount;
  }

  @Nonnull
  @Nonempty
  public String getURI ()
  {
    return m_sURI;
  }

  @Nullable
  public static EMEP getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EMEP.class, sID);
  }

  @Nullable
  public static EMEP getFromURIOrNull (@Nullable final String sURI)
  {
    if (StringHelper.hasNoText (sURI))
      return null;
    return EnumHelper.findFirst (EMEP.class, x -> sURI.equals (x.getURI ()));
  }
}