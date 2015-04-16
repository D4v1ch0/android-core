package rp3.content;

import java.util.ArrayList;
import java.util.List;

import rp3.core.R;
import rp3.data.models.GeopoliticalStructure;
import rp3.db.sqlite.DataBase;
import rp3.util.ConnectionUtils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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
    private GeopoliticalStructure selected;
    private int viewResourceId;
	private DataBase db;

    public GeopoliticalStructureAdapter(Context context, DataBase db) {
        super(context, R.layout.base_rowlist_geopolitical, new ArrayList<GeopoliticalStructure>());
        this.db = db;
        this.items = new ArrayList<GeopoliticalStructure>();
        this.suggestions = new ArrayList<GeopoliticalStructure>();
    }

    public List<GeopoliticalStructure> getItems() {
        return items;
    }

    public GeopoliticalStructure getSelected(String text)
    {
        try {
            for (GeopoliticalStructure geo : suggestions) {
                if (geo.getParents().equalsIgnoreCase(text))
                    return geo;
            }
        }
        catch (Exception ex)
        {

        }
        return null;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final GeopoliticalStructure geo = getItem(position);
        if(geo.getName().equalsIgnoreCase("Cargando"))
        {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.base_rowlist_loading, null);
                v.setClickable(false);
                v.setFocusable(false);
                v.setEnabled(false);
                v.setOnClickListener(null);
        }
        else {
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.base_rowlist_geopolitical, null);
            }
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
            String str = ((GeopoliticalStructure)(resultValue)).getParents();
            return str;
        }

        @Override
        protected FilterResults performFiltering(final CharSequence constraint) {
            if(constraint != null && ConnectionUtils.isNetAvailable(getContext())) {
                suggestions.clear();
                final GeopoliticalStructure loading = new GeopoliticalStructure();
                loading.setName("Cargando");
                suggestions.add(loading);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                            suggestions = rp3.sync.GeopoliticalStructure.executeSearch(constraint.toString());
                            Activity actv = (Activity)getContext();
                            actv.runOnUiThread(new Runnable()
                            {

                                @Override
                                public void run() {
                                    try {
                                        FilterResults filterResults = new FilterResults();
                                        filterResults.values = suggestions;
                                        filterResults.count = suggestions.size();
                                        notifyDataSetInvalidated();
                                        if(suggestions.size() == 0)
                                            publishResults(constraint, null);
                                        else
                                            publishResults(constraint, filterResults);
                                    }catch (Exception ex)
                                    {
                                        clear();
                                        publishResults(constraint, null);
                                    }

                                }

                            });
                    }
                };
                new Thread(runnable).start();
                //suggestions = GeopoliticalStructure.getGeopoliticalStructureSearch(db, constraint.toString());
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
            try {
                if (results != null && results.count > 0) {
                    ArrayList<GeopoliticalStructure> filteredList = (ArrayList<GeopoliticalStructure>) results.values;
                    clear();
                    for (GeopoliticalStructure c : filteredList) {
                        add(c);
                    }
                }
                else
                {
                    clear();
                    notifyDataSetInvalidated();
                }
            }
            catch (Exception ex)
            {}
            notifyDataSetChanged();
        }
    };
}
