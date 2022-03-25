package com.hyh.ipc.binderpool

/**
 * FileName: SecurityCenterImpl
 * Description: 作用描述
 * Author: haoyanhui
 * Date: 2022-03-25 15:16
 */
class SecurityCenterImpl : ISecurityCenter.Stub() {
    override fun encrypt(content: String?): String {
        return "encrypt - $content"
    }

    override fun decrypt(password: String?): String {
        return "decrypt - $password"
    }
}