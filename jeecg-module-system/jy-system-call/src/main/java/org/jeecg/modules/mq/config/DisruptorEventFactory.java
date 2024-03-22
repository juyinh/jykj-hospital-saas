package org.jeecg.modules.mq.config;

import com.lmax.disruptor.EventFactory;

/**
 * @Description: TODO
 * @author: xiaopeng.wu
 * @create: 2023/11/27 9:46
 **/
public class DisruptorEventFactory implements EventFactory<MessageModel> {
    @Override
    public MessageModel newInstance() {
        return new MessageModel();
    }
}
