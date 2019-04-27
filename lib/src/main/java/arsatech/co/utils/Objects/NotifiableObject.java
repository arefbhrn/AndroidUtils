package arsatech.co.utils.Objects;

import java.util.ArrayList;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class NotifiableObject<T> {

	private T mObject;
	private ArrayList<Listener> listeners = new ArrayList<>();

	public interface Listener {

		void onNotifyObjectChanged();

	}

	public NotifiableObject() {
	}

	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	public void notifyObjectChanged() {
		for (int i = 0; i < listeners.size(); i++) {
			if (listeners.get(i) != null)
				listeners.get(i).onNotifyObjectChanged();
		}
	}

}
