package com.example.android_loader_manager;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private LoaderManager loaderManager;
	private ListView listView1;
	private MyAdapter adapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loaderManager = this.getLoaderManager();
		loaderManager.initLoader(1000, null, callbacks);
		listView1 = (ListView) findViewById(R.id.listView1);
		registerForContextMenu(listView1);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.activity_main, menu);

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.add:
			// 添加自定义对话框，完成数据录入。
			final Dialog dialog = createAddDialog(MainActivity.this);
			final EditText editText = (EditText) dialog
					.findViewById(R.id.editText1);
			Button button = (Button) dialog.findViewById(R.id.button1);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String name = editText.getText().toString().trim();
					ContentResolver contentResolver = getContentResolver();
					Uri url = Uri
							.parse("content://com.example.android_loader_manager.StudentContentProvider/student");
					ContentValues values = new ContentValues();
					values.put("name", name);
					Uri result_uri = contentResolver.insert(url, values);
					if (result_uri != null) {
						loaderManager.restartLoader(1000, null, callbacks);
						dialog.dismiss();
					}
				}
			});
			dialog.show();
			break;
		case R.id.del:
			int position = adapterContextMenuInfo.position;
			String name = adapter.getItem(position).toString();
			ContentResolver contentResolver = getContentResolver();

			Uri url = Uri
					.parse("content://com.example.android_loader_manager.StudentContentProvider/student");
			int count = contentResolver.delete(url, "name=?",
					new String[] { name });
			if (count > 0) {
				loaderManager.restartLoader(1000, null, callbacks);
			}
			break;

		}
		return super.onMenuItemSelected(featureId, item);
	}

	public Dialog createAddDialog(Context context) {
		Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.add);
		return dialog;
	}

	public class MyAdapter extends BaseAdapter {

		private Context context;
		private List<String> list = null;

		public MyAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView view = null;
			if (convertView == null) {
				view = new TextView(context);
				view.setHeight(100);
				view.setGravity(Gravity.CENTER);
			} else {
				view = (TextView) convertView;
			}
			view.setText(list.get(position).toString());
			return view;
		}

		public void setList(List<String> list) {
			this.list = list;
		}
	}

	private LoaderCallbacks<Cursor> callbacks = new LoaderCallbacks<Cursor>() {

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			// TODO Auto-generated method stub
			CursorLoader loader = new CursorLoader(MainActivity.this);
			Uri uri = Uri
					.parse("content://com.example.android_loader_manager.StudentContentProvider/student");
			loader.setUri(uri);
			return loader;
		}

		// 完成对ui数据的提取，更新数据的操作。
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			// TODO Auto-generated method stub
			// 把数据提取出来，放到适配器中完成对ui的更新操作。
			List<String> list = new ArrayList<String>();
			while (cursor.moveToNext()) {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				list.add(name);
			}
			adapter = new MyAdapter(MainActivity.this);
			adapter.setList(list);
			listView1.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			// TODO Auto-generated method stub

		}
	};

}
