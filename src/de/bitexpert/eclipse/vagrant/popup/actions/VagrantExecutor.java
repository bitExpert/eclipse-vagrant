/*
 * Copyright (c) 2007-2012 bitExpert AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */



package de.bitexpert.eclipse.vagrant.popup.actions;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import de.bitexpert.eclipse.vagrant.preference.PreferenceConstants;

import eclipse_vagrant.Activator;


/**
 * The Vagrant plugin action dealing with the main logic. Subclasses need just
 * to implement the <code>getVagrantCommand</code> method to indicate which
 * vagrant command should be executed.
 *
 * @author	Stephan Hochdörfer <S.Hochdoerfer@bitExpert.de>
 */


abstract public class VagrantExecutor implements IObjectActionDelegate
{
	protected IProject selectedProject;


	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
	}


	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action)
	{
		if(null != this.selectedProject)
		{
			MessageConsole myConsole       = createConsole("Vagrant Output");
			final MessageConsoleStream out = myConsole.newMessageStream();
			final String vagrantExecutable = getVagrantExecutable();
			final String vagrantCommand    = getVagrantCommand();
			final String workingDir        = this.selectedProject.getLocation().
				toOSString();

			Job job = new Job("Vagrant Background Job") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try
					{
						// run composer install task in background
						List<String> commands = new ArrayList<String>();
						commands.add(vagrantExecutable);
						commands.add(vagrantCommand);

						ProcessBuilder pb = new ProcessBuilder(commands);
						pb.directory(new File(workingDir));
						pb.redirectErrorStream(true);
						Process process = pb.start();

						// Read output and write it to the open Eclipse console
						BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
						String line = null;
						while((line = br.readLine()) != null)
						{
							out.println(line);
						}
						process.waitFor();
					}
					catch(IOException e)
					{
						System.out.println("The exception is " + e.getMessage());
					}
					catch(InterruptedException e)
					{
						System.out.println("The exception is " + e.getMessage());
					}

					return Status.OK_STATUS;
				}
			};

			// Start the Job
			job.schedule();
		}
	}


	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		if (selection instanceof IStructuredSelection)
		{
			IStructuredSelection strucSelect = (IStructuredSelection) selection;
			IFile selectedElement = (IFile) strucSelect.getFirstElement();
			if(null != selectedElement)
			{
				this.selectedProject  = selectedElement.getProject();
			}
		}
	}


	/**
	 * Creates a console for the given name.
	 *
	 * @param name
	 * @return MessageConsole
	 */
	private MessageConsole createConsole(String name)
	{
		ConsolePlugin plugin     = ConsolePlugin.getDefault();
		IConsoleManager manager  = plugin.getConsoleManager();
		MessageConsole myConsole = new MessageConsole(name, null);
		manager.addConsoles(new IConsole[]{myConsole});
		return myConsole;
	}


	/**
	 * Returns the path to the Vagrant executable.
	 *
	 * @return String
	 */
	private String getVagrantExecutable()
	{
		String file = "\\vagrant.sh";
		String platform = System.getProperty("os.name"); 
		if(platform.contains("Windows")) {
			file = "\\vagrant.bat";
		}
		IPreferenceStore store = Activator.getDefault()
                .getPreferenceStore();
		return store.getString(PreferenceConstants.P_PATH) + file;
	}


	/**
	 * Returns the name of the vagrant command to execute.
	 *
	 * @return String
	 */
	abstract protected String getVagrantCommand();
}