package rp3.app.nav;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

public class NavItem {
	private String title;
	private int id;
	private boolean isCategory;
    private int icon;
    private int resTitleId;
    private String count = "0";
    private ArrayList<NavItem> navItems;
    private Context context;
    
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;
     
    public NavItem(){}
 
    
    public NavItem(int id,String title, int icon){
    	this.setId(id);
        this.title = title;
        this.icon = icon;
    }
    
    public NavItem(int id,String title, int icon, boolean isCategory){
    	this.setId(id);
        this.title = title;
        this.isCategory = isCategory;
        this.icon = icon;
    }       
     
    public NavItem(int id,String title, int icon,boolean isCategory,boolean isCounterVisible, String count){
    	this.id = id;
    	this.isCategory = isCategory;
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
    
    public NavItem(int id,int resTitleId, int icon, boolean isCategory){
    	this.setId(id);
    	this.setResTitleId(resTitleId);
        this.isCategory = isCategory;
        this.icon = icon;
    }
     
    public NavItem(int id,int resTitleId, int icon,boolean isCategory,boolean isCounterVisible, String count){
    	this.id = id;
    	this.isCategory = isCategory;
        this.setResTitleId(resTitleId);
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }
     
    public String getTitle(){
    	if(TextUtils.isEmpty(title) && context!=null ){
    		setTitle( context.getText(getResTitleId()).toString() );
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

	public boolean isCategory() {
		return isCategory;
	}

	public void setCategory(boolean isCategory) {
		this.isCategory = isCategory;
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
