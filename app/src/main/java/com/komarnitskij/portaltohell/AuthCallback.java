package com.komarnitskij.portaltohell;

import android.content.Context;

public interface AuthCallback {
    void AuthRequest(String response, Context context);
}
