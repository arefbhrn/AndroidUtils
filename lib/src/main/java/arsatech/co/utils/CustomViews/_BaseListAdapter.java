package arsatech.co.utils.CustomViews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public abstract class _BaseListAdapter<T, VH extends RecyclerView.ViewHolder> extends _BaseAdapter<VH> {

	private ArrayList<T> list;

	public _BaseListAdapter(Context context, @NonNull ArrayList<T> list) {
		super(context);
		this.list = list;
	}

	public void setList(@NonNull ArrayList<T> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public ArrayList<T> getList() {
		return list;
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
