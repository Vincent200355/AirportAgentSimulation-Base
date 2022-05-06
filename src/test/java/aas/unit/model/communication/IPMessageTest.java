package aas.unit.model.communication;

import org.junit.Test;
import aas.model.communication.network.internet.IPMessage;

public class IPMessageTest {
	
	@Test
	public void testIPMessage() {
		IPMessage message = new IPMessage(0L, 0, 1, "bob", "test");
		assert message != null;
	}
	
	@Test
	public void testGetCaption() {
		String caption = "test";
		IPMessage message = new IPMessage(0L, 0, 1, "bob", caption);
		assert message.getCaption().compareTo(caption) == 0;
	}
	
	@Test
	public void testAddData() {
		IPMessage message = new IPMessage(0L, 0, 1, "bob", "test");
		message.addData("data1", "hello");
		assert message.getData("data1").compareTo("hello") == 0;
		assert message.getData("data2") == null;
	}
	
	@Test
	public void testGetReceiverHostName() {
		IPMessage message = new IPMessage(0L, 0, 1, "bob", "test");
		assert message.getReceiverHostName().compareTo("bob") == 0;
	}
	
	@Test
	public void testCopy() {
		IPMessage message = new IPMessage(0L, 0, 1, "bob", "test");
		IPMessage message2 = message.copy(3, 4);
		assert message2.getSender() == 0;
		assert message2.getReceiver() == 4;
		assert message2.getCaption().compareTo(message.getCaption()) == 0;
		assert message2.getTime() == 0L;
	}
	
	@Test
	public void testIsMessage() {
		IPMessage message = new IPMessage(0L, 0, 1, "bob", "test");
		assert message.isMessage("test", new String[0]);
		assert !message.isMessage("test", new String[] { "data1", "data2" });
		message.addData("data1", "hello");
		message.addData("data2", "goodbye");
		assert message.isMessage("test", new String[] { "data1", "data2" });
		assert !message.isMessage("test", new String[] { "data1", "data3" });
	}
	
}
