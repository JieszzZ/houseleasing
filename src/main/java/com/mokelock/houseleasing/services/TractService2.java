package com.mokelock.houseleasing.services;

public interface TractService2 {
    /**
     * 用户二次签名交易
     * @param _i
     * @param _ethFile
     * @param _userAddress
     * @param _ethPassword
     * @param _ownerAddress
     */
    public void postUserIden(int _i, String _ethFile, String _userAddress, String _ethPassword,String _ownerAddress);

    /**
     * 卖家二次签名交易
     * @param _i
     * @param _ethFile
     * @param _userAddress
     * @param _ethPassword
     * @param _ownerAddress
     */
    public void postOwnerIden(int _i, String _ethFile, String _userAddress, String _ethPassword,String _ownerAddress);

    /**
     * 支付密码的输入
     * @param _payPass 支付密码
     */
    public void postPayPass(String _payPass);
}

