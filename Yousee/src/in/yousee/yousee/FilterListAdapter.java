package in.yousee.yousee;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class FilterListAdapter extends BaseExpandableListAdapter
{

	private Context context;
	private ArrayList<FilterGroupInfo> deptList;

	public FilterListAdapter(Context context, ArrayList<FilterGroupInfo> deptList)
	{
		this.context = context;
		this.deptList = deptList;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		ArrayList<FilterChildInfo> productList = deptList.get(groupPosition).getProductList();
		return productList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent)
	{

		FilterGroupInfo group = (FilterGroupInfo) getGroup(groupPosition);

		FilterChildInfo detailInfo = (FilterChildInfo) getChild(groupPosition, childPosition);

		if (view == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.child_row, null);
		}

		CheckBox check = (CheckBox) view.findViewById(R.id.sequence);
		check.setFocusable(false);
		detailInfo.setCheckBox(check);

		// sequence.setText(detailInfo.getSequence().trim() + ") ");
		TextView childItem = (TextView) view.findViewById(R.id.childItem);
		childItem.setText(detailInfo.getName().trim());
		detailInfo.setTextView(childItem);
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{

		ArrayList<FilterChildInfo> productList = deptList.get(groupPosition).getProductList();
		return productList.size();

	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return deptList.get(groupPosition);
	}

	@Override
	public int getGroupCount()
	{
		return deptList.size();
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent)
	{

		FilterGroupInfo headerInfo = (FilterGroupInfo) getGroup(groupPosition);
		if (view == null)
		{
			LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.group_heading, null);
		}

		TextView heading = (TextView) view.findViewById(R.id.heading);
		heading.setText(headerInfo.getName().trim());    
		return view;
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}

}