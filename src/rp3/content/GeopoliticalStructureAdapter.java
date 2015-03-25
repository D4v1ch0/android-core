package rp3.content;

import java.util.ArrayList;
import java.util.List;

import rp3.core.R;
import rp3.data.models.GeopoliticalStructure;
import rp3.db.sqlite.DataBase;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

public class GeopoliticalStructureAdapter extends ArrayAdapter<GeopoliticalStructure> {
    private final String MY_DEBUG_TAG = "GeopoliticalStructureAdapter";
    private List<GeopoliticalStructure> items;
    private List<GeopoliticalStructure> suggestions;
    private int viewResourceId;
	private DataBase db;

    public GeopoliticalStructureAdapter(Context context, DataBase db) {
        super(context, R.layout.base_rowlist_geopolitical, new ArrayList<GeopoliticalStructure>());
        this.db = db;
        this.items = new ArrayList<GeopoliticalStructure>();
        this.suggestions = new ArrayList<GeopoliticalStructure>();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.base_rowlist_geopolitical, null);
        }
        GeopoliticalStructure geo = getItem(position);
        if (geo != null) {
            TextView geopoliticalNameLabel = (TextView) v.findViewById(R.id.base_geopolitical_name);
            if (geopoliticalNameLabel != null) {
            	geopoliticalNameLabel.setText(geo.getName());
            }
            TextView parentNameLabel = (TextView) v.findViewById(R.id.base_geopolitical_parents);
            if (geopoliticalNameLabel != null) {
            	parentNameLabel.setText(geo.getParents());
            }
        }
        return v;
    }

	@Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((GeopoliticalStructure)(resultValue)).getName(); 
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                suggestions = GeopoliticalStructure.getGeopoliticalStructureSearch(db, constraint.toString());
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<GeopoliticalStructure> filteredList = (ArrayList<GeopoliticalStructure>) results.values;
            try {
                if (results != null && results.count > 0) {
                    clear();
                    for (GeopoliticalStructure c : filteredList) {
                        add(c);
                    }
                    notifyDataSetChanged();
                }
            }
            catch (Exception ex)
            {}
        }
    };
}
