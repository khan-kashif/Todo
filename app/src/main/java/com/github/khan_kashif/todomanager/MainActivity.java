package com.github.khan_kashif.todomanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    TodoApplication TodoApplication;
    ListView todoListView;
    ListItemAdapter listItemAdapter;
    FloatingActionButton addButton;

    Button deleteButton, cancelButton;

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
        listItemAdapter.Refresh();
    }

    protected void SetupButtonEvents(){
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
                                HandleDeleteMenuItem();
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
                HandleDeleteMenuItem();
            }
        });
    }

    protected void SetupListView(){
        todoListView = (ListView)findViewById(R.id.list_view_main);

        listItemAdapter = new ListItemAdapter(getApplicationContext(), R.layout.list_item, DataManager.TodoItems);
        todoListView.setAdapter(listItemAdapter);

        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HandleListItemClick(view, position);
            }
        });
    }

    protected void HandleListItemClick(View view, int position){

        if(DataManager.IsDeleteMode) {

            CheckBox taskCheckbox = (CheckBox) view.findViewById(R.id.task_check);

            TodoItem todoItem = DataManager.TodoItems.get(position);
            todoItem.MarkForDelete = !todoItem.MarkForDelete;
            taskCheckbox.setChecked(todoItem.MarkForDelete);

            return;
        }

        Intent navigateToEdit = new Intent(MainActivity.this, ToDoEdit.class);
        navigateToEdit.putExtra("position", position);
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
            HandleDeleteMenuItem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void HandleDeleteMenuItem(){
        if(DataManager.IsDeleteMode){
            DataManager.SetDeleteMode(false);
            listItemAdapter.Refresh();
            todoListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

            addButton.setVisibility(View.VISIBLE);

            deleteButton.setVisibility(View.INVISIBLE);
            cancelButton.setVisibility(View.INVISIBLE);

        }else {
            DataManager.SetDeleteMode(true);
            listItemAdapter.Refresh();
            todoListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

            addButton.setVisibility(View.INVISIBLE);

            deleteButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
        }
    }
}
