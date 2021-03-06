package org.cmov.ticketclient;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TicketUnusedFragment extends ListFragment {
	
	public TicketUnusedFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_unused_tickets, container, false);
		return root;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		((MainActivity) getActivity()).onUnusedTicketClick(position);
	}

}
