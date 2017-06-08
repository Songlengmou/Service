// IRemoteBinder.aidl
package com.example.anotherapp;

import com.example.anotherapp.ICountChangeListener;
// Declare any non-default types here with import statements

interface IRemoteBinder {
  void setCount(int count);
  int getCount();

  void registerCountChangeListener(ICountChangeListener listener);
  void unregisterCountChangeListener(ICountChangeListener listener);
}
