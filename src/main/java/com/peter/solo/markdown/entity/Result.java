package com.peter.solo.markdown.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Result {
    public static final int STATUS_FAILURE = -1;
    public static final int STATUS_SUCESS = 1;
    @NonNull
    private int status;
    @NonNull
    private String msg;
}
