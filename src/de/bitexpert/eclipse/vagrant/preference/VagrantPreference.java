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


package de.bitexpert.eclipse.vagrant.preference;


import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import de.bitexpert.eclipse.vagrant.VagrantActivator;


/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog.
 */

public class VagrantPreference extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
	public VagrantPreference()
	{
		super(GRID);
		setPreferenceStore(VagrantActivator.getDefault().getPreferenceStore());
		setDescription("Vagrant Settings");
	}


	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors()
	{
		addField(new FileFieldEditor(PreferenceConstants.VAGRANT_PATH,
			"&Vagrant Executable:", getFieldEditorParent()));
	}


	@Override
	public void init(IWorkbench workbench)
	{
	}
}