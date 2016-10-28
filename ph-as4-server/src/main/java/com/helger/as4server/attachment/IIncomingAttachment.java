/**
 * Copyright (C) 2015-2016 Philip Helger (www.helger.com)
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
package com.helger.as4server.attachment;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.wss4j.common.ext.Attachment;
import org.apache.wss4j.common.util.AttachmentUtils;

import com.helger.commons.collection.attr.IAttributeContainer;
import com.helger.commons.collection.ext.CommonsHashMap;
import com.helger.commons.collection.ext.ICommonsMap;
import com.helger.commons.io.IHasInputStream;
import com.helger.commons.io.file.FilenameHelper;
import com.helger.http.CHTTPHeader;

/**
 * Base interface for a single incoming attachment.
 *
 * @author Philip Helger
 */
public interface IIncomingAttachment extends IHasInputStream, IAttributeContainer <String, String>
{
  @Nullable
  default String getContentID ()
  {
    return getAttributeAsString ("Content-ID");
  }

  @Nullable
  default String getContentTransferEncoding ()
  {
    return getAttributeAsString (CHTTPHeader.CONTENT_TRANSFER_ENCODING);
  }

  @Nullable
  default String getContentType ()
  {
    return getAttributeAsString (CHTTPHeader.CONTENT_TYPE);
  }

  @Nonnull
  default Attachment getAsWSS4JAttachment ()
  {
    final ICommonsMap <String, String> aHeaders = new CommonsHashMap<> ();
    aHeaders.put (AttachmentUtils.MIME_HEADER_CONTENT_DESCRIPTION, "Attachment");
    if (this instanceof IncomingFileAttachment)
    {
      final File aFile = ((IncomingFileAttachment) this).getFile ();
      aHeaders.put (AttachmentUtils.MIME_HEADER_CONTENT_DISPOSITION,
                    "attachment; filename=\"" + FilenameHelper.getWithoutPath (aFile) + "\"");
    }
    aHeaders.put (AttachmentUtils.MIME_HEADER_CONTENT_ID, "<attachment=" + getContentID () + ">");
    aHeaders.put (AttachmentUtils.MIME_HEADER_CONTENT_TYPE, getContentType ());

    final Attachment aAttachment = new Attachment ();
    aAttachment.setMimeType (getContentType ());
    aAttachment.addHeaders (aHeaders);
    aAttachment.setId (getContentID ());
    aAttachment.setSourceStream (getInputStream ());
    return aAttachment;
  }
}