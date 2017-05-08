/*
 * Copyright (C) 2017 Vektorsoft Ltd. (http://www.vektorsoft.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.vektorsoft.lockdown.crypto;

import java.security.Provider;

/**
 * Utility class which performs initialization of cryptography providers.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class LockdownCrypto {

    private static final LockdownCrypto INSTANCE;
    
    static {
        INSTANCE = new LockdownCrypto();
    }
    
    private boolean initialized;
    private Provider provider;
    
    private LockdownCrypto() {
        this.initialized = false;
    }
    
    public static LockdownCrypto instance() {
        return INSTANCE;
    }
    
    private void checkInitialized() {
        if(!initialized) {
            throw new IllegalStateException("Lockdown cryptography not initialized");
        }
    }
    
    public void initialize(Provider defaultProvider) {
        this.provider = defaultProvider;
        this.initialized = true;
    }

    public Provider getProvider() {
        checkInitialized();
        return provider;
    }
    
}
