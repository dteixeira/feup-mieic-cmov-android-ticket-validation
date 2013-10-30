package org.cmov.ticketclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

public class TicketBuyFragment extends Fragment {
	
	public View.OnClickListener clickListener = null;
	
	public TicketBuyFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_buy_tickets, container, false);
		setupSpinners(root);
		root.findViewById(R.id.buy_button).setOnClickListener(clickListener);
		return root;
	}
	
	private void setupSpinners(View root) {
		NumberPicker np15 = (NumberPicker) root.findViewById(R.id.ticket_15_picker);
		np15.setMaxValue(10);
		np15.setMinValue(0);
		np15.setValue(0);
		np15.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		NumberPicker np30 = (NumberPicker) root.findViewById(R.id.ticket_30_picker);
		np30.setMaxValue(10);
		np30.setMinValue(0);
		np30.setValue(0);
		np30.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		NumberPicker np60 = (NumberPicker) root.findViewById(R.id.ticket_60_picker);
		np60.setMaxValue(10);
		np60.setMinValue(0);
		np60.setValue(0);
		np60.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
	}

}
