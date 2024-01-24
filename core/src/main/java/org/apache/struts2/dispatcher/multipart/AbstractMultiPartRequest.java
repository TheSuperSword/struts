/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.struts2.dispatcher.multipart;

import com.opensymphony.xwork2.LocaleProviderFactory;
import com.opensymphony.xwork2.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.dispatcher.LocalizedMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Abstract class with some helper methods, it should be used
 * when starting development of another implementation of {@link MultiPartRequest}
 */
public abstract class AbstractMultiPartRequest implements MultiPartRequest {

    protected static final String STRUTS_MESSAGES_UPLOAD_ERROR_PARAMETER_TOO_LONG_KEY = "struts.messages.upload.error.parameter.too.long";

    private static final Logger LOG = LogManager.getLogger(AbstractMultiPartRequest.class);

    /**
     * Defines the internal buffer size used during streaming operations.
     */
    public static final int BUFFER_SIZE = 10240;

    /**
     * Internal list of raised errors to be passed to the the Struts2 framework.
     */
    protected List<LocalizedMessage> errors = new ArrayList<>();

    /**
     * Specifies the maximum size of the entire request.
     */
    protected Long maxSize;

    /**
     * Specifies the maximum number of files in one request.
     */
    protected Long maxFiles;

    /**
     * Specifies the maximum length of a string parameter in a multipart request.
     */
    protected Long maxStringLength;

    /**
     * Specifies the maximum size per file in the request.
     */
    protected Long maxFileSize;

    /**
     * Specifies the buffer size to use during streaming.
     */
    protected int bufferSize = BUFFER_SIZE;

    protected String defaultEncoding;

    /**
     * Localization to be used regarding errors.
     */
    protected Locale defaultLocale = Locale.ENGLISH;

    /**
     * @param bufferSize Sets the buffer size to be used.
     */
    @Inject(value = StrutsConstants.STRUTS_MULTIPART_BUFFERSIZE, required = false)
    public void setBufferSize(String bufferSize) {
        this.bufferSize = Integer.parseInt(bufferSize);
    }

    @Inject(StrutsConstants.STRUTS_I18N_ENCODING)
    public void setDefaultEncoding(String enc) {
        this.defaultEncoding = enc;
    }

    /**
     * @param maxSize Injects the Struts multipart request maximum size.
     */
    @Inject(StrutsConstants.STRUTS_MULTIPART_MAXSIZE)
    public void setMaxSize(String maxSize) {
        this.maxSize = Long.parseLong(maxSize);
    }

    @Inject(StrutsConstants.STRUTS_MULTIPART_MAXFILES)
    public void setMaxFiles(String maxFiles) {
        this.maxFiles = Long.parseLong(maxFiles);
    }

    @Inject(value = StrutsConstants.STRUTS_MULTIPART_MAXFILESIZE, required = false)
    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = Long.parseLong(maxFileSize);
    }

    @Inject(StrutsConstants.STRUTS_MULTIPART_MAX_STRING_LENGTH)
    public void setMaxStringLength(String maxStringLength) {
        this.maxStringLength = Long.parseLong(maxStringLength);
    }

    @Inject
    public void setLocaleProviderFactory(LocaleProviderFactory localeProviderFactory) {
        defaultLocale = localeProviderFactory.createLocaleProvider().getLocale();
    }

    /**
     * @param request Inspect the servlet request and set the locale if one wasn't provided by
     *                the Struts2 framework.
     */
    protected void setLocale(HttpServletRequest request) {
        if (defaultLocale == null) {
            defaultLocale = request.getLocale();
        }
    }

    /**
     * Build error message.
     *
     * @param e    the Throwable/Exception
     * @param args arguments
     * @return error message
     */
    protected LocalizedMessage buildErrorMessage(Throwable e, Object[] args) {
        String errorKey = "struts.messages.upload.error." + e.getClass().getSimpleName();
        LOG.debug("Preparing error message for key: [{}]", errorKey);

        return new LocalizedMessage(this.getClass(), errorKey, e.getMessage(), args);
    }

    /* (non-Javadoc)
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getErrors()
     */
    public List<LocalizedMessage> getErrors() {
        return errors;
    }

    /**
     * @param originalFileName file name
     * @return the canonical name based on the supplied filename
     */
    protected String getCanonicalName(final String originalFileName) {
        String fileName = originalFileName;

        int forwardSlash = fileName.lastIndexOf('/');
        int backwardSlash = fileName.lastIndexOf('\\');
        if (forwardSlash != -1 && forwardSlash > backwardSlash) {
            fileName = fileName.substring(forwardSlash + 1);
        } else {
            fileName = fileName.substring(backwardSlash + 1);
        }
        return fileName;
    }

    protected String sanitizeNewlines(String before) {
        return before.replaceAll("\\R", "_");
    }

}
