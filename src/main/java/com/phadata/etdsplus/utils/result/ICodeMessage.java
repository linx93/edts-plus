package com.phadata.etdsplus.utils.result;

/**
 * @Author linx
 * @Classname ICodeMessage
 * @Description 封装API的错误码
 * @Date 2019/12/24 08:57
 */

public interface ICodeMessage {
    /**
     * 获取code
     * @return
     */
    String getCode();

    /**
     * 获取msg
     * @return
     */
    String getMessage();
}
