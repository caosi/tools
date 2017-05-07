package com.joy.util.ssh.sample;

import com.joy.util.ssh.core.ConnBean;
import com.joy.util.ssh.core.Result;
import com.joy.util.ssh.core.SSHExec;
import com.joy.util.ssh.exception.TaskExecFailException;
import com.joy.util.ssh.task.CustomTask;
import com.joy.util.ssh.task.impl.ExecCommand;

public class Sample001 {

	public static void main(String[] args) {
		SSHExec ssh = null;
		try {
			ConnBean cb = new ConnBean("rfidic-1.svl.ibm.com", "tsadmin","u7i8o9p0");
			ssh = SSHExec.getInstance(cb);		
			CustomTask echo = new ExecCommand("echo 123");
			ssh.connect();
			Result res = ssh.exec(echo);
			if (res.isSuccess)
			{
				System.out.println("Return code: " + res.rc);
				System.out.println("sysout: " + res.sysout);
			}
			else
			{
				System.out.println("Return code: " + res.rc);
				System.out.println("error message: " + res.error_msg);
			}
		} catch (TaskExecFailException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			ssh.disconnect();	
		}
	}

}
