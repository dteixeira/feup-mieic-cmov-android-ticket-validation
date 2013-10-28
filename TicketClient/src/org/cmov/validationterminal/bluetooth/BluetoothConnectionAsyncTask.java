package org.cmov.validationterminal.bluetooth;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class BluetoothConnectionAsyncTask extends AsyncTask<Void, Void, Void> {
	
	/**
	 * Constants.
	 */
	private static final UUID SERVICE_UUID = UUID.fromString("1d49b2b0-3774-11e3-aa6e-0800200c9a66");
	private static final String TAG = "BluetoothConnectionAsyncTask";
	private static final int CONNECTION_CHECK_TIMEOUT = 350;
	private static final int CONNECTION_CHECK_TRIES = 20;
	private static final int CONNECTION_TRIES = 15;
	
	/**
	 * Instance variables.
	 */
	private String terminalAddress = null;
	private ProgressDialog progressDialog = null;
	private Activity callingActivity = null;
	private BluetoothSocket socket = null;
	private AtomicBoolean isConnected = null;
	private Object param = null;
	
	/**
	 * Creates a new async task instance.
	 */
	public BluetoothConnectionAsyncTask(BluetoothConnectionResultCallback activity, String macAddr, Object param) {
		this.socket = null;
		this.isConnected = new AtomicBoolean(false);
		this.terminalAddress = macAddr;
		this.callingActivity = (Activity) activity;
		this.progressDialog = new ProgressDialog(this.callingActivity);
		this.param = param;
	}
	
	/**
	 * Sets up a progress dialog.
	 */
	@Override
	protected void onPreExecute() {
		
		// Show a progress modal dialog.
		progressDialog.setMessage("Connecting to the terminal.");
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}

	/**
	 * Tries to find the remote device and establish a
	 * bluetooth connection with it.
	 */
	@Override
	protected Void doInBackground(Void... params) {
		
		// Try to establish a connection.
		int tries = 0;
		while(tries < CONNECTION_TRIES && !isConnected.get()) {
			
			// Should immediately stop when the socket cannot 
			// be created.
			try {
				BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(terminalAddress);
				socket = device.createInsecureRfcommSocketToServiceRecord(SERVICE_UUID);
				if(socket != null) {
					startConnection();
				}
				++tries;
			} catch (Exception e) {
				Log.e(TAG, "Problem getting bluetooth device.", e);
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Launches a new thread to execute the blocking call
	 * to connect(), while keeping a timeout counter to
	 * prevent hangs.
	 */
	private void startConnection() {
		
		// Try to connect with the remote device. Essential to
		// implement a timeout system, connect() hangs frequently;
		// new AsyncTask is needed for this purpose.
		isConnected.set(false);
		Thread connectThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Log.d(TAG, "Trying to connect to socket.");
					socket.connect();
					isConnected.set(true);
				} catch (Exception e) {
					Log.e(TAG, "Connection failed.", e);
				}
			}
		});
		Log.d(TAG, "Starting async connection try.");
		connectThread.start();
		
		// Wait for some time while the connection is not established. 
		// If too much times passes, the connection attempt if forcefully shut down.
		// Hangs at most CONNECTION_CHECK_TIMEOUT * CONNECTION_CHECK_TRIES.
		int retries = 0;
		while(retries < CONNECTION_CHECK_TRIES && !isConnected.get() && connectThread.isAlive()) {
			retries++;
			try {
				Log.d(TAG, "Connection try timeout.");
				Thread.sleep(CONNECTION_CHECK_TIMEOUT);
			} catch (InterruptedException e) {}
		}
		
		// Stops the connection attempt if it is hanging.
		if(!isConnected.get()) {
			Log.e(TAG, "Connection try timed out, cancelling task.");
			connectThread.interrupt();
			try {
				socket.getOutputStream().close();
				socket.getInputStream().close();
				socket.close();
			} catch(Exception e) {
			} finally {
				socket = null;
			}
		} else {
			Log.d(TAG, "Connection established.");
		}
	}
	
	/**
	 * Closes the progress dialog and informs the activity that
	 * the task ended.
	 */
	@Override
	protected void onPostExecute(Void result) {
		
		// Dismiss the progress dialog.
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
        }
		
		// Callback the task executor.
		Log.d(TAG, "Invoking result callback.");
		((BluetoothConnectionResultCallback) callingActivity).onConnectionResult(isConnected.get(), socket, param);
	}

}
