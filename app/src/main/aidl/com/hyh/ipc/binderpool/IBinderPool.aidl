// IBinderPool.aidl
package com.hyh.ipc.binderpool;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}