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

package com.vektorsoft.lockdown.app.init;

import com.vektorsoft.lockdown.app.LockdownConstants;
import com.vektorsoft.lockdown.app.LockdownUtil;
import com.vektorsoft.lockdown.crypto.seed.MnemonicLanguage;
import java.io.File;

/**
 * Initializes application with runtime configuration.
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public class Initializer {
    
    private static final  Initializer INSTANCE;
    
    private Initializer() {
        // private to prevent initialization
    }
    
    static {
        INSTANCE = new Initializer();
    }
    
    public static Initializer instance() {
        return INSTANCE;
    }
    
    private MnemonicLanguage mnemonicLanguage;

    
    public boolean checkFileStructure() {
        File appDir = new File(LockdownUtil.getUserHomeDirectory(), LockdownConstants.APP_DIRECTORY);
        File keyringFile = new File(appDir, LockdownConstants.KEYRING_FILE);
        
        return keyringFile.exists();
    }
    
    public boolean validateKeyring() {
        return false;
    }
    
    /**
     * Create keyring file structure.
     */
    public void createKeyring() {
        
    }

    public void setMnemonicLanguage(MnemonicLanguage mnemonicLanguage) {
        this.mnemonicLanguage = mnemonicLanguage;
    }
    
}
