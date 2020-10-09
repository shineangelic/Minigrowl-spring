package it.angelic.growlroom.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class QueueCommands {

	@Id
	@SequenceGenerator(name = "queue_command_id_seq", sequenceName = "queue_command_id_queue_command_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "queue_command_id_seq")
	@Column(columnDefinition="serial")
	private Long idQueueCommand;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "command_id", nullable = false,unique=true)
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
