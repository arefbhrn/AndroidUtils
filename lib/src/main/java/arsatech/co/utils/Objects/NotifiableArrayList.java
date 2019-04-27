package arsatech.co.utils.Objects;

import java.util.ArrayList;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class NotifiableArrayList<T> {

	private ArrayList<T> mList = new ArrayList<>();
	private ArrayList<Listener<T>> listeners = new ArrayList<>();

	public interface Listener<T> {

		void onAdd(int position);

		void onAddAll(int start, int end);

		void onChangeItem(int position, T oldObject, T newObject);

		void onRemove(int position, T object);

		void onClear();

		void onNotifyDatasetChanged();

	}

	public NotifiableArrayList() {
	}

	public void addListener(Listener<T> listener) {
		listeners.add(listener);
	}

	public void removeListener(Listener<T> listener) {
		listeners.remove(listener);
	}

	public int size() {
		return mList.size();
	}

	public void add(T item) {
		if (item == null)
			return;
		mList.add(item);
		for (int i = 0; i < listeners.size(); i++) {
			if (listeners.get(i) != null)
				listeners.get(i).onAdd(mList.size() - 1);
		}
	}

	public void addAll(ArrayList<T> items) {
		if (items == null)
			return;
		mList.addAll(items);
		for (int i = 0; i < listeners.size(); i++) {
			if (listeners.get(i) != null)
				listeners.get(i).onAddAll(mList.size() - items.size(), mList.size() - 1);
		}
	}

	public T get(int position) {
		if (position < 0 || position >= mList.size())
			return null;
		return mList.get(position);
	}

	public void remove(int position) {
		if (position < 0 || position >= mList.size())
			return;
		T object = mList.remove(position);
		for (int i = 0; i < listeners.size(); i++) {
			if (listeners.get(i) != null)
				listeners.get(i).onRemove(position, object);
		}
	}

	public void change(int position, T newObject) {
		if (position < 0 || position >= mList.size())
			return;
		T oldObject = mList.get(position);
		mList.set(position, newObject);
		for (int i = 0; i < listeners.size(); i++) {
			if (listeners.get(i) != null)
				listeners.get(i).onChangeItem(position, oldObject, newObject);
		}
	}

	public void clear() {
		mList.clear();
		for (int i = 0; i < listeners.size(); i++) {
			if (listeners.get(i) != null)
				listeners.get(i).onClear();
		}
	}

	public void notifyDatasetChanged() {
		for (int i = 0; i < listeners.size(); i++) {
			if (listeners.get(i) != null)
				listeners.get(i).onNotifyDatasetChanged();
		}
	}

	public ArrayList<T> toArrayList() {
		return new ArrayList<>(mList);
	}

	public static class BaseListener<T> implements Listener<T> {

		@Override
		public void onAdd(int position) {
		}

		@Override
		public void onAddAll(int start, int end) {
		}

		@Override
		public void onChangeItem(int position, T oldObject, T newObject) {
		}

		@Override
		public void onRemove(int position, T object) {
		}

		@Override
		public void onClear() {
		}

		@Override
		public void onNotifyDatasetChanged() {
		}

	}

}