package com.akp.tabir;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.akp.tabir.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayComments extends Activity{
	private final String NAMESPACE = "http://tempuri.org/";
	private final String URL = "http://tachar.net/Tabir.asmx";
	private final String SOAP_ACTION = "http://tempuri.org/GetTabirComments";
	private final String METHOD_NAME = "GetTabirComments";

	private static String strResult = "";
	private static String PageNo;
	private static String ItemCount;
	private Context mContext;
	private String TAG = "TabirApp";
	private static String strTabirCode = "";
	public int passCode = 0;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_comments);
		
		getWindow().getAttributes().windowAnimations = R.style.Fade;
		
		setTitle("äÙÑÇÊ ˜ÇÑÈÑÇä");
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
		mContext = getApplicationContext();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int Value = extras.getInt("code");
			if (Value > 0) {
				passCode = Value;
				strTabirCode = Integer.toString(passCode);
			}
		}

		AsyncCallWS task = new AsyncCallWS();
		task.execute();
	}
	
	public void GetComments(String PageNo, String ItemCount, String TabirCode) {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		PropertyInfo SecretKeyPI = new PropertyInfo();
		SecretKeyPI.setName("SecretKey");
		SecretKeyPI.setValue("mbam54jg34hfee345idbkl656lj3");
		SecretKeyPI.setType(String.class);
		request.addProperty(SecretKeyPI);

		PropertyInfo KeywordPI = new PropertyInfo();
		KeywordPI.setName("strTabirCode");
		KeywordPI.setValue(TabirCode);
		KeywordPI.setType(String.class);
		request.addProperty(KeywordPI);

		PropertyInfo ItemCountPI = new PropertyInfo();
		ItemCountPI.setName("strItemCount");
		ItemCountPI.setValue(ItemCount);
		ItemCountPI.setType(String.class);
		request.addProperty(ItemCountPI);

		PropertyInfo PageNoPI = new PropertyInfo();
		PageNoPI.setName("strPageNo");
		PageNoPI.setValue(PageNo);
		PageNoPI.setType(String.class);
		request.addProperty(PageNoPI);

		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			strResult = response.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class AsyncCallWS extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			Log.i(TAG, "doInBackground");
			PageNo = "1";
			ItemCount = "100";

			GetComments(PageNo, ItemCount, strTabirCode);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i(TAG, "onPostExecute");
			ProgressBar pBar = (ProgressBar) findViewById(R.id.progress_bar);
			pBar.setVisibility(View.GONE);

			JSONArray mArray = null;

			try {
				mArray = new JSONArray(strResult);
			} catch (JSONException jsonerr) {

			}
			List<CommentObject> CommentList = new ArrayList<CommentObject>();

			try {

				for (int i = 0; i < mArray.length(); i++) {
					JSONObject c = mArray.getJSONObject(i);// Used JSON Object
															// from Android

					// Storing each Json in a string variable
					int Code = c.getInt("Code");
					String Name = c.getString("SenderName");
					String Comment = c.getString("Comment");
					String SendDate = c.getString("SendDate");

					CommentList.add(new CommentObject(Code, Name, Comment,
							SendDate));

				}

				ListView lvComment = (ListView) findViewById(R.id.lvComments);
				CommentListAdapter customAdapter = new CommentListAdapter(
						mContext, R.layout.comment_list_item, CommentList);
				lvComment.setAdapter(customAdapter);
				
				LinearLayout LayoutComments = (LinearLayout) findViewById(R.id.LayoutComments);
				
				int listViewHeight = lvComment.getMeasuredHeight();
				int itemCount = lvComment.getCount();
				int itemHeight = lvComment.getMeasuredHeight();
				int dividerHeight = lvComment.getDividerHeight();
				int totalDividerHeight = (itemCount - 1) * dividerHeight;
				int targetTotalItemHeight = listViewHeight - totalDividerHeight;
				int totalItemHeight = itemCount * itemHeight;
				boolean weNeedToUpsize = totalItemHeight < targetTotalItemHeight;
				if (weNeedToUpsize) {
					int targetItemHeight = targetTotalItemHeight / itemCount;
					lvComment.setMinimumHeight(targetItemHeight);
				}
				
				if( customAdapter.getCount() == 0)
				{
					LayoutComments.setVisibility(View.GONE);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
			}

			// ListView yourListView = (ListView) findViewById(R.id.listViewID);

		}

		@Override
		protected void onPreExecute() {
			ProgressBar pBar = (ProgressBar) findViewById(R.id.progress_bar);
			pBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.i(TAG, "onProgressUpdate");
		}

	}

}
