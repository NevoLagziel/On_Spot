package com.example.onspot.interfaces;

import com.example.onspot.Models.Method;

public interface MethodCallback {

    void onMethodFound(Method method);

    void onMethodNotFound();
}
