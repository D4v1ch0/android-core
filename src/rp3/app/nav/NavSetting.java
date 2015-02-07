package rp3.app.nav;

import java.util.List;

import rp3.app.NavActivity;


public interface NavSetting {
	
	void navConfig(List<NavItem> navItems, NavActivity currentActivity);
	void onNavItemSelected(NavItem item);	
	
}
