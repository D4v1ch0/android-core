package rp3.app;

import java.util.ArrayList;
import java.util.List;

import rp3.app.nav.NavItem;
import rp3.app.nav.NavSetting;
import rp3.core.R;
import rp3.widget.adapter.NavListAdapter;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

@SuppressLint("NewApi")
public class NavActivity extends BaseActivity implements NavSetting {

	private final static String STATE_CURRENTNAV = "currentNav";
	
	private NavListAdapter navDrawerAdapter;
	private ListView listViewDrawer;
	private ArrayList<NavItem> navItems;
	private ArrayList<NavItem> resultNavItems;
	private NavSetting navSettingCallback;
	private int currentNavigationSelectionId = 0;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private DrawerLayout drawerLayout;
	private ArrayList<MenuItem> currentVisibleActionsMenu = new ArrayList<MenuItem>();
	private boolean isPaneFragmentLoaded = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_activity_navigation_drawer);		
		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	protected void onStart() {		
		super.onStart();
		if(navSettingCallback==null)
		{
			setNavSettingListener(this);
			if(!isPaneFragmentLoaded)
			{
				int sel = currentNavigationSelectionId;
				currentNavigationSelectionId = -99999999;
				setNavigationSelection(sel);
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
		
		outState.putInt(STATE_CURRENTNAV, currentNavigationSelectionId);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {		
		super.onRestoreInstanceState(savedInstanceState);
		
		currentNavigationSelectionId = savedInstanceState.getInt(STATE_CURRENTNAV);
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
			if (item.isCategory()) {
				resultNavItems.addAll(item.getNavItems());
			}
		}

		listViewDrawer = (ListView) findViewById(R.id.listView_navigationDrawer);
		navDrawerAdapter = new NavListAdapter(this, resultNavItems);
		listViewDrawer.setAdapter(navDrawerAdapter);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		setDrawerToggle();
		
		listViewDrawer.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				
				drawerLayout.closeDrawer(Gravity.LEFT);
				NavItem item = resultNavItems.get(position);
				
				//if(!NavActivity.this.navSettingCallback.equals(this)) onNavItemSelected(item);
				NavActivity.this.navSettingCallback.onNavItemSelected(item);				
			}
		});
	}
	
	public void onNavItemSelected(NavItem item) {		
	}

	public void setNavFragment(Fragment fragment, String title){
		getCurrentFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
		if(title!=null){
			this.setTitle(title);
		}
		isPaneFragmentLoaded = true;
	}
	
	public void setFragmentContent(Fragment fragment){
		setNavFragment(fragment, null);
	}
	
	private void setDrawerToggle() {
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(getTitle());
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(getTitle());
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
	}

	public void setNavigationSelection(int id) {
		if(currentNavigationSelectionId!=id){
			currentNavigationSelectionId = id;
			if (resultNavItems != null) {
				int position = currentNavigationSelectionId;
				for (NavItem item : resultNavItems) {
					if (item.getId() == id) {
						position = resultNavItems.indexOf(item);
						break;
					}
				}
	
				listViewDrawer.setSelection(position);
				listViewDrawer.setItemChecked(position, true);
				
				onNavItemSelected(resultNavItems.get(position));
			}
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {		
		// toggle nav drawer on selecting action bar app icon/title
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = drawerLayout.isDrawerOpen(listViewDrawer);
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
		actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void navConfig(List<NavItem> navItems, NavActivity currentActivity) {
		
	}
}
