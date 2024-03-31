package cn.micro.trace.service;

import java.util.List;
import java.util.Map;

public interface TraceService {

    /**
     * 跟踪请求
     *
     * @param url           请求url地址
     * @param method        请求方式
     * @param headers       请求头
     * @param parameters    请求参数
     * @param reqBody       请求体
     * @param rspStatus     返回状态
     * @param rspBody       返回体
     * @param serviceMethod 请求服务方法
     * @param cost          耗时，单位纳秒
     */
    void request(String url
            , String method
            , Map<String, List<String>> headers
            , Map<String, List<String>> parameters
            , byte[] reqBody
            , int rspStatus
            , byte[] rspBody
            , String serviceMethod
            , long cost);

}
