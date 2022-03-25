package com.hyh.ipc.binderpool

/**
 * FileName: ComputeImpl
 * Description: 作用描述
 * Author: haoyanhui
 * Date: 2022-03-25 15:17
 */
class ComputeImpl : ICompute.Stub() {
    override fun add(a: Int, b: Int): Int {
        return a.plus(b)
    }
}