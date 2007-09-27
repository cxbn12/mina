/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.mina.transport.apr;

import org.apache.tomcat.jni.Library;
import org.apache.tomcat.jni.Pool;

/**
 * Internal singleton used for initializing corretcly the APR native library
 * and the associated memory pool.
 * 
 * @author The Apache MINA Project (dev@mina.apache.org)
 * @version $Rev$, $Date$
 */
class APRLibrary {

    // is APR library was initialized (load of native libraries)
    private static APRLibrary library = null;

    static synchronized APRLibrary getLibrary() {
        if (!isInitialized())
            initialize();
        return library;
    }

    static synchronized void initialize() {
        if (library == null)
            library = new APRLibrary();
    }

    static synchronized boolean isInitialized() {
        return library != null;
    }

    // APR memory pool (package wide mother pool)	
    private long pool = -1;

    private APRLibrary() {
        try {
            Library.initialize(null);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error loading Apache Portable Runtime (APR)", e);
        }
        pool = Pool.create(0);
    }

    protected void finalize() throws Throwable {
        //		 TODO : necessary I think, need to check APR doc
        Pool.clear(pool);
    }

    public long getPool() {
        return pool;
    }
}
