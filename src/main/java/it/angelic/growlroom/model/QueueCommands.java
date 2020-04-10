package it.angelic.growlroom.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class QueueCommands {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idQueueCommand;
	private final Command toExecute;
	private final Date creationTime;
	
	public QueueCommands(Command toExecute) {
		this.toExecute = toExecute;
		creationTime = new Date();
	}

	public Command getToExecute() {
		return toExecute;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public Long getIdQueueCommand() {
		return idQueueCommand;
	}
}
