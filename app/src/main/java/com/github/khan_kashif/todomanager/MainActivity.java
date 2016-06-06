package com.github.khan_kashif.todomanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    TodoApplication TodoApplication;
    ListView todoListView;
    ListItemAdapter listItemAdapter;
    FloatingActionButton addButton;

    Button deleteButton, cancelButton;
    LinearLayout bottomButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TodoApplication = (TodoApplication)getApplication();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        SetupListView();
        SetupFABEvents();

        SetupButtonEvents();
    }

    @Override
    protected void onResume(){
        super.onResume();

        UpdateDeleteControls(false);
    }

    protected void SetupButtonEvents(){

        bottomButtons = (LinearLayout)findViewById(R.id.bottom_buttons);

        deleteButton = (Button)findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.delete)
                        .setMessage(R.string.delete_confirm)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataManager.DeleteTodoItems(TodoApplication);
                                listItemAdapter.Refresh();
                                UpdateDeleteControls(false);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        cancelButton = (Button)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateDeleteControls(false);
            }
        });
    }

    protected void SetupListView(){
        todoListView = (ListView)findViewById(R.id.list_view_main);

        listItemAdapter = new ListItemAdapter(getApplicationContext());
        todoListView.setAdapter(listItemAdapter);

        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HandleListItemClick(view, position);
            }
        });
    }

    protected void HandleListItemClick(View view, int position){

        TodoItem todoItem = DataManager.GetTodoItem(position);

        if(DataManager.IsDeleteMode) {

            CheckBox taskCheckbox = (CheckBox) view.findViewById(R.id.task_check);

            todoItem.MarkForDelete = !todoItem.MarkForDelete;
            taskCheckbox.setChecked(todoItem.MarkForDelete);

            return;
        }

        Intent navigateToEdit = new Intent(MainActivity.this, ToDoEdit.class);
        navigateToEdit.putExtra("idTodoItem", todoItem.ID);
        MainActivity.this.startActivity(navigateToEdit);
    }

    protected void SetupFABEvents() {
        addButton = (FloatingActionButton) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigateToEdit = new Intent(MainActivity.this, ToDoEdit.class);
                MainActivity.this.startActivity(navigateToEdit);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_save).setVisible(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            UpdateDeleteControls(!DataManager.IsDeleteMode);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void UpdateDeleteControls(boolean mode){

        DataManager.SetDeleteMode(mode);
        listItemAdapter.Refresh();

        if(mode){

            todoListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            addButton.setVisibility(View.INVISIBLE);
            bottomButtons.setVisibility(View.VISIBLE);

        }else {

            todoListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            addButton.setVisibility(View.VISIBLE);
            bottomButtons.setVisibility(View.INVISIBLE);

        }
    }
}
