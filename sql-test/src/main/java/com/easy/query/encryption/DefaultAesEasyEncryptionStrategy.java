package com.easy.query.encryption;

import com.easy.query.core.encryption.AbstractAesEasyEncryptionStrategy;

/**
 * create time 2023/3/25 09:40
 * 文件说明
 *
 * @author xuejiaming
 */
public class DefaultAesEasyEncryptionStrategy extends AbstractAesEasyEncryptionStrategy {
    @Override
    public String getIv() {
        return "A-16-Byte-String";
    }

    @Override
    public String getKey() {
        return "abcdef1234567890";
    }

}
