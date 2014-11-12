package com.skysea.pushing;

import org.junit.Test;

public class AppTest {
    @Test
    public void testMain() throws Exception {
        App.main(new String[]{
                "skysea.com",
                "http://192.168.1.104:9090/plugins/push/packet"});
    }
}
