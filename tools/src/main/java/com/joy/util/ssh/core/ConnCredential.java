package com.joy.util.ssh.core;

import com.jcraft.jsch.UserInfo;

/**
 * This is an implementation of UserInfo interface that provided by Jsch. It is used to process password for SSH connection. 
 * 
 *
 */
public class ConnCredential implements UserInfo {

	public boolean promptYesNo(String str) {
		if (str.contains("The authenticity of host")) {
			return true;
		}
		return false;
	}

	public String getPassphrase() {
		return null;
	}

	public boolean promptPassphrase(String message) {
		return false;
	}

	public boolean promptPassword(String message) {
		return false;
	}

	public void showMessage(String message) {
	}

	@Override
	public String getPassword() {
		return null;
	}
}

