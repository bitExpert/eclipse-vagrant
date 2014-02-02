/*
 * Copyright (c) 2007-2014 bitExpert AG
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


import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;


/**
 * The Vagrant plugin action executing the init task.
 *
 * @author	Stephan Hochdörfer <S.Hochdoerfer@bitExpert.de>
 */


public class InitExecutor extends VagrantExecutor
{
	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		if (selection instanceof IStructuredSelection)
		{
			IStructuredSelection strucSelect = (IStructuredSelection) selection;
			this.selectedProject = (IProject) strucSelect.getFirstElement();
		}
	}


	@Override
	protected String getVagrantCommand()
	{
		return "init";
	}

}