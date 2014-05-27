package rp3.app.nav;

import java.util.ArrayList;
import java.util.List;

import rp3.runtime.Session;

import android.text.TextUtils;

public class NavItem {
	
	public static final int TYPE_CATEGORY = 2;
	public static final int TYPE_NAV = 0;
	public static final int TYPE_ACTION = 1;
	
	private String title;
	private int id;	
    private int icon;
    private int resTitleId;
    private String count = "0";
    private int itemType = TYPE_NAV;
    private ArrayList<NavItem> navItems;    
    
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;
     
    public NavItem(){}
 
    
    public NavItem(int id,String title, int icon){
    	this.setId(id);
        this.title = title;
        this.icon = icon;
    }
    
    public NavItem(int id,String title, int icon, int navItemType){
    	this.setId(id);
    	this.itemType = navItemType;
        this.title = title;        
        this.icon = icon;
    }       
     
    public NavItem(int id,String title, int icon,int navItemType,boolean isCounterVisible, String count){
    	this.id = id;
    	this.itemType = navItemType;
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }
    
    public NavItem(int id,int resTitleId, int icon){
    	this.setId(id);
        this.setResTitleId(resTitleId);
        this.icon = icon;
    }
    
    public NavItem(int id,int resTitleId, int icon, int navItemType){
    	this.setId(id);
    	this.setResTitleId(resTitleId);
    	this.itemType = navItemType;
        this.icon = icon;
    }
     
    public NavItem(int id,int resTitleId, int icon,int navItemType,boolean isCounterVisible, String count){
    	this.id = id;
    	this.itemType = navItemType;
        this.setResTitleId(resTitleId);
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }
     
    public String getTitle(){
    	if(TextUtils.isEmpty(title)){
    		setTitle( Session.getContext().getText(getResTitleId()).toString() );
    	}
        return this.title;
    }
     
    public int getIcon(){
        return this.icon;
    }
     
    public String getCount(){
        return this.count;
    }
     
    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }
     
    public void setTitle(String title){
        this.title = title;
    }
     
    public void setIcon(int icon){
        this.icon = icon;
    }
     
    public void setCount(String count){
        this.count = count;
    }
     
    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }

	public int getNavItemType() {
		return itemType;
	}

	public void setNavItemType(int navItemType) {
		this.itemType = navItemType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void addChildItem(NavItem item){
		getNavItems().add(item);
	}
	
	public void removeChildItem(NavItem item){
		getNavItems().remove(item);
	}
	
	public void clearChildItems(){
		getNavItems().clear();
	}
	
	public List<NavItem> getNavItems(){
		if(navItems == null) navItems = new ArrayList<NavItem>();
		return navItems;
	}


	public int getResTitleId() {
		return resTitleId;
	}


	public void setResTitleId(int resTitleId) {
		this.resTitleId = resTitleId;
	}
}
