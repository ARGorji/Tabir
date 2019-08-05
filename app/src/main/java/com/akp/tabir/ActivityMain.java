package com.akp.tabir;

import java.io.IOException;
import java.util.ArrayList;

import com.akp.adapter.NavDrawerListAdapter;
import com.akp.model.NavDrawerItem;
import com.akp.tabir.DBHelper;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityMain extends ActionBarActivity {
	private Toolbar toolbar;

   private ListView obj;	
	DBHelper mydb;
	
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
 
    // nav drawer title
    private CharSequence mDrawerTitle;
 
    // used to store app title
    private CharSequence mTitle;
 
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
 
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private Context mContext;
    

	private void initView() {

		try {
			toolbar = (Toolbar) findViewById(R.id.tool_bar);
			setSupportActionBar(toolbar);

			LayoutInflater mInflater = LayoutInflater.from(this);
			View mCustomView = mInflater.inflate(R.layout.tool_bar, null);
			getSupportActionBar().setCustomView(mCustomView);
			getSupportActionBar().setDisplayShowCustomEnabled(true);
			Toolbar parent = (Toolbar) mCustomView.getParent();
			parent.setContentInsetsAbsolute(0, 0);

			
			getWindow().getDecorView().setLayoutDirection(
					View.LAYOUT_DIRECTION_RTL);

			ImageView btnCloseSearchArea = (ImageView) toolbar
					.findViewById(R.id.toolbar_close_searcharea);
			btnCloseSearchArea.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {

						ImageView btnShowSearchArea = (ImageView) findViewById(R.id.toolbar_btnshowsearch_area);
						btnShowSearchArea.setVisibility(View.VISIBLE);
						ImageButton ShowDrawer = (ImageButton) findViewById(R.id.btn_showdrawer);
						ShowDrawer.setVisibility(View.VISIBLE);

						LinearLayout SearchArea = (LinearLayout) findViewById(R.id.toolbar_search_area);
						SearchArea.setVisibility(View.GONE);

						EditText txtKeyword = (EditText) findViewById(R.id.toolbar_keyword);
						txtKeyword.setText("جستجو در تعبیرها ...");

					} catch (Exception ex) {

					}

				}
			});

			ImageView btnShowSearchArea = (ImageView) toolbar
					.findViewById(R.id.toolbar_btnshowsearch_area);
			btnShowSearchArea.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {

						ImageView btnShowSearchArea = (ImageView) findViewById(R.id.toolbar_btnshowsearch_area);
						btnShowSearchArea.setVisibility(View.GONE);
						ImageButton ShowDrawer = (ImageButton) findViewById(R.id.btn_showdrawer);
						ShowDrawer.setVisibility(View.GONE);

						LinearLayout SearchArea = (LinearLayout) findViewById(R.id.toolbar_search_area);
						SearchArea.bringToFront();

						Animation slide_left_right = AnimationUtils
								.loadAnimation(mContext,
										R.anim.slide_left_to_right);
						SearchArea.setVisibility(View.VISIBLE);
						SearchArea.startAnimation(slide_left_right);

					} catch (Exception ex) {

					}

				}
			});

			ImageView btnSearch = (ImageView) toolbar
					.findViewById(R.id.toolbar_btnsearch);
			btnSearch.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {

						performSearch();

					} catch (Exception ex) {

					}

				}
			});

			final EditText txtKeyword = (EditText) toolbar
					.findViewById(R.id.toolbar_keyword);

			txtKeyword.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						if (txtKeyword.getText().toString()
								.equals("جستجو در خبرها ..."))
							txtKeyword.setText("");
					} else {
						txtKeyword.setText("جستجو در خبرها ...");
					}
				}
			});

			ImageButton btnShowDrawer = (ImageButton) toolbar
					.findViewById(R.id.btn_showdrawer);
			btnShowDrawer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {

						mDrawerLayout.openDrawer(Gravity.RIGHT);

					} catch (Exception ex) {

					}

				}
			});

			ImageView btnShowSettings = (ImageView) findViewById(R.id.toolbar_small_settings);
			btnShowSettings.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {

						//Intent refresh = new Intent(mContext,
						//		ActivitySettings.class);
						//startActivity(refresh);
						//overridePendingTransition(R.anim.pull_in_right,
						//		R.anim.push_out_left);

					} catch (Exception ex) {

					}

				}
			});

			ImageView btnShowHelp = (ImageView) findViewById(R.id.toolbar_help);
			btnShowHelp.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {

						//Intent refresh = new Intent(mContext,
						//		ActivityHelp.class);
						//startActivity(refresh);
						//overridePendingTransition(R.anim.pull_in_right,
						//		R.anim.push_out_left);

					} catch (Exception ex) {

					}

				}
			});

			
			
			final ImageView btnLamp = (ImageView) toolbar
					.findViewById(R.id.toolbar_lamp);
			btnLamp.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Boolean IsLampOn = false;
						if (btnLamp.getTag() != null) {
							IsLampOn = (Boolean) btnLamp.getTag();
						}

						if (IsLampOn) {
							btnLamp.setImageResource(R.drawable.ic_lamp_off);
							btnLamp.setTag(false);
							getWindow()
									.clearFlags(
											WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
							Toast.makeText(
									getApplicationContext(),
									"حالت جلوگیری از به خواب رفتن دستگاه غیر فعال شد",
									Toast.LENGTH_LONG).show();
						} else {
							btnLamp.setImageResource(R.drawable.ic_lamp_on);
							btnLamp.setTag(true);
							getWindow()
									.addFlags(
											WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
							Toast.makeText(
									getApplicationContext(),
									"حالت جلوگیری از به خواب رفتن دستگاه فعال شد",
									Toast.LENGTH_LONG).show();
						}

					} catch (Exception ex) {

					}

				}
			});


			txtKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			    @Override
			    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
			        	View view = getCurrentFocus();
			        	if (view != null) {  
			        	    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			        	    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			        	}
			            performSearch();
			            return true;
			        }
			        return false;
			    }
			});
			
	        txtKeyword.addTextChangedListener(new TextWatcher() {
	            @Override
	            public void onTextChanged(CharSequence s, int start, int before, int count) {

	            	try
	            	{
	                TextView txtKeyword = (TextView) findViewById(R.id.toolbar_keyword);
	            	

	            	String[] columns = new String[] { "_id", "Word", "Meaning" };
	            	int[] to = new int[] { R.id.Code_entry, R.id.Word_entry, R.id.ShortMeaning_entry};
	            	
	    	        Cursor c = mydb.SearchTabirs("%" + txtKeyword.getText().toString() + "%");
	    	        obj = (ListView)findViewById(R.id.lvTabirs);
	    	        
	    	        CustomTabirAdapter cAdapter = new CustomTabirAdapter(mContext, R.layout.list_entry, c, columns, to);
	    	        obj.setAdapter(cAdapter);


	            	}
	            	catch(Exception err1)
	            	{
	                    Toast.makeText(getApplicationContext(), err1.getMessage(),
	                    Toast.LENGTH_LONG).show();
	            	}           }

	            @Override
	            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	                // TODO Auto-generated method stub
	            }

	            @Override
	            public void afterTextChanged(Editable editable) {


	            }
	        });
			
		} catch (Exception err1) {
			Toast.makeText(getApplicationContext(), err1.getMessage(),
					Toast.LENGTH_LONG).show();

		}

	}
    
	private void performSearch()
	{
		try
    	{
        TextView txtKeyword = (TextView) findViewById(R.id.toolbar_keyword);
    	

    	String[] columns = new String[] { "_id", "Word", "Meaning" };
    	int[] to = new int[] { R.id.Code_entry, R.id.Word_entry, R.id.ShortMeaning_entry};
    	
        Cursor c = mydb.SearchTabirs("%" + txtKeyword.getText().toString() + "%");
        obj = (ListView)findViewById(R.id.lvTabirs);
        
        CustomTabirAdapter cAdapter = new CustomTabirAdapter(mContext, R.layout.list_entry, c, columns, to);
        obj.setAdapter(cAdapter);


    	}
    	catch(Exception err1)
    	{
            Toast.makeText(getApplicationContext(), err1.getMessage(),
            Toast.LENGTH_LONG).show();
    	}
	}
	
    private boolean RotateScreen()
    {
    	int orientation = getResources().getConfiguration().orientation;
    	if(orientation == Configuration.ORIENTATION_PORTRAIT)
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	else
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	return true;
    	
    }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mContext = getApplicationContext();
        mTitle = mDrawerTitle = getTitle();
        
        initView();
        
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
 
        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
 
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        //mDrawerLayout.setFocusable(true);
        //mDrawerLayout.setClickable(true);
        
        
        mydb = new DBHelper(this);
        try
        {
    	getApplicationContext().deleteDatabase("TabirDB");
    	mydb.createDataBase();
        }
    	catch(IOException err2)
    	{
            Toast.makeText(getApplicationContext(), err2.getMessage(),
            Toast.LENGTH_LONG).show();

    	}

        navDrawerItems = new ArrayList<NavDrawerItem>();
 
        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1), true, mydb.GetPrettyCountTabir("")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1), true, ""));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1), true, mydb.GetPrettyCountTabir("آ")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, mydb.GetPrettyCountTabir("ا")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1), true, mydb.GetPrettyCountTabir("ب")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, mydb.GetPrettyCountTabir("پ")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1), true, mydb.GetPrettyCountTabir("ت")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1), true, mydb.GetPrettyCountTabir("ث")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1), true, mydb.GetPrettyCountTabir("ج")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1), true, mydb.GetPrettyCountTabir("چ")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1), true, mydb.GetPrettyCountTabir("ح")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[11], navMenuIcons.getResourceId(11, -1), true, mydb.GetPrettyCountTabir("خ")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[12], navMenuIcons.getResourceId(12, -1), true, mydb.GetPrettyCountTabir("د")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[13], navMenuIcons.getResourceId(13, -1), true, mydb.GetPrettyCountTabir("ذ")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[14], navMenuIcons.getResourceId(14, -1), true, mydb.GetPrettyCountTabir("ر")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[15], navMenuIcons.getResourceId(15, -1), true, mydb.GetPrettyCountTabir("ز")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[16], navMenuIcons.getResourceId(16, -1), true, mydb.GetPrettyCountTabir("ژ")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[17], navMenuIcons.getResourceId(17, -1), true, mydb.GetPrettyCountTabir("س")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[18], navMenuIcons.getResourceId(18, -1), true, mydb.GetPrettyCountTabir("ش")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[19], navMenuIcons.getResourceId(19, -1), true, mydb.GetPrettyCountTabir("ص")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[20], navMenuIcons.getResourceId(20, -1), true, mydb.GetPrettyCountTabir("ض")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[21], navMenuIcons.getResourceId(21, -1), true, mydb.GetPrettyCountTabir("ط")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[22], navMenuIcons.getResourceId(22, -1), true, mydb.GetPrettyCountTabir("ظ")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[23], navMenuIcons.getResourceId(23, -1), true, mydb.GetPrettyCountTabir("ع")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[24], navMenuIcons.getResourceId(24, -1), true, mydb.GetPrettyCountTabir("غ")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[25], navMenuIcons.getResourceId(25, -1), true, mydb.GetPrettyCountTabir("ف")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[26], navMenuIcons.getResourceId(26, -1), true, mydb.GetPrettyCountTabir("ق")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[27], navMenuIcons.getResourceId(27, -1), true, mydb.GetPrettyCountTabir("ک")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[28], navMenuIcons.getResourceId(28, -1), true, mydb.GetPrettyCountTabir("گ")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[29], navMenuIcons.getResourceId(29, -1), true, mydb.GetPrettyCountTabir("ل")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[30], navMenuIcons.getResourceId(30, -1), true, mydb.GetPrettyCountTabir("م")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[31], navMenuIcons.getResourceId(31, -1), true, mydb.GetPrettyCountTabir("ن")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[32], navMenuIcons.getResourceId(32, -1), true, mydb.GetPrettyCountTabir("و")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[33], navMenuIcons.getResourceId(33, -1), true, mydb.GetPrettyCountTabir("ه")));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[34], navMenuIcons.getResourceId(34, -1), true, mydb.GetPrettyCountTabir("ی")));
         
 
        // Recycle the typed array
        navMenuIcons.recycle();
        //mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
 
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
 
        // enabling action bar app icon and behaving it as toggle button
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);
 
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
				super.onDrawerOpened(view);
            }
 
            public void onDrawerOpened(View drawerView) {
				super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
 
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }        
        

        
        obj = (ListView)findViewById(R.id.lvTabirs);
        obj.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
            long id) {
            	
            	try
            	{
            	String strCode = ((TextView) view.findViewById(R.id.Code_entry)).getText().toString();
                    
                // TODO Auto-generated method stub
                //int Code_To_Search = position + 1;
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("code", Integer.parseInt(strCode) );
                Intent intent = new Intent(getApplicationContext(),DisplayTabir.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                
                
            	}
            	catch(Exception err1)
            	{
                    Toast.makeText(getApplicationContext(), err1.getMessage(),
                    Toast.LENGTH_LONG).show();
            		
            	}
                //Toast.makeText(getApplicationContext(), "CLICKED", Toast.LENGTH_SHORT).show();
            }
            });
        
        mDrawerList.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
            long id) {
            	
            	displayView(position);
            }
            });    
        
        


    }
     
    private void ShowFavorites()
    {
    	SharedPreferences sharedPref = mContext.getSharedPreferences(getString(R.string.tabir_app_file_key), Context.MODE_PRIVATE);
    	String FavCodes = sharedPref.getString(getString(R.string.Favorite_Item_list), "");
    	
    	String[] columns = new String[] { "_id", "Word", "Meaning" };
    	int[] to = new int[] { R.id.Code_entry, R.id.Word_entry, R.id.ShortMeaning_entry};
    	
    	
    	
        Cursor c = mydb.SearchTabirsByCodes(FavCodes);

        obj = (ListView)findViewById(R.id.lvTabirs);
        
        CustomTabirAdapter cAdapter = new CustomTabirAdapter(this, R.layout.list_entry, c, columns, to);
        obj.setAdapter(cAdapter);    
        
        
        
        Toast.makeText(getApplicationContext(), c.getCount() + " نتیجه پیدا شد. ",
        Toast.LENGTH_LONG).show();
        
    }    

    private void BindList(String Keyword)
    {
    	String[] columns = new String[] { "_id", "Word", "Meaning" };
    	int[] to = new int[] { R.id.Code_entry, R.id.Word_entry, R.id.ShortMeaning_entry};
    	
        Cursor c = mydb.SearchTabirs(Keyword);

        obj = (ListView)findViewById(R.id.lvTabirs);
        
        CustomTabirAdapter cAdapter = new CustomTabirAdapter(this, R.layout.list_entry, c, columns, to);
        obj.setAdapter(cAdapter);    
        
        
        
        Toast.makeText(getApplicationContext(), c.getCount() + " نتیجه پیدا شد. ",
        Toast.LENGTH_LONG).show();
        
    }
    
     /**
     * Displaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
 
   
        
    	try
    	{
	        mydb = new DBHelper(this);
        	try
        	{
	        	getApplicationContext().deleteDatabase("TabirDB");
	        	mydb.createDataBase();
	        	
	            mDrawerList.setItemChecked(position, true);
	            mDrawerList.setSelection(position);
	            if(position >= 2)
	            	setTitle("تعابیری که با " + navMenuTitles[position] + " شروع می شوند ");
	            mDrawerLayout.closeDrawer(mDrawerList);
	        	
	            switch (position) {
	            case 0:
	            	BindList("%%");
	                break;
	            case 1:
	            	ShowFavorites();
	                break;
	            default:
	            	BindList(navMenuTitles[position] + "%");
	                break;
	            }
        	}
        	catch(IOException err2)
        	{
                Toast.makeText(getApplicationContext(), err2.getMessage(),
                Toast.LENGTH_LONG).show();

        	}
        	

            


	        
    	}
    	catch(Exception err3)
    	{
            Toast.makeText(getApplicationContext(), err3.getMessage(),
            Toast.LENGTH_LONG).show();

    	}        
        
        
        

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
}
