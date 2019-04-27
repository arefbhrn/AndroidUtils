package arsatech.co.utils.CustomViews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import arsatech.co.utils.Objects.NotifiableArrayList;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public abstract class _BaseNotifiableListAdapter<T, VH extends RecyclerView.ViewHolder> extends _BaseAdapter<VH> {

	private NotifiableArrayList<T> list;

	public _BaseNotifiableListAdapter(Context context, @NonNull NotifiableArrayList<T> list) {
		super(context);
		this.list = list;
	}

	public void setList(@NonNull NotifiableArrayList<T> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public NotifiableArrayList<T> getList() {
		return list;
	}

	public ArrayList<T> getArrayList() {
		return list.toArrayList();
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	public T getItem(int position) {
		return list.get(position);
	}

	public void add(T object) {
		list.add(object);
		notifyItemInserted(getItemCount() - 1);
	}

	public void addAll(ArrayList<T> newList) {
		int lastItemCount = getItemCount();
		list.addAll(newList);
		notifyItemRangeInserted(lastItemCount + 1, newList.size());
	}

}
