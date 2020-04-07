package com.girmiti.skybandecr.fragment.connectsetting;

import androidx.lifecycle.ViewModel;

import com.girmiti.skybandecr.fragment.home.HomeViewModel;
import com.girmiti.skybandecr.sdk.SocketHostConnector;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectSettingViewModel extends ViewModel {

    private static SocketHostConnector socketHostConnector;
    private Logger logger = Logger.getNewLogger(ConnectSettingViewModel.class.getName());

    public static SocketHostConnector getSocketHostConnector() {
        return socketHostConnector;
    }

    public boolean getConnection(String ipAddress, int portNo) throws IOException {

        socketHostConnector = new SocketHostConnector(ipAddress, portNo);
        socketHostConnector.createConnection();

        return true;
    }

    public void disConnectSocket() throws IOException {

        if ( socketHostConnector !=null && socketHostConnector.getSocket().isConnected() ) {

            socketHostConnector.cleanup();
            socketHostConnector = null;
        }
    }

    public boolean validateIp(String ipAddress) {

        final Pattern IP_ADDRESS
                = Pattern.compile(
                "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                        + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                        + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                        + "|[1-9][0-9]|[0-9]))");
        Matcher matcher = IP_ADDRESS.matcher(ipAddress);

        return  matcher.matches();
    }

    public boolean validatePort(String portNo) {

        Pattern pattern;
        Matcher matcher;
        String PORT="^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
        pattern = Pattern.compile(PORT);
        matcher = pattern.matcher(portNo);

        return matcher.matches();
    }
}
