package rp3.data.entity;

import rp3.data.Message;
import rp3.data.MessageCollection;


public interface OnEntityCheckerListener<T> {
	
	public void onEntityValidationFailed(MessageCollection m, T e);
	public void onEntityItemValidationFailed(Message message, T e);	
	public void onEntityValidationSuccess(T e);

}
