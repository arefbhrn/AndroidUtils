package arsatech.co.utils.Models;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class Contact extends _BaseModel {

	public Contact() {
	}

	private String phoneNumber;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Contact setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

}
