package rp3.widget;

import rp3.core.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class NumberPad extends FrameLayout {

	private int targetViewId;
	private Context context;	
	private TextView targetView;
	private int maxLength = 0;
	
	public NumberPad(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.NumberPad, 0, 0);

		try {
			targetViewId = a.getResourceId(R.styleable.NumberPad_targetView,0);			
			maxLength = a.getInteger(R.styleable.NumberPad_maxLength, 0);
		} finally {
			a.recycle();
		}
		
		initView();
	}

	private void initView(){
		View v = inflate(context, R.layout.layout_number_keypad, null);
		addView(v);			
		
		if(!isInEditMode()){
			setClickListener(findViewById(R.id.button_number_one));
			setClickListener(findViewById(R.id.button_number_two));
			setClickListener(findViewById(R.id.button_number_three));
			setClickListener(findViewById(R.id.button_number_for));
			setClickListener(findViewById(R.id.button_number_five));
			setClickListener(findViewById(R.id.button_number_six));
			setClickListener(findViewById(R.id.button_number_seven));
			setClickListener(findViewById(R.id.button_number_eight));		
			setClickListener(findViewById(R.id.button_number_nine));
			setClickListener(findViewById(R.id.button_number_zero));
			
			View discard = findViewById(R.id.action_discard);
			setClickListener(findViewById(R.id.action_discard));
			
			discard.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View arg0) {
					if(targetView!=null){
						targetView.setText("");
					}
					return true;
				}
			});
		}
	}
	
	private void setClickListener(View v){				
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(targetView!=null){
					int value = getNumber(view.getId());
					String text = targetView.getText().toString();
					if(value==-1){
						if(text != null && text.length()>0)
						text = text.substring(0, text.length()-1);
					}else{
						if(text==null) text = "";						
						text += String.valueOf(value);						
					}
					
					if(maxLength == 0 || text.length() <= maxLength)
						targetView.setText(text);
				}
			}
		});
	}
	
	public int getNumber(int id){
		int result = 0;
		if(id == R.id.button_number_one){
			result = 1;
		}else if(id== R.id.button_number_two){
			result = 2;
		}else if(id== R.id.button_number_three){
			result = 3;
		}else if(id== R.id.button_number_for){
			result = 4;
		}else if(id== R.id.button_number_five){
			result = 5;
		}else if(id== R.id.button_number_six){
			result = 6;
		}else if(id== R.id.button_number_seven){
			result = 7;
		}else if(id== R.id.button_number_eight){
			result = 8;
		}else if(id== R.id.button_number_nine){
			result = 9;
		}else if(id== R.id.button_number_zero){
			result = 0;
		}else if(id== R.id.action_discard){
			result = -1;
		}
		return result;
	}
	
	@Override
	protected void onAttachedToWindow() {		
		super.onAttachedToWindow();
		setTargetView(targetViewId);
	}
	
	public void setTargetView(int resId){
		targetViewId = resId;
		if(targetViewId != 0)
			//targetView = (TextView) ((Activity)this.context).getWindow().getDecorView().findViewById(targetViewId);
			targetView = (TextView) getRootView().findViewById(targetViewId);
		else
			targetView = null;
	}
	
	public int getTargetViewId(){
		return targetViewId;
	}
}
