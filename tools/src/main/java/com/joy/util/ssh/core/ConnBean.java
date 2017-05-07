package com.joy.util.ssh.core;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.joy.util.string.StringUtil;

/**
 * This is a Javabean class that used to construct a connection. It stores host, usernamd and password for a connection.
 * <br>
 * The three parameters has getter and setter method.
 * 
 *
 */
public class ConnBean {

	private String host;
	
	private String user;
	
	private String password;
	

	private String privateKey;

	private String passphrase;

	public ConnBean(String host, String user, String password){
		this.host = host;
		this.password = password;
		this.user = user;
	}

	public ConnBean(String host, String user, String privateKey,String passphrase){
		this.host = host;
		this.user = user;
		this.privateKey = privateKey;
		this.passphrase = passphrase;
	}
	
	public void secure(JSch jsch,Session session) throws JSchException{
		if (!StringUtil.isEmpty(password)) {
			session.setPassword(password);
		} else {
			// 设置密钥和密码
			if (!StringUtil.isEmpty(privateKey)) {
				if (!StringUtil.isEmpty(passphrase)) {
					// 设置带口令的密钥
					jsch.addIdentity(privateKey, passphrase);
				} else {
					// 设置不带口令的密钥
					jsch.addIdentity(privateKey);
				}
			}
		}
		UserInfo userInfo = new ConnCredential();
		session.setUserInfo(userInfo);
		// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassphrase() {
		return passphrase;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
}

