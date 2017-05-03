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

package com.vektorsoft.lockdown.crypto.seed;

/**
 *
 * @author Vladimir Djurovic <vdjurovic@vektorsoft.com>
 */
public enum EntropyInfo {
    
    ENTROPY_128_BITS (128, 4, 12),
    ENTROPY_160_BITS (160, 5, 15),
    ENTROPY_192_BITS (192, 6, 18),
    ENTROPY_224_BITS (224, 7, 21),
    ENTROPY_256_BITS (256, 8, 24);
    
    private final int entropyLength;
    private final int checksumLength;
    private final int wordCount;
    
    EntropyInfo(int entropyLength, int checksumLength, int wordCount) {
        this.entropyLength = entropyLength;
        this.checksumLength = checksumLength;
        this.wordCount = wordCount;
    }

    public int getEntropyLength() {
        return entropyLength;
    }

    public int getChecksumLength() {
        return checksumLength;
    }

    public int getWordCount() {
        return wordCount;
    }
    
    public int totalLength() {
        return entropyLength + checksumLength;
    }
    
}
