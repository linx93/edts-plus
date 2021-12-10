package com.phadata.etdsplus.service;


/**
 * 数字身份相关服务
 *
 * @author linx
 * @since 2021-11-15
 */
public interface DTIDComponent {
    /**
     * 解析数字身份获取企业功能名称
     *
     * @param dtid
     * @return
     * @throws Exception
     */
    String getCompanyNameByDtid(String dtid) throws Exception;

}
