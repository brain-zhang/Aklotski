package me.happy123.aklotski;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WarAdapter extends ArrayAdapter<WarRecord> {

	private int resourceId;

	public WarAdapter(Context context, int textViewResourceId,
			List<WarRecord> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		WarRecord war = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.warName = (TextView) view.findViewById(R.id.war_name);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.warName.setText(war.getName());
		if (war.getStatus() == 1) {
			viewHolder.warName.setTextColor(Color.GREEN);
		} else {
			viewHolder.warName.setTextColor(Color.BLACK);
		}
		return view;
	}
	
	class ViewHolder {
		TextView warName;
	}
}
