package com.phadata.etdsplus.mq;

/**
 * @description: 消息消费的类型，标记消费到哪张表
 * @author: linx
 * @create: 2021-11-17 11:17
 */
public enum MessageConsumerEnum {
    //说明 re:request(请求方)
    //gr:grant(授权方)
    //pr:provide(数据提供方)
    //tj:统计的意思
    re_etds_to_re_tdaas_apply(1, "请求通知"),
    re_etds_to_gr_tdaas_apply(2, "请求授权"),
    gr_tdaas_to_re_tdaas(3, "授权通知"),
    gr_tdaas_to_re_etds(4, "授权通知"),
    gr_tdaas_to_pr_tdaas(5, "授权通知"),
    gr_tdaas_to_pr_etds(6, "授权通知"),
    re_etds_to_re_tdaas_data(7, "拉取数据通知"),
    re_etds_to_pr_tdaas_data(8, "拉取数据通知"),
    re_etds_to_pr_etds_data(9, "拉取数据"),
    pr_etds_to_re_tdaas_tj(10, "拉取统计数据日志"),
    pr_etds_to_re_etds_data(11, "返回数据"),
    pr_etds_to_gr_tdaas_tj(12, "拉取统计数据日志"),
    pr_etds_to_pr_tdaas_tj(13, "拉取统计数据日志");


    private final int code;
    private final String remark;

    MessageConsumerEnum(int code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public int getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }
}
