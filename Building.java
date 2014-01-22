package org.cityofchicago.dob.bfax;
 
import org.cityofchicago.dob.bfax.R;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Building extends ListActivity {

	private ProgressDialog pDialog;

	// URL to get contacts JSON
	//private static String url = "http://api.androidhive.info/contacts/";
	private static String url = "https://data.cityofchicago.org/resource/building-permits.json?%24select=street_number%2Cstreet_direction%2Cstreet_name%2C_issue_date%2Clatitude%2Clongitude%2C_permit_type%2Cwork_description%2Cpermit_&%24where=street_number%20=%20611%20and%20street_name%20=%20%27WELLS%27";
	
	// JSON Node names
	private static final String TAG_CONTACTS = "contacts";
	//private static final String TAG_ID = "id";
	//private static final String TAG_NAME = "name";
	//private static final String TAG_EMAIL = "email";
	//private static final String TAG_ADDRESS = "address";
	private static final String TAG_ID = "permit_";
	private static final String TAG_NAME = "street_name";
	private static final String TAG_EMAIL = "street_number";
	private static final String TAG_ADDRESS = "street_direction";
	private static final String TAG_GENDER = "work_description";
	private static final String TAG_PHONE = "_issue_date";
	private static final String TAG_PHONE_MOBILE = "mobile";
	private static final String TAG_PHONE_HOME = "home";
	private static final String TAG_PHONE_OFFICE = "office";

	// contacts JSONArray
	JSONArray contacts = null;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> contactList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.building_page);

		contactList = new ArrayList<HashMap<String, String>>();

		ListView lv = getListView();

		// Listview on item click listener
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String name = ((TextView) view.findViewById(R.id.name))
						.getText().toString();
				String cost = ((TextView) view.findViewById(R.id.email))
						.getText().toString();
				String description = ((TextView) view.findViewById(R.id.mobile))
						.getText().toString();

				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						SingleContactActivity.class);
				in.putExtra(TAG_NAME, name);
				in.putExtra(TAG_EMAIL, cost);
				in.putExtra(TAG_PHONE_MOBILE, description);
				startActivity(in);

			}
		});

		// Calling async task to get json
		new GetContacts().execute();
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(Building.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					//JSONObject jsonObj = new JSONObject(jsonStr);
					 contacts = new JSONArray(jsonStr);
					// Getting JSON Array node
					//contacts = jsonObj.getJSONArray(TAG_CONTACTS);
					//contacts = jsonObj.getJSONArray("0");
					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);
						
						String id = c.getString(TAG_ID);
						String name = c.getString(TAG_NAME);
						String email = c.getString(TAG_EMAIL);
						//String address = c.getString(TAG_ADDRESS);
						String gender = c.getString(TAG_GENDER);

						// Phone node is JSON Object
						//JSONObject phone = c.getJSONObject(TAG_PHONE);
						//String mobile = phone.getString(TAG_PHONE_MOBILE);
						String mobile = c.getString(TAG_ADDRESS);
						String home = c.getString(TAG_PHONE);
						//String office = phone.getString(TAG_PHONE_OFFICE);

						// tmp hashmap for single contact
						HashMap<String, String> contact = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						contact.put(TAG_ID, id);
						contact.put(TAG_NAME, email+' '+mobile+' '+name);
						contact.put(TAG_EMAIL, gender);
						contact.put(TAG_PHONE_MOBILE, home);

						// adding contact to contact list
						contactList.add(contact);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(
					Building.this, contactList,
					R.layout.list_item, new String[] { TAG_NAME, TAG_EMAIL,
							TAG_PHONE_MOBILE }, new int[] { R.id.name,
							R.id.email, R.id.mobile });

			setListAdapter(adapter);
		}

	}

}