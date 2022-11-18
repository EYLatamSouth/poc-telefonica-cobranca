package Helpers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;


/*####################################################################
 *          Essa Classe Foi inspirada no tutorial do site:           *
 *  http://www.devmedia.com.br/desenvolvendo-um-cliente-ftp/3547     *
 *####################################################################
*/

public class FTP {
	
	private String servidor;
	private String usuario;
	private String senha;
	private FTPClient ftpClient;
			
	/**
	 * Método construtor da classe TdvFTP.
	 * @author Felipe Santaniello
	 * @date 15/12/2015  
	 * @param servidorFTP String - IP do servidor FTP.
	 * @param usuario String - Usuário do servidor FTP.
	 * @param senha String - Senha do servidor FTP.
	 * @param FTPClient FTPClient - FTPClient.
	 *
	 * */	
	
	public FTP(String servidorFTP, String usuario, String senha, FTPClient ftpClient) {
		this.servidor = servidorFTP;
		this.usuario = usuario;
		this.senha = senha;
		this.ftpClient = ftpClient;
	}
	
	/**
	 * Método que realiza a conexão com o FTP.
	 * @author Felipe Santaniello
	 * @data 15/12/2015  
	 * @throws SocketException
	 * @throws IOException
	 * @return boolean - Retorna true se a conexão foi realizada com sucesso e false caso contrário.
	 * 
	 * */	
	public boolean connect() throws SocketException, IOException {
		
		 ftpClient.connect(this.servidor, 22);  
	     //verifica se conectou com sucesso!  
	     if( FTPReply.isPositiveCompletion( ftpClient.getReplyCode() ) ) {  
	         ftpClient.login(this.usuario, this.senha );  
	     } else {  
	     //erro ao se conectar  
	         disconnectFTP();
	         System.out.println("Conexão recusada");  
	         System.exit(1);  
	         return false;
	     }
	    return true;    
	}
	
	
	/**
	 * Método que retorna apenas uma lista com o nome dos diretórios e arquivos do FTP.
	 * @author Felipe Santaniello
	 * @data 15/12/2015  
	 * @param  diretorio String - Nome do diretório a ser listado.
	 * @throws SocketException
	 * @throws IOException
	 * @return String[] - Retorna uma lista de Strings com o nome dos arquivos e diretórios contidos no diretório informado.
	 * 
	 * */	
	public String[] getNameDirs(String diretorio) throws SocketException, IOException {
	    String[] nameDirs = null;
		try {
			connect();
			ftpClient.enterLocalPassiveMode();  
		    ftpClient.changeWorkingDirectory(diretorio);  
			nameDirs = ftpClient.listNames();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			disconnectFTP();
		}		
		return nameDirs;
	}
	
	/**
	 * 
	 * Método que devolve diversas propriedades dos arquivos e diretórios do FTP tais como: permissões, tamanho dos arquivos e diretórios e etc... É mais completo que o getNameDirs.
	 * @author Felipe Santaniello
	 * @data 15/12/2015  
	 * @param  diretorio String - Nome do diretório a ser listado.
	 * @throws SocketException
	 * @throws IOException
	 * @return FTPFile[] - Retorna uma lista do tipo FTPFile com o nome dos arquivos e diretórios contidos no diretório informado.
	 * 
	 * */		
	public FTPFile[] getConfigFTPFiles(String diretorio) {				
		FTPFile[] filesConfig = null;
	     try {
	    	connect(); 
	    	ftpClient.enterLocalPassiveMode();   
			ftpClient.changeWorkingDirectory(diretorio);
			filesConfig = ftpClient.listFiles();  
	     } catch (IOException e) {
		    	e.printStackTrace(); 
		}finally {
			disconnectFTP();
		}		 		
		return filesConfig;
	}
	
	
	/**
	 * Envia um arquivo para o servidor FTP.
	 * @author Felipe Santaniello
	 * @data 15/12/2015  
	 * @description Envia um arquivo para o servidor FTP.
	 * @param caminhoArquivo String - Caminho aonde está localizado o arquivo a ser enviado.
	 * @param  arquivo String - Nome do arquivo.
	 * @throws IOException
	 * @return boolean - Retorna true se o arquivo foi enviado e false caso contrário.
	 * 
	 * */
	public boolean sendFTPFile(String caminhoArquivo, String arquivo) throws IOException {
		FileInputStream arqEnviar = null;
		try {
		    connect();
			ftpClient.enterLocalPassiveMode();   
			arqEnviar = new FileInputStream(caminhoArquivo);
	        if (ftpClient.storeFile(arquivo, arqEnviar)) {
	            return true;
	        }
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			arqEnviar.close();
			disconnectFTP();
		}   
      return false;
	}
	
	
	/**
	 * Obtém um arquivo do servidor FTP.
	 * @author Felipe Santaniello
	 * @data 15/12/2015  
	 * @description Obtém um arquivo do servidor FTP.
	 * @param  arquivo String - Nome do arquivo a ser baixado.
	 * @return void 
	 * 
	 * */
	public void getFile(String arquivo) {
		try {
			connect();
			ftpClient.enterLocalPassiveMode(); 
			ftpClient.setFileType( FTPClient.BINARY_FILE_TYPE );
			OutputStream os = new FileOutputStream(arquivo);  
			ftpClient.retrieveFile(arquivo, os );  
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			disconnectFTP();
		}   		
	}
	
	/**
	 * Fecha a conexão com o FTP.
	 * @author Felipe Santaniello
	 * @data 15/12/2015  
	 * @description Faz a desconexão com o FTP.
	 * @return void 
	 * 
	 * */
	public void disconnectFTP() {
		try {
			this.ftpClient.logout();
			this.ftpClient.disconnect();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}

/*
    Dependências Maven
		<dependency>
			<groupId>commons-net</groupId>
			<artifactId>commons-net</artifactId>
			<version>3.3</version>
		</dependency>
		<dependency>
			<groupId>oro</groupId>
			<artifactId>oro</artifactId>
			<version>2.0.8</version>
		</dependency>
*/
