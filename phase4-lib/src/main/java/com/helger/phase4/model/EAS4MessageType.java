/**
 * Copyright (C) 2015-2020 Philip Helger (www.helger.com)
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
package com.helger.phase4.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

/**
 * Defines the basic message types of AS4 that can effectively be send or
 * receipt.
 *
 * @author Philip Helger
 * @since 0.12.0
 */
public enum EAS4MessageType implements IHasID <String>
{
  USER_MESSAGE ("user"),
  PULL_REQUEST ("pr"),
  ERROR_MESSAGE ("errormsg"),
  RECEIPT ("receipt");

  private final String m_sID;

  EAS4MessageType (@Nonnull @Nonempty final String sID)
  {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  /**
   * @return <code>true</code> if this message type must be wrapped in a signal
   *         message or not.
   */
  public boolean isSignalMessage ()
  {
    return this == PULL_REQUEST || this == ERROR_MESSAGE || this == RECEIPT;
  }

  @Nullable
  public static EAS4MessageType getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EAS4MessageType.class, sID);
  }
}