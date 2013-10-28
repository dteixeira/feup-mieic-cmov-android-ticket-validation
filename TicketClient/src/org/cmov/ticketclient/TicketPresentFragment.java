package org.cmov.ticketclient;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.jwetherell.quick_response_code.data.Contents;
import com.jwetherell.quick_response_code.qrcode.QRCodeEncoder;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TicketPresentFragment extends Fragment {
	
	public Ticket ticket = null;
	
	public TicketPresentFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_present_ticket, container, false);
		if(ticket != null && ticket.getId() != -1) {
			ImageView imageView = (ImageView) root.findViewById(R.id.qr_image_view);
			JSONObject json = new JSONObject();
			try {
				json.put("bus", BluetoothAdapter.getDefaultAdapter().getAddress());
			} catch (JSONException e1) {}
	    	QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(
	    			json.toString(), 
	    			null, 
	    			Contents.Type.TEXT, 
	    			BarcodeFormat.QR_CODE.toString(), 
	    			500);
	    	Bitmap bitmap = null;
			try {
				bitmap = qrCodeEncoder.encodeAsBitmap();
			} catch (WriterException e) {}
	        imageView.setImageBitmap(bitmap);
		}
		return root;
	}
	
	public void updateQRImage(Ticket ticket) {
		if(ticket.getId() != -1) {
			View view = getView();
			if(view != null) {
				ImageView imageView = (ImageView) view.findViewById(R.id.qr_image_view);
				JSONObject json = new JSONObject();
				try {
					json.put("bus", BluetoothAdapter.getDefaultAdapter().getAddress());
				} catch (JSONException e1) {}
		    	QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(
		    			json.toString(), 
		    			null, 
		    			Contents.Type.TEXT, 
		    			BarcodeFormat.QR_CODE.toString(), 
		    			500);
		    	Bitmap bitmap = null;
				try {
					bitmap = qrCodeEncoder.encodeAsBitmap();
				} catch (WriterException e) {}
		        imageView.setImageBitmap(bitmap);
			}
		}
	}

}
