package com.mokelock.houseleasing.services;

public interface TractService2 {
    /**
     *用户二次签名确认交易
     * @param _username 用户名
     * @param _password 密码
     * @param _ownername 房主的用户名
     * @param _requestIdentify 请求反馈 true 同意确认结束 false 不同意确认结束
     */
    public void postUserIden(String _username,String _password,String _ownername,boolean _requestIdentify);

    /**
     * 向后台打印卖家是否确认交易
     * @param _username 这个交易的房客的用户名
     * @param _requestIdentify true签约成功完成 false签约失败结束
     */
    public void postOwnerIden(String _username,boolean _requestIdentify);

    /**
     * 支付密码的输入
     * @param _payPass 支付密码
     */
    public void postPayPass(String _payPass);
}

