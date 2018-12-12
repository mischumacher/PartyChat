package partychat.mainapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * A BroadcastReceiver that notifies of important wifi p2p events.
 */
public class WifiReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private Channel channel;
    private ChatroomList activity;
    private static List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    private  PeerListListener peerListListener = new PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {

            List<WifiP2pDevice> refreshedPeers = new ArrayList<>(peerList.getDeviceList());
            if (!refreshedPeers.equals(peers)) {
                peers.clear();
                peers.addAll(refreshedPeers);


            }

            if (peers.size() == 0) {
                Log.d("WIFIReceiver", "No devices found");
                return;
            }
        }
    };

    public WifiReceiver(WifiP2pManager manager, Channel channel,
                        ChatroomList activity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }

    public static List<WifiP2pDevice> getPeers(){
        return peers;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // UI update to indicate wifi p2p status.
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d("wifi", "onsuccess");
                }

                @Override
                public void onFailure(int reasonCode) {
                    Log.d("wifi", "onfailure");
                }
            });
            if (manager != null) {
                manager.requestPeers(channel, peerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            activity.resetData();
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        }
    }
}
