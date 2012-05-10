package cz.vutbr.fit.xzelin15.dp.servlet;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;



public class SendJMSMessage {
	
	QueueConnection conn;
	QueueSession session;
	Queue que;

	public void setupConnection() throws JMSException, NamingException {
		Properties properties1 = new Properties();
		properties1.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		properties1.put(Context.URL_PKG_PREFIXES,
				"org.jboss.naming:org.jnp.interfaces");
		properties1.put(Context.PROVIDER_URL, "jnp://127.0.0.1:1099");
		InitialContext iniCtx = new InitialContext(properties1);

		Object tmp = iniCtx.lookup("ConnectionFactory2");
		QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
		conn = qcf.createQueueConnection();
		que = (Queue) iniCtx
				.lookup("queue/restaurant_consumer_gw");
		session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		conn.start();
		System.out.println("Connection Started");
	}

	public void stop() throws JMSException {
		conn.stop();
		session.close();
		conn.close();
	}

	public void sendAMessage(String msg) throws JMSException {

		QueueSender send = session.createSender(que);
		ObjectMessage tm = session.createObjectMessage(msg);
		send.send(tm);
		send.close();
	}
}
