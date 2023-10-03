package com.lamp.mallchat.common.wx;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author qyxmzg
 * @date 2023/9/25 23:30
 * @description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class WxTest {
    @Autowired
    WxMpService wxMpService;

    @Test
    public void test() throws WxErrorException {
        WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(1, 1000);
        String url = ticket.getUrl();
        System.out.println(url);
    }



}
