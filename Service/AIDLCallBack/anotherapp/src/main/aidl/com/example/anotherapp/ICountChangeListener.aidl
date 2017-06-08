// ICountChangeListener.aidl
package com.example.anotherapp;

import com.example.anotherapp.ICountChangeListener;  //这个需要自己写
// Declare any non-default types here with import statements

interface ICountChangeListener {

    void onChange(int count);
}
