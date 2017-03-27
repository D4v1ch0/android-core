package rp3.app;

import java.util.ArrayList;
import java.util.List;

import rp3.app.nav.NavItem;
import rp3.app.nav.NavSetting;
import rp3.core.R;
import rp3.util.Screen;
import rp3.util.ViewUtils;
import rp3.widget.adapter.NavListAdapter;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import rp3.widget.SlidingPaneLayout;
import rp3.widget.SlidingPaneLayout.PanelSlideListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

@SuppressLint("NewApi")
public class NavActivity extends BaseActivity implements NavSetting {

	private final static String STATE_CURRENTNAV = "currentNav";
	private final static String STATE_FRAGMENT_LOADED = "isPaneFragmentLoaded";
	private final static String STATE_TITLE = "activitytitle";

	public final static int NAV_MODE_DRAWER = 1;
	public final static int NAV_MODE_SLIDING_PANE = 2;
	private static final int PARALLAX_SIZE = 30;
	
	private NavListAdapter navDrawerAdapter;
	private ListView listViewDrawer;
	private View viewDrawer;
	private ViewGroup viewContentHeader;
	private ArrayList<NavItem> navItems;
	private ArrayList<NavItem> resultNavItems;
	private NavSetting navSettingCallback;
	private long currentNavigationSelectionId = 0;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private DrawerLayout drawerLayout;
	private SlidingPaneLayout slidingPane;
	private ArrayList<MenuItem> currentVisibleActionsMenu = new ArrayList<MenuItem>();
	private boolean isPaneFragmentLoaded = false;
	private boolean isCustomSetNavMode = false;
    private int navMode = NAV_MODE_DRAWER;
    private String currentTitle = null;
    private boolean isSlidingPanelOpen = false;

    public void setNavMode(int mode){
        if(mode == NAV_MODE_DRAWER || mode == NAV_MODE_SLIDING_PANE){
            navMode = mode;
            isCustomSetNavMode = true;
        }
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        if(!isCustomSetNavMode){
            if(Screen.isMinLargeLayoutSize(getApplicationContext())){
                navMode = NAV_MODE_SLIDING_PANE;
            }else {
                navMode = NAV_MODE_DRAWER;
            }
        }

		if(navMode == NAV_MODE_SLIDING_PANE){

			setContentView(R.layout.base_activity_navigation_sliding_pane);
			// SlidingPaneLayout customization
			slidingPane = (SlidingPaneLayout) findViewById(R.id.drawer_layout);
			slidingPane.setParallaxDistance(PARALLAX_SIZE);
			slidingPane.setShadowResource(R.drawable.sliding_pane_shadow);			
		}
		else{

			setContentView(R.layout.base_activity_navigation_drawer);
		}
		
		viewContentHeader = (ViewGroup)findViewById(R.id.nav_header);
		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setHomeButtonEnabled(true);
		
		if(savedInstanceState != null){
			currentNavigationSelectionId = savedInstanceState.getLong(STATE_CURRENTNAV);
			isPaneFragmentLoaded = savedInstanceState.getBoolean(STATE_FRAGMENT_LOADED);
            currentTitle = savedInstanceState.getString(STATE_TITLE);

            setTitle(currentTitle);
		}
	}



    @Override
	protected void onStart() {		
		super.onStart();
		if(navSettingCallback==null)
		{
			setNavSettingListener(this);
			if(!isPaneFragmentLoaded)
			{
				long sel = currentNavigationSelectionId;
				currentNavigationSelectionId = -999999999;
				setNavigationSelection(sel);
			}
		}
	}
	
	public void setSlindigEnabled(boolean enabled){
		if(navMode == NAV_MODE_DRAWER){
			if(!enabled)
				drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			else
				drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		}else{
			slidingPane.setSlidingEnabled(enabled);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
		
		outState.putLong(STATE_CURRENTNAV, currentNavigationSelectionId);
		outState.putBoolean(STATE_FRAGMENT_LOADED, isPaneFragmentLoaded);
        outState.putString(STATE_TITLE, currentTitle);

	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {		
		super.onRestoreInstanceState(savedInstanceState);	
		
		
	}
	
	public void setPaneContentView(int resID) {

		FrameLayout content = (FrameLayout) findViewById(R.id.content_frame);
		content.addView(getLayoutInflater().inflate(resID, null));

	}

	public NavSetting getNavSettingListener() {
		return navSettingCallback;
	}

	public void setNavSettingListener(NavSetting navSettingCallback) {
		this.navSettingCallback = navSettingCallback;
		navItems = new ArrayList<NavItem>();
		this.navSettingCallback.navConfig(navItems, this);

		resultNavItems = new ArrayList<NavItem>();
		for (NavItem item : navItems) {
			resultNavItems.add(item);
			if (item.getNavItemType() == NavItem.TYPE_CATEGORY) {
				resultNavItems.addAll(item.getNavItems());
			}
		}

		listViewDrawer = (ListView) findViewById(R.id.listView_navigationDrawer);
		navDrawerAdapter = new NavListAdapter(this, resultNavItems);
		listViewDrawer.setAdapter(navDrawerAdapter);		
		viewDrawer = findViewById(R.id.viewDrawer);
		
		if(navMode == NAV_MODE_DRAWER){
			drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			setDrawerToggle();
		}else{
			slidingPane = (SlidingPaneLayout) findViewById(R.id.drawer_layout);	
		}				
		 		
		
		listViewDrawer.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				
				if(navMode == NAV_MODE_DRAWER){
					drawerLayout.closeDrawer(Gravity.LEFT);
				}else{
					slidingPane.closePane();
				}
				
				NavItem item = resultNavItems.get(position);
				if(item.getNavItemType() == NavItem.TYPE_NAV){
					currentNavigationSelectionId = id;
				}else{										
					int beforePosition = getNavItemPosition(currentNavigationSelectionId);					
					listViewDrawer.setSelection(beforePosition);
					listViewDrawer.setItemChecked(beforePosition, true);
				}
				
				NavActivity.this.navSettingCallback.onNavItemSelected(item);				
			}
		});
	}
	
	public void onNavItemSelected(NavItem item) {		
	}

	public void setNavFragment(Fragment fragment, String title){
		getCurrentFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
		if(title!=null){
            currentTitle = title;
			this.setTitle(title);
		}
		isPaneFragmentLoaded = true;
	}
	
	public void setFragmentContent(Fragment fragment){
		setNavFragment(fragment, null);
	}
	
	private void setDrawerToggle() {
		if(navMode == NAV_MODE_DRAWER){
			actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
					R.drawable.ic_drawer, // nav menu toggle icon
					R.string.app_name, // nav drawer open - description for
										// accessibility
					R.string.app_name // nav drawer close - description for
										// accessibility
			) {
				public void onDrawerClosed(View view) {
					getActionBar().setTitle(currentTitle);
					// calling onPrepareOptionsMenu() to show action bar icons
					invalidateOptionsMenu();
				}
	
				public void onDrawerOpened(View drawerView) {
					getActionBar().setTitle(currentTitle);
					// calling onPrepareOptionsMenu() to hide action bar icons
					invalidateOptionsMenu();
				}
			};
			drawerLayout.setDrawerListener(actionBarDrawerToggle);
		}
		else{
			PanelSlideListener panelSlideListener = new  PanelSlideListener() {
				
				@Override
				public void onPanelSlide(View arg0, float arg1) {

				}
				
				@Override
				public void onPanelOpened(View arg0) {
                    isSlidingPanelOpen = true;

					getActionBar().setTitle(currentTitle);
                    invalidateOptionsMenu();
				}
				
				@Override
				public void onPanelClosed(View arg0) {
                    isSlidingPanelOpen = false;
					getActionBar().setTitle(currentTitle);
                    invalidateOptionsMenu();
				}
			};

			slidingPane.setPanelSlideListener(panelSlideListener);
		}
	}	
	
	public void reset(){
		long navGo = currentNavigationSelectionId;
		currentNavigationSelectionId = -999999999;
		setNavigationSelection(navGo);
	}

	public void setNavigationSelection(long sel) {
		if(currentNavigationSelectionId!=sel){
			currentNavigationSelectionId = sel;
			if (resultNavItems != null) {
				int position = 0;
						//currentNavigationSelectionId;
				position = getNavItemPosition(sel);
				
				NavItem item = resultNavItems.get(position);
				if(item.getNavItemType() == NavItem.TYPE_NAV){
					listViewDrawer.setSelection(position);
					listViewDrawer.setItemChecked(position, true);	
				}
																
				onNavItemSelected(item);								
			}
		}
	}
	
	private int getNavItemPosition(long id){
		int position = 0;
		for (NavItem item : resultNavItems) {
			if (item.getId() == id) {
				position = resultNavItems.indexOf(item);
				break;
			}
		}
		return position;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if(navMode ==  NAV_MODE_DRAWER){
	        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
		}
		else{
			switch (item.getItemId()) {
            case android.R.id.home:
                if (slidingPane.isOpen()) {
                	slidingPane.closePane();
                } else {
                	slidingPane.openPane();
                }
                return true;
        }
		}

		return super.onMenuItemSelected(featureId, item);
	}
	
	public boolean isNavOpen(){
		boolean drawerOpen = false;
		if(navMode == NAV_MODE_DRAWER)
			drawerOpen = drawerLayout.isDrawerOpen(viewDrawer);
		else
			drawerOpen  =  isSlidingPanelOpen;
		return drawerOpen;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = isNavOpen();
		
		if (!drawerOpen) {
			for (MenuItem item : currentVisibleActionsMenu)
				item.setVisible(true);
		} else {
			currentVisibleActionsMenu.clear();
			for (int i = 0; i < menu.size(); i++) {
				MenuItem item = menu.getItem(i);
				if (item.isVisible()) {
					currentVisibleActionsMenu.add(item);
					item.setVisible(false);
				}

			}
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		if(navMode == NAV_MODE_DRAWER)
			actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls

		if(navMode == NAV_MODE_DRAWER)
			actionBarDrawerToggle.onConfigurationChanged(newConfig);
        setTitle(currentTitle);
	}

	@Override
	public void navConfig(List<NavItem> navItems, NavActivity currentActivity) {
		
	}
	
	public void setNavHeaderTitle(int title){
		setNavHeaderTitle(getText(title).toString());
	}
	
	public void setNavHeaderTitle(String title){
		if(!TextUtils.isEmpty(title)){
			ViewUtils.setTextViewText(viewContentHeader,R.id.nav_header_title, title);
		}
	}
	
	public void setNavHeaderSubtitle(int subtitle){
		setNavHeaderSubtitle(getText(subtitle).toString());
	}
	
	public void setNavHeaderSubtitle(String subtitle){
		if(!TextUtils.isEmpty(subtitle)){
			ViewUtils.setViewVisibility(viewContentHeader,R.id.nav_header_subtitle, View.VISIBLE);
			ViewUtils.setTextViewText(viewContentHeader,R.id.nav_header_subtitle, subtitle);
		}else{
			ViewUtils.setTextViewText(viewContentHeader,R.id.nav_header_subtitle, "");
			ViewUtils.setViewVisibility(viewContentHeader,R.id.nav_header_subtitle, View.GONE);
		}
	}
	
	public void setNavHeaderIcon(int icon){
		ImageView imageView = (ImageView)viewContentHeader.findViewById(R.id.nav_header_icon);
		imageView.setImageResource(icon);
	}
	
	public void setNavHeaderIcon(Drawable icon){
		ImageView imageView = (ImageView)viewContentHeader.findViewById(R.id.nav_header_icon);
		imageView.setImageDrawable(icon);
	}
	
	public void setNavHeaderIcon(Bitmap icon){
		ImageView imageView = (ImageView)viewContentHeader.findViewById(R.id.nav_header_icon);
		imageView.setImageBitmap(icon);
	}
	
	public void setNavHeaderLayout(int layout){
		View viewInsert = getLayoutInflater().inflate(layout, null);
		viewContentHeader.removeAllViews();
		viewContentHeader.addView(viewInsert);
	}
	
	public void setNavHeaderViewText(int id, int value){
		ViewUtils.setTextViewText(viewContentHeader, id, getText(value).toString());
	}
	
	public void setNavHeaderViewText(int id, String value){
		ViewUtils.setTextViewText(viewContentHeader, id, value);
	}
	
	public void setNavHeaderViewIcon(int id, Drawable icon){
		ImageView imageView = (ImageView)viewContentHeader.findViewById(id);
		imageView.setImageDrawable(icon);
	}
	
	public void setNavHeaderViewIcon(int id, Bitmap icon){
		ImageView imageView = (ImageView)viewContentHeader.findViewById(id);
		imageView.setImageBitmap(icon);
	}
	
	public void showNavHeader(boolean show){
		if(show)
			viewContentHeader.findViewById(R.id.nav_header).setVisibility(View.VISIBLE);
		else
			viewContentHeader.findViewById(R.id.nav_header).setVisibility(View.GONE);
	}

	public void updateBadgeNavItem(int id, int badge)
	{
		navDrawerAdapter.getNavItemById(id).setBadge(badge);
		navDrawerAdapter.notifyDataSetChanged();
	}
}
