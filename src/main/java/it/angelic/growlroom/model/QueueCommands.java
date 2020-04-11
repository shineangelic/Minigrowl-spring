package it.angelic.growlroom.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class QueueCommands {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idQueueCommand;
	private Command toExecute;
	private Date creationTime;

	public QueueCommands() {
		super();
	}

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

	public void setIdQueueCommand(Long idQueueCommand) {
		this.idQueueCommand = idQueueCommand;
	}

	public void setToExecute(Command toExecute) {
		this.toExecute = toExecute;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
}
