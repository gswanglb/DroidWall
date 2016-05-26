/**
 * Custom scripts activity.
 * This screen is displayed to change the custom scripts.
 * 
 * Copyright (C) 2009-2011  Rodrigo Zechin Rosauro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Rodrigo Zechin Rosauro
 * @version 1.0
 */
package com.asiainfo.droidwall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Custom scripts activity. This screen is displayed to change the custom
 * scripts.
 */
public class CustomScriptActivity extends Activity {
	private EditText script;
	private EditText script2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = getLayoutInflater().inflate(
				R.layout.activity_custom_script, null);
		((TextView) view.findViewById(R.id.custom_script_title1))
				.setMovementMethod(LinkMovementMethod.getInstance());
		final SharedPreferences prefs = getSharedPreferences(Api.PREFS_NAME, 0);
		this.script = (EditText) view.findViewById(R.id.customscript);
		this.script.setText(prefs.getString(Api.PREF_CUSTOMSCRIPT, ""));
		this.script2 = (EditText) view.findViewById(R.id.customscript2);
		this.script2.setText(prefs.getString(Api.PREF_CUSTOMSCRIPT2, ""));

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		
		setContentView(view);
	}

	/**
	 * Set the activity result to RESULT_OK and terminate this activity.
	 */
	private void resultOk() {
		final Intent response = new Intent(Api.CUSTOM_SCRIPT_MSG);
		response.putExtra(Api.SCRIPT_EXTRA, script.getText().toString());
		response.putExtra(Api.SCRIPT2_EXTRA, script2.getText().toString());
		setResult(RESULT_OK, response);
		finish();
	}

	/**
	 * Set the activity result to RESULT_OK and terminate this activity.
	 */
	private void resultCancel() {
		final Intent response = new Intent(Api.CUSTOM_SCRIPT_MSG);
		setResult(RESULT_CANCELED, response);
		finish();
	}

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		// Handle the back button when dirty
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			final SharedPreferences prefs = getSharedPreferences(
					Api.PREFS_NAME, 0);
			if (script.getText().toString()
					.equals(prefs.getString(Api.PREF_CUSTOMSCRIPT, ""))
					&& script2
							.getText()
							.toString()
							.equals(prefs.getString(Api.PREF_CUSTOMSCRIPT2, ""))) {
				// Nothing has been changed, just return
				return super.onKeyDown(keyCode, event);
			}
			final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						resultOk();
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						resultCancel();
						break;
					}
				}
			};
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.unsaved_changes)
					.setMessage(R.string.unsaved_changes_message)
					.setPositiveButton(R.string.apply, dialogClickListener)
					.setNegativeButton(R.string.discard, dialogClickListener)
					.show();
			// Say that we've consumed the event
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_custom_script, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			resultCancel();
			finish();
			return true;
		case R.id.menu_save_script:
			resultOk();
			return true;
		case R.id.menu_discard_script:
			resultCancel();
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
