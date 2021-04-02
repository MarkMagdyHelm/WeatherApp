package com.egabi.core.di;


import org.jetbrains.annotations.Nullable;

public class UploadEvent {

    private static final UploadEvent INSTANCE = new UploadEvent();

    private UploadEvent() {
    }

    public static UploadEvent instance() {
        return INSTANCE;
    }

    @Nullable
    public static UploadEvent instance(int toInt) {
        return null;
    }
}

