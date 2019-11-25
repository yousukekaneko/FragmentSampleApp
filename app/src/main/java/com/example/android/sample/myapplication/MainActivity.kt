package com.example.android.sample.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_detail.*

class MainActivity : AppCompatActivity(), EditFragment.OnFragmentInteractionListener,
    DataPickerDialogFragment.OnDateSetListener, MasterFragment.OnListFragmentInteractionListener {

    var isTwoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        //スマホかアブレットか判定
        //container_detailはタブレットの時だけ使うため、
        //null出ない時は、isTwoPaneをtrueにする。
        if (container_detail != null) isTwoPane = true

        supportFragmentManager.beginTransaction()
            .add(R.id.container_master, MasterFragment.newInstance(1),
                FragmentTag.MASTER.toString()).commit()

        fab.setOnClickListener { view ->
            goEditScreen("","","", false, ModeInEdit.NEW_ENTRY)
        }
    }

    override fun onResume() {
        super.onResume()
        updateTodoList() //スマホの場合のリスト更新
    }

    private fun goEditScreen(title: String, deadline: String, taskDetail: String, isCompleted: Boolean, mode: ModeInEdit) {
        if (supportFragmentManager.findFragmentByTag(FragmentTag.EDIT.toString()) == null &&
            supportFragmentManager.findFragmentByTag(FragmentTag.DETAIL.toString()) == null) {
                supportFragmentManager.beginTransaction()
                                      .add(R.id.container_detail, EditFragment.newInstance(title, deadline, taskDetail, isCompleted, mode), FragmentTag.EDIT.toString())
                                      .commit()
                return
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_detail, EditFragment.newInstance(title, deadline, taskDetail, isCompleted, mode), FragmentTag.EDIT.toString())
                .commit()
            return
        }
        val intent = Intent(this@MainActivity, EditActivity::class.java).apply {
            putExtra(IntentKey.TITLE.name, title)
            putExtra(IntentKey.DEADLINE.name, deadline)
            putExtra(IntentKey.TASK_DETAIL.name, taskDetail)
            putExtra(IntentKey.IS_COMPLETED.name, isCompleted)
            putExtra(IntentKey.MODE_IN_EDIT.name, mode)
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.apply {
            findItem(R.id.menu_delete).isVisible = false
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_register).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    // EditFragment.OnFragmentInteractionListener
    override fun onDataPickerLaunched() {
        DataPickerDialogFragment().show(supportFragmentManager, FragmentTag.DATE_PICKER.toString())
    }

    override fun onDateSeleceted(dateString: String) {
        val inputDateText = findViewById<EditText>(R.id.inputDateText)
        inputDateText.setText(dateString)
    }

    // EditFragment.OnFragmentInteractionListener
    // タブレットの場合の更新処理
    override fun onDataEdited() {
        updateTodoList()
    }

    private fun updateTodoList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_master, MasterFragment.newInstance(1),
                FragmentTag.MASTER.toString()).commit()
    }

    // MasterFragment.OnListFragmentInteractionListener
    override fun onListItemClicked(item: TodoModel) {
        goDetailScreen(item.title, item.deadline, item.taskDetail, item.isCompleted)
    }

    private fun goDetailScreen(title: String, deadline: String, taskDetail: String, isCompleted: Boolean) {
        if (isTwoPane) {
            if (supportFragmentManager.findFragmentByTag(FragmentTag.EDIT.toString()) == null &&
                    supportFragmentManager.findFragmentByTag(FragmentTag.DETAIL.toString()) == null) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container_detail, DetailFragment.newInstance(title, deadline, taskDetail, isCompleted),
                        FragmentTag.DETAIL.toString()).commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_detail, DetailFragment.newInstance(title, deadline, taskDetail, isCompleted),
                        FragmentTag.DETAIL.toString()).commit()
            }
            return
        }
        val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
            putExtra(IntentKey.TITLE.name, title)
            putExtra(IntentKey.DEADLINE.name, deadline)
            putExtra(IntentKey.TASK_DETAIL.name, taskDetail)
            putExtra(IntentKey.IS_COMPLETED.name, isCompleted)
        }
        startActivity(intent)
    }

}
