package arsatech.co.utils.Models;

import java.io.Serializable;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class _BaseModel implements Serializable {

	private int id;
	private String name;

	public _BaseModel() {
	}

	public int getId() {
		return id;
	}

	public _BaseModel setId(int id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public _BaseModel setName(String name) {
		this.name = name;
		return this;
	}

}
