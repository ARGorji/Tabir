package com.akp.tabir;

import java.util.ArrayList;
import java.util.Arrays;
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

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.tabir.DBHelper;
import com.akp.tabir.R;
import com.akp.utils.Tools;

public class DisplayTabir extends Activity {
	int from_Where_I_Am_Coming = 0;
	private DBHelper mydb;
	WebView Meaning;
	TextView WordTitle;
	public int passCode = 0;
	private String dbWord = "";
	private String TAG = "TabirApp";
	private static String PageNo;
	private static String ItemCount;
	private static String strResult = "";
	private static Boolean SendResult = false;
	private static String strTabirCode = "";

	private final String NAMESPACE = "http://tempuri.org/";
	private final String URL = "http://tachar.net/Tabir.asmx";
	private final String SOAP_ACTION = "http://tempuri.org/GetTabirComments";
	private final String METHOD_NAME = "GetTabirComments";
	private final String SEND_METHOD_NAME = "SendComment";
	private final String SOAP_SEND_ACTION = "http://tempuri.org/SendComment";
	private TextView tvMessage;
	private Context mContext;
	private static String GetDefaultFontName = "";
	private static String strCurContentFontSize;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_tabir);

		try {
			mContext = getApplicationContext();
			Meaning = (WebView) findViewById(R.id.webview);
			WordTitle = (TextView) findViewById(R.id.tvWordTitle);

			Typeface BYekanFont = Typeface.createFromAsset(mContext.getAssets(),
					"fonts/byekan.ttf");
			WordTitle.setTypeface(BYekanFont);

			mydb = new DBHelper(this);

			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				int Value = extras.getInt("code");
				if (Value > 0) {
					// means this is the view part not the add contact part.
					Cursor rs = mydb.GetDetails(Value);
					passCode = Value;
					strTabirCode = Integer.toString(passCode);
					rs.moveToFirst();
					String dbMeaning = rs.getString(rs
							.getColumnIndex(DBHelper.Tabirs_COLUMN_Meaning));
					dbWord = rs.getString(rs
							.getColumnIndex(DBHelper.Tabirs_COLUMN_Word));

					if (!rs.isClosed()) {
						rs.close();
					}
					
					Tools tools = new Tools();

					GetDefaultFontName = tools.GetDefaultFontName(mContext);
					String strCurHeaderFontSize = tools.GetSettingVal(
							getString(R.string.app_default_key), mContext,
							"HeaderFontSize");
					strCurContentFontSize = tools.GetSettingVal(
							getString(R.string.app_default_key), mContext,
							"ContentFontSize");
					if (strCurContentFontSize == "")
						strCurContentFontSize = "18";
					
					dbMeaning = GenParagraph(dbMeaning);
					String text = "<html><body>"
							+ "<style type=\"text/css\">"
							+ "@font-face {"
							+ "    font-family: MyFont;"
							+ "    src: url(\"file:///android_asset/fonts/"
							+ GetDefaultFontName
							+ ".ttf\")"
							+ "}"
							+ "</style>"
							+ "<div style=\"font-family:MyFont;text-align:justify;direction:rtl;font-size:"
							+ strCurContentFontSize + "px;line-height:130%;padding:10px;border-right:solid 1px #CFCFCF;\">"
							+ dbMeaning + "</div> " + "</body></html>";

					WebView mWebView = (WebView) findViewById(R.id.webview);
					mWebView.setBackgroundColor(Color.parseColor("#EDF0F5"));
					mWebView.loadDataWithBaseURL(null, text, "text/html", "utf-8",
							null);


					WordTitle.setText("تعبیر " + dbWord);

					Context context = getApplicationContext();
					SharedPreferences sharedPref = context
							.getSharedPreferences(
									getString(R.string.tabir_app_file_key),
									Context.MODE_PRIVATE);
					String CurrentFavCodes = sharedPref.getString(
							getString(R.string.Favorite_Item_list), "");
					String[] CurrentFavCodesArray = CurrentFavCodes.split(",");
					List<String> resultList = Arrays
							.asList(CurrentFavCodesArray);
					if (resultList.contains("\"" + Value + "\"")) {
						ImageButton btnAddTOFav = (ImageButton) findViewById(R.id.btnAddToFavorite);
						btnAddTOFav.setImageResource(R.drawable.star_on);
						btnAddTOFav.setBackgroundDrawable(null);

					}
					AsyncCallWS task = new AsyncCallWS();
					task.execute();

					
					Button btnDisplayAllComments = (Button) findViewById(R.id.btnDisplayAllComments);
					btnDisplayAllComments.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							try {

				                Bundle dataBundle = new Bundle();
				                dataBundle.putInt("code", Integer.parseInt(strTabirCode) );
				                Intent intent = new Intent(getApplicationContext(),DisplayComments.class);
				                intent.putExtras(dataBundle);
				                startActivity(intent);
				                
				                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


							} catch (Exception err1) {
								Toast.makeText(getApplicationContext(),
										err1.getMessage(), Toast.LENGTH_LONG).show();
							}

						}

					});

					
					
					Button btnSendComment = (Button) findViewById(R.id.btnSubmit);
					btnSendComment.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							try {

								AsyncCallSendCommentWS task = new AsyncCallSendCommentWS();
								task.execute();


							} catch (Exception err1) {
								Toast.makeText(getApplicationContext(),
										err1.getMessage(), Toast.LENGTH_LONG).show();
							}

						}

					});
					
					
					
					
					
					
					
					
					

				}
			}



			ImageButton btnAddToFavorite = (ImageButton) findViewById(R.id.btnAddToFavorite);
			btnAddToFavorite.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					try {
						ImageButton btnAddTOFav = (ImageButton) findViewById(R.id.btnAddToFavorite);
						String strNewFavList = "";
						String strCurCode = "\""
								+ Integer.toString(DisplayTabir.this.passCode)
								+ "\"";
						Context context = v.getContext();
						SharedPreferences sharedPref = context
								.getSharedPreferences(
										getString(R.string.tabir_app_file_key),
										Context.MODE_PRIVATE);
						String CurrentFavCodes = sharedPref.getString(
								getString(R.string.Favorite_Item_list), "");
						String[] CurrentFavCodesArray = CurrentFavCodes
								.split(",");
						List<String> resultList = Arrays
								.asList(CurrentFavCodesArray);
						if (!resultList.contains(strCurCode)) {
							if (CurrentFavCodes == "")
								strNewFavList = strCurCode;
							else
								strNewFavList = CurrentFavCodes + ","
										+ strCurCode;

							btnAddTOFav.setImageResource(R.drawable.star_on);
							btnAddTOFav.setBackgroundDrawable(null);

							Toast.makeText(
									getApplicationContext(),
									"تعبیر "
											+ dbWord
											+ " به لیست علاقه مندی ها اضافه شد. ",
									Toast.LENGTH_LONG).show();
						} else {

							for (int i = 0; i < CurrentFavCodesArray.length; i++) {
								if (!CurrentFavCodesArray[i].toString().equals(
										strCurCode)) {
									if (strNewFavList == "")
										strNewFavList = CurrentFavCodesArray[i];
									else
										strNewFavList += ","
												+ CurrentFavCodesArray[i];
								}

							}

							btnAddTOFav.setImageResource(R.drawable.star_off);
							btnAddTOFav.setBackgroundDrawable(null);

							Toast.makeText(
									getApplicationContext(),
									"تعبیر " + dbWord
											+ " از لیست علاقه مندی ها حذف شد ",
									Toast.LENGTH_LONG).show();
						}

						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putString(
								getString(R.string.Favorite_Item_list),
								strNewFavList);
						editor.commit();
					} catch (Exception err1) {
						Toast.makeText(getApplicationContext(),
								err1.getMessage(), Toast.LENGTH_LONG).show();
					}

				}

			});

		} catch (Exception err) {
			Toast.makeText(getApplicationContext(), err.getMessage(),
					Toast.LENGTH_LONG).show();

		}

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
	
	public void SendComment(String TabirCode, String SenderName, String SenderComment) {
		SoapObject request = new SoapObject(NAMESPACE, SEND_METHOD_NAME);

		PropertyInfo SecretKeyPI = new PropertyInfo();
		SecretKeyPI.setName("SecretKey");
		SecretKeyPI.setValue("mbam54jg34hfee345idbkl656lj3");
		SecretKeyPI.setType(String.class);
		request.addProperty(SecretKeyPI);

		PropertyInfo TabirCodePI = new PropertyInfo();
		TabirCodePI.setName("strTabirCode");
		TabirCodePI.setValue(TabirCode);
		TabirCodePI.setType(String.class);
		request.addProperty(TabirCodePI);

		PropertyInfo SenderNamePI = new PropertyInfo();
		SenderNamePI.setName("SenderName");
		SenderNamePI.setValue(SenderName);
		SenderNamePI.setType(String.class);
		request.addProperty(SenderNamePI);

		PropertyInfo CommentPI = new PropertyInfo();
		CommentPI.setName("Comment");
		CommentPI.setValue(SenderComment);
		CommentPI.setType(String.class);
		request.addProperty(CommentPI);

		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			androidHttpTransport.call(SOAP_SEND_ACTION, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			SendResult = Boolean.parseBoolean(response.toString()); 

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private String GenParagraph(String FullStory) {
		try {
			int DotPos = 0;
			int SavedDotPos = 0;
			String Result = "";
			int Index = 0;
			int ParagraphLen = 200;
			int LoopCounter = 0;
			while (Index < FullStory.length() && LoopCounter < 100) {
				SavedDotPos = DotPos;
				if (Index + ParagraphLen < FullStory.length())
					DotPos = FullStory.indexOf(".", Index + ParagraphLen);
				else
					DotPos = 0;
				if (DotPos > 0) {
					Result = Result
							+ FullStory
									.substring(Index, Index + DotPos - Index)
							+ ".<br /><br />";
					Index = DotPos + 1;
				} else {
					Result = Result
							+ FullStory.substring(SavedDotPos + 1, SavedDotPos
									+ FullStory.length() - SavedDotPos)
							+ "<br />";
					Index = FullStory.length();
				}
				LoopCounter++;
			}
			return Result;
		} catch (Exception err) {
			return FullStory;
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
			tvMessage = (TextView) findViewById(R.id.tvMessage);
			tvMessage.setText("");
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
			Log.i(TAG, "onPreExecute");
			tvMessage = (TextView) findViewById(R.id.tvMessage);
			tvMessage.setText("در حال دریافت نظرات . . .");
			ProgressBar pBar = (ProgressBar) findViewById(R.id.progress_bar);
			pBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.i(TAG, "onProgressUpdate");
		}

	}

	
	private class AsyncCallSendCommentWS extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			Log.i(TAG, "doInBackground");

			EditText txtName = (EditText) findViewById(R.id.txtName);
			EditText txtComment = (EditText) findViewById(R.id.txtComment);
			
			String SenderName = txtName.getText().toString();
			String SenderComment = txtComment.getText().toString();
			
			if(SenderName.length() == 0)
			{
				Toast.makeText(getApplicationContext(),	"لطفا نام را وارد کنید", Toast.LENGTH_LONG).show();
				return null;
			}
			else if(SenderComment.length() == 0)
			{
				Toast.makeText(getApplicationContext(),	"لطفا نظر را وارد کنید", Toast.LENGTH_LONG).show();
				return null;
			}
			else
				SendComment(strTabirCode, SenderName, SenderComment);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i(TAG, "onPostExecute");
			tvMessage = (TextView) findViewById(R.id.tvMessage);
			if(SendResult)
				tvMessage.setText("نظر شما با موفقت ارسال شد و پس از تایید در لیست نظرات قرار می گیرد");
			else
				tvMessage.setText("خطل در ارسال نظر");
				
			ProgressBar pBar = (ProgressBar) findViewById(R.id.progress_bar);
			pBar.setVisibility(View.GONE);

		}

		@Override
		protected void onPreExecute() {
			Log.i(TAG, "onPreExecute");
			tvMessage = (TextView) findViewById(R.id.tvMessage);
			tvMessage.setText("در حال دریافت نظرات . . .");
			ProgressBar pBar = (ProgressBar) findViewById(R.id.progress_bar);
			pBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.i(TAG, "onProgressUpdate");
		}

	}
	
	
}
