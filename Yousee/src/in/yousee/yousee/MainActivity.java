package in.yousee.yousee;

import in.yousee.yousee.model.ProxyOpportunityItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class MainActivity extends SherlockActivity implements OnItemClickListener
{

	private FrameLayout filterFrame;
	private Button updateButton;
	ListView listview;
	OpportunityListBuilder listBuilder;
	ArrayList<ProxyOpportunityItem> proxyList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// setSupportProgressBarIndeterminate(false);
		// setSupportProgressBarIndeterminate(true);
		// requestWindowFeature(Window.FEATURE_PROGRESS);
		// setSupportProgressBarVisibility(true);
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_main);
		filterFrame = (FrameLayout) findViewById(R.id.filterFrame);
		updateButton = (Button) findViewById(R.id.updateButton);
		setUpdateButtonOnClickListener();

		initiateExpandableList();

		buildOpportunityListForTheFirstTime();
		// sendRequest();
		// sendTestRequest();
	}

	private void buildOpportunityListForTheFirstTime()
	{
		Log.i("tag", "building opportunity list");
		listBuilder = new OpportunityListBuilder(this);
		listBuilder.execute();
	}

	public void createOpportunityListView(ArrayList<ProxyOpportunityItem> proxyList)
	{

		// Log.i("tag", "creating");
		this.proxyList = proxyList;
		listview = (ListView) findViewById(R.id.opportunityListview);

		String[] titles = new String[proxyList.size()];
		String[] types = new String[proxyList.size()];
		int index = 0;
		Iterator it = proxyList.iterator();
		while (it.hasNext())
		{
			ProxyOpportunityItem item = (ProxyOpportunityItem) it.next();
			titles[index] = item.getTitle();
			types[index] = item.getOpportunityType();
			index++;

		}

		OpportunityListAdapter adapter = new OpportunityListAdapter(getApplicationContext(), titles, types);
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		listview.setOnItemClickListener(this);

	}

	private void setUpdateButtonOnClickListener()
	{
		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Iterator<FilterGroupInfo> it = filterGroupList.iterator();
				while (it.hasNext())
				{
					FilterGroupInfo group = it.next();
					Log.i("tag", "+" + group.getName());
					Iterator<FilterChildInfo> childIterator = group.getProductList().iterator();
					while (childIterator.hasNext())
					{
						FilterChildInfo child = childIterator.next();
						if (child.isChecked())
						{
							Log.i("tag", "	" + child.getName());
						}
					}

				}
				showFilterMenu(false);
			}
		});

	}

	private boolean filterMenuVisibility = false;

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
			{
			case R.id.action_filter:
				filterMenuVisibility = !(filterMenuVisibility);
				showFilterMenu(filterMenuVisibility);

				break;

			default:
				break;
			}
		return true;
	}

	public void showFilterMenu(boolean visibility)
	{
		filterMenuVisibility = visibility;
		if (visibility)
			filterFrame.setVisibility(View.VISIBLE);
		else
			filterFrame.setVisibility(View.INVISIBLE);
	}

	public void sendRequest()
	{

	}

	private LinkedHashMap<String, FilterGroupInfo> filterCatagories = new LinkedHashMap<String, FilterGroupInfo>();
	private ArrayList<FilterGroupInfo> filterGroupList = new ArrayList<FilterGroupInfo>();

	private FilterListAdapter listAdapter;
	private ExpandableListView myList;

	public void initiateExpandableList()
	{
		// get reference to the ExpandableListView
		myList = (ExpandableListView) findViewById(R.id.expandableListView1);
		// setPadding();
		// create the adapter by passing your ArrayList data
		listAdapter = new FilterListAdapter(MainActivity.this, filterGroupList);
		// attach the adapter to the list
		myList.setAdapter(listAdapter);

		Log.i("tag", "before expand all");
		// expand all Groups
		expandAll();

		// add new item to the List
		// listener for child row click
		myList.setOnChildClickListener(myListItemClicked);
		// listener for group heading click
		myList.setOnGroupClickListener(myListGroupClicked);

		// add a new item to the list
		loadData();
		// notify the list so that changes can take effect
		listAdapter.notifyDataSetChanged();

		// collapse all groups
		// collapseAll();
		// expand the group where item was just added
		// myList.expandGroup(groupPosition);
		// set the current group to be selected so that it becomes
		// visible
		// myList.setSelectedGroup(groupPosition);
	}

	// method to expand all groups
	private void expandAll()
	{
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++)
		{
			myList.expandGroup(i);
		}
	}

	// method to collapse all groups
	private void collapseAll()
	{
		int count = listAdapter.getGroupCount();
		for (int i = 0; i < count; i++)
		{
			myList.collapseGroup(i);
		}
	}

	// load some initial data into out list
	private void loadData()
	{
		addProduct("Area", "Education");
		addProduct("Area", "Environment");
		addProduct("Area", "Health");

		addProduct("Domain", "Documentation");
		addProduct("Domain", "Project Activity");
		addProduct("Domain", "Technology");

		addProduct("City", "Bangalore");
		addProduct("City", "Hyderabad");
		addProduct("City", "Lucknow");

		addProduct("Acivity Type", "Onsite");
		addProduct("Acivity Type", "Offsite");

	}

	// our child listener
	public OnChildClickListener myListItemClicked = new OnChildClickListener() {

		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
		{

			Log.d("tag", "child is selected");
			// get the group header
			FilterGroupInfo headerInfo = filterGroupList.get(groupPosition);
			// get the child info
			FilterChildInfo detailInfo = headerInfo.getProductList().get(childPosition);
			// display it or do something with it
			Toast.makeText(getBaseContext(), "Clicked on Detail " + headerInfo.getName() + "/" + detailInfo.getName(), Toast.LENGTH_SHORT).show();
			CheckBox checkBox = detailInfo.getCheckBox();
			checkBox.setChecked(!checkBox.isChecked());
			return false;
		}

	};

	// our group listener
	private OnGroupClickListener myListGroupClicked = new OnGroupClickListener() {

		public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
		{

			// get the group header
			FilterGroupInfo headerInfo = filterGroupList.get(groupPosition);
			// display it or do something with it
			Toast.makeText(getBaseContext(), "Child on Header " + headerInfo.getName(), Toast.LENGTH_SHORT).show();

			return false;
		}

	};

	// here we maintain our products in various departments
	private int addProduct(String department, String product)
	{

		int groupPosition = 0;

		// check the hash map if the group already exists
		FilterGroupInfo headerInfo = filterCatagories.get(department);
		// add the group if doesn't exists
		if (headerInfo == null)
		{
			headerInfo = new FilterGroupInfo();
			headerInfo.setName(department);
			filterCatagories.put(department, headerInfo);
			filterGroupList.add(headerInfo);
		}

		// get the children for the group
		ArrayList<FilterChildInfo> productList = headerInfo.getProductList();
		// size of the children list
		int listSize = productList.size();
		// add to the counter
		listSize++;

		// create a new child and add that to the group
		FilterChildInfo detailInfo = new FilterChildInfo();
		detailInfo.setSequence(String.valueOf(listSize));
		detailInfo.setName(product);
		productList.add(detailInfo);
		headerInfo.setProductList(productList);

		// find the group position inside the list
		groupPosition = filterGroupList.indexOf(headerInfo);
		return groupPosition;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void fancyThat(View v)
	{
		v.getBackground().setAlpha(50);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Log.i("tag", "item clicked " + position);

		Intent intent = new Intent();
		intent.setClass(this, IndividualOpportunityItemActivity.class);
		intent.putExtra("result", proxyList.get(position).toJsonString());
		startActivity(intent);

	}

}
