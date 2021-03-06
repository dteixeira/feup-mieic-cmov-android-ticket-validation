package org.cmov.validationterminal.bluetooth;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

public class BluetoothHelper {
	
	/**
	 * Checks if the bluetooth adapter is on and ready.
	 */
	public static boolean isBluetoothAvailable() {
		BluetoothAdapter tempBtAdapter = BluetoothAdapter.getDefaultAdapter();
		return tempBtAdapter != null && tempBtAdapter.isEnabled();
	}
	
	/**
	 * Sends an object through bluetooth.
	 */
	public static boolean bluetoothWriteObject(Object object, BluetoothSocket socket) {
		try {
			ObjectOutputStream oOS = new ObjectOutputStream(socket.getOutputStream());
			oOS.writeObject(object);
			oOS.flush();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Receives an object through bluetooth.
	 */
	public static Object bluetoothReadObject(BluetoothSocket socket) {
		ObjectInputStream oIS;
		try {
			oIS = new ObjectInputStream(socket.getInputStream());
			Object object = oIS.readObject();
			return object;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Enable the bluetooth adapter.
	 */
	public static void bluetoothEnable(Activity activity, boolean discoverable, int reqCode) {
		if(discoverable) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
			activity.startActivityForResult(discoverableIntent, reqCode);
		} else {
			Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			activity.startActivityForResult(bluetoothIntent, reqCode);
		}
	}

}
