package com.girmiti.skybandecr.fragment.connectsetting;

import androidx.lifecycle.ViewModel;

import com.girmiti.skybandecr.sdk.SocketHostConnector;

import java.io.IOException;

public class ConnectSettingViewModel extends ViewModel {

    private SocketHostConnector socketHostConnector;
    private String socketConnectionResponse;

    public boolean getConnectResponse(String ipAddress, int portNo) throws IOException {

        socketHostConnector = new SocketHostConnector(ipAddress, portNo);
        socketConnectionResponse = socketHostConnector.createConnection();
        return true;
    }

    public void disConnectSocket() throws IOException {

        if ( socketHostConnector !=null && socketHostConnector.socket.isConnected() ) {

            socketHostConnector.cleanup();
        }
    }
}
