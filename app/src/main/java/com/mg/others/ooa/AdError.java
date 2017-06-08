package com.mg.others.ooa;

/**
 * Created by sean on 2/17/16.
 */
public class AdError {
    public final static int ERROR_CODE_INTERNAL_ERROR = 0;
    public final static int ERROR_CODE_INVALID_REQUEST = 1;
    public final static int ERROR_CODE_NETWORK_ERROR = 2;
    public final static int ERROR_CODE_NO_FILL = 3;
    public final static int ERROR_CODE_TIMEOUT = 4;

    private final int errorCode;
    private String reason = "";
    public AdError(int errorCode) {
        this.errorCode = errorCode;
    }

    public AdError(int errorCode , String reason){
        this.errorCode = errorCode;
        this.reason = reason;
    }

    @Override
    public String toString() {
        String message;
        switch (errorCode) {
            case ERROR_CODE_INTERNAL_ERROR:
                message = "mmError TYPE = Internal Error";
                break;
            case ERROR_CODE_INVALID_REQUEST:
                message = "mmError TYPE = Invalid Request :" + reason;
                break;
            case ERROR_CODE_NETWORK_ERROR:
                message = "mmError TYPE = Network Error";
                break;
            case ERROR_CODE_NO_FILL:
                message = "mmError TYPE = Not Fill";
                break;
            case ERROR_CODE_TIMEOUT:
                message = "mmError TYPE = Load Timeout";
                break;
            default:
                message = "mmError TYPE = Unknown Error";
                break;
        }
        return message;
    }

}
