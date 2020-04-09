package it.angelic.growlroom.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class QueueCommands {

	@Id
	private Integer idQueue;
	private Command toExecute;
	private Date creationTime;
}
