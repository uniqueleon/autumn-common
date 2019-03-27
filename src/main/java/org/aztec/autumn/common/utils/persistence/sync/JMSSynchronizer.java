package org.aztec.autumn.common.utils.persistence.sync;

import javax.jms.JMSException;
import javax.persistence.PersistenceException;

import org.aztec.autumn.common.GlobalConfig;
import org.aztec.autumn.common.constant.CommonConst;
import org.aztec.autumn.common.utils.jms.DestinationType;
import org.aztec.autumn.common.utils.jms.IMessageSender;
import org.aztec.autumn.common.utils.jms.JmsProxy;
import org.aztec.autumn.common.utils.persistence.PersistenceEvent;

public class JMSSynchronizer implements PersistenceSynchronizer {

	public JMSSynchronizer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void synchronize() throws PersistenceException {
		// TODO Auto-generated method stub
		try {
			IMessageSender sender = JmsProxy.getSender(DestinationType.TOPIC.name());
			sender.sendText(CommonConst.DB_EVENT_TYPE_TOPIC, GlobalConfig.getInstance().getLocalhost() + "-" + PersistenceEvent.EventType.DB_CACHE_SYNCHORIZED.name());
		} catch (JMSException e) {
			throw new PersistenceException(e.getMessage(), e);
		}
	}

}
