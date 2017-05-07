package com.joy.util.ssh.sample;

import com.joy.util.ssh.core.ConnBean;
import com.joy.util.ssh.core.IOptionName;
import com.joy.util.ssh.core.SSHExec;
import com.joy.util.ssh.exception.TaskExecFailException;
import com.joy.util.ssh.task.CustomTask;
import com.joy.util.ssh.task.impl.ExecCommand;

public class Sample003 {

	public static void main(String[] args) {
		SSHExec ssh = null;
		try {
			SSHExec.setOption(IOptionName.HALT_ON_FAILURE, false);
			ConnBean cb = new ConnBean("rfidic-1.svl.ibm.com", "tsadmin","u7i8o9p0");
			ssh = SSHExec.getInstance(cb);		
			CustomTask task1 = new ExecCommand("echo 123");
			CustomTask task2 = new ExecCommand("abcd");
			CustomTask task3 = new ExecCommand("pwd");
			ssh.connect();
			ssh.exec(task1);
			ssh.exec(task2);
			ssh.exec(task3);
			System.out.println("All tasks finished");
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
