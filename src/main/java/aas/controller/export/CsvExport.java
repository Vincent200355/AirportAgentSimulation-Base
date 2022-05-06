package aas.controller.export;

import aas.model.util.Point;
import java.util.logging.Level;
import aas.model.communication.Message;
import aas.model.Agent;
import java.io.IOException;
import java.io.FileWriter;
import java.util.logging.Logger;

public class CsvExport implements Export {
	
	private static final Logger LOGGER = Logger.getLogger(CsvExport.class.getName());
	
	
	private FileWriter positionWriter;
	private FileWriter comWriter;
	
	public CsvExport(String positionFileName, String comFileName) {
		try {
			(this.positionWriter = new FileWriter(positionFileName)).append("Name");
			this.positionWriter.append(';');
			this.positionWriter.append("Time");
			this.positionWriter.append(';');
			this.positionWriter.append("X");
			this.positionWriter.append(';');
			this.positionWriter.append("Y");
			this.positionWriter.append('\n');
		} catch(IOException e) {
			throw new IllegalArgumentException("Could not access file " + positionFileName);
		}
		try {
			(this.comWriter = new FileWriter(comFileName)).append("Time");
			this.comWriter.append(';');
			this.comWriter.append("Sender");
			this.comWriter.append(';');
			this.comWriter.append("Receiver");
			this.comWriter.append(';');
			this.comWriter.append("Type");
			this.comWriter.append('\n');
		} catch(IOException e) {
			throw new IllegalArgumentException("Could not access file " + comFileName);
		}
	}
	
	@Override
	public void notify(int time, Agent agent, Message[] messages) {
		addPostion(agent.getFootprint().getName(), time, agent.getFootprint().getPosition());
		for(Message message : messages)
			if(message != null)
				if(message.getReceiver() != null)
					this.addMessage(message);
	}
	
	private void addMessage(Message message) {
		try {
			this.comWriter.append(Long.toString(message.getTime()));
			this.comWriter.append(';');
			this.comWriter.append(Integer.toString(message.getSender()));
			this.comWriter.append(';');
			this.comWriter.append(Integer.toString(message.getReceiver()));
			this.comWriter.append(';');
			this.comWriter.append(message.getClass().getSimpleName());
			this.comWriter.append('\n');
		} catch(IOException e) {
			CsvExport.LOGGER.log(Level.SEVERE, "Could not write to file ", e);
		}
	}
	
	public void addPostion(String name, long time, Point point) {
		try {
			this.positionWriter.append(name);
			this.positionWriter.append(';');
			this.positionWriter.append(Long.toString(time));
			this.positionWriter.append(';');
			this.positionWriter.append(Double.toString(point.getX()).replace(".", ","));
			this.positionWriter.append(';');
			this.positionWriter.append(Double.toString(point.getY()).replace(".", ","));
			this.positionWriter.append('\n');
		} catch(IOException e) {
			CsvExport.LOGGER.log(Level.SEVERE, "Could not write to file ", e);
		}
	}
	
	@Override
	public void finish() {
		try {
			this.positionWriter.flush();
			this.positionWriter.close();
			this.comWriter.flush();
			this.comWriter.close();
		} catch(IOException e) {
			CsvExport.LOGGER.log(Level.SEVERE, "Could not write to file ", e);
		}
	}
	
}
