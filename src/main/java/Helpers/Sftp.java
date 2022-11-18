package Helpers;

import java.io.IOException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Sftp {
	private String remoteHost = "bancos.blob.core.windows.net";
	private String username = "bancos.envio.telefonica";
	private String password = "Ia6GM3ceCT+HfXaN+39GUnjAO9n3xe9G";
	
	public void connect() throws JSchException, IOException {				
	    JSch jsch = new JSch();	    
	    //jsch.setKnownHosts("/Users/john/.ssh/known_hosts");
	    Session jschSession = jsch.getSession(username, remoteHost, 22);
	    jschSession.setPassword(password);
	    
	    try {
	    	jschSession.connect();	    	
	    	System.out.println("Connected");
	    }
	    catch(JSchException ex)
		{
			throw new IOException("Não foi possível conectar-se ao SFTP. Error: " + ex.getMessage());
		}
	    finally{
	    	jschSession.disconnect();
            System.out.println("Disconnected");
        }
	    
	    //return (ChannelSftp) jschSession.openChannel("sftp");
	}
	
	/*private ChannelSftp setupJsch() throws JSchException {
	    JSch jsch = new JSch();
	    jsch.setKnownHosts("/Users/john/.ssh/known_hosts");
	    Session jschSession = jsch.getSession(username, remoteHost);
	    jschSession.setPassword(password);
	    jschSession.connect();
	    return (ChannelSftp) jschSession.openChannel("sftp");
	}*/
	
	
}
