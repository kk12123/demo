package com.seckill.common.dto;

public class CommonResult  {
    private String responseCode = "0000";

    private String responseMsg;

    private boolean success = true;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getRepsonseMsg() {
        return responseMsg;
    }

    public void setRepsonseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public boolean isSuccess(){
        if(!"0000".equals(responseCode)){
            return false;
        }
        return success;
    }

    public void fail(String responseCode,String responseMsg){
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
        this.success = false;
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", success=" + success +
                '}';
    }
}
