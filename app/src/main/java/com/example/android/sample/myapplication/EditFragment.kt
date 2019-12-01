package com.example.android.sample.myapplication

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_edit.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EditFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFragment : Fragment() {

    private var title: String? = ""
    private var deadline: String? = ""
    private var taskDetail: String? = ""
    private var isCompleted: Boolean = false
    private var mode: ModeInEdit? = null

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_title)
            deadline = it.getString(ARG_deadline)
            taskDetail = it.getString(ARG_taskDetail)
            isCompleted = it.getBoolean(ARG_isCompleted)
            mode = it.getSerializable(ARG_mode) as ModeInEdit

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //mode!!が使えないので以下の記述に変更
        mode?.let { updateUi(it) }
        imageButtonDateSet.setOnClickListener {
            listener!!.onDataPickerLaunched()
        }
    }

    private fun updateUi(mode: ModeInEdit) {
        when(mode) {
            ModeInEdit.NEW_ENTRY -> {
                checkBox.visibility = View.INVISIBLE
            }
            ModeInEdit.EDIT -> {
                inputTitleText.setText(title)
                inputDateText.setText(deadline)
                inputDetailText.setText(taskDetail)
                if (isCompleted) checkBox.isChecked = true else isCompleted
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.apply {
            findItem(R.id.menu_delete).isVisible = false
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_register).isVisible = true

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.menu_register) recordToRealmDB(mode)
        return super.onOptionsItemSelected(item)
    }

    private fun recordToRealmDB(mode: ModeInEdit?) {

        // タイトルと期日がセットされていないと登録できない
        val isRequiredItemsFilled = isRequiredFilledCheck()
        if (!isRequiredItemsFilled) return

        //TODO 無理やりmodeがnullでも単語をスケジュールを追加できるようにしたが、修正必要
//        if (mode == null ) {
//            addNewTodo()
//        }
        when (mode) {
            ModeInEdit.NEW_ENTRY -> addNewTodo()
            ModeInEdit.EDIT -> editExistingTodo()
        }
        listener?.onDataEdited()
        fragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    private fun isRequiredFilledCheck(): Boolean {
        if (inputTitleText.text.toString() == ""){
            inputTitle.error = getString(R.string.error)
            return false
        }
        if (inputDateText.text.toString() == "") {
            inputDate.error = getString(R.string.error)
            return false
        }
        return true
    }

    private fun editExistingTodo() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val selectedTodo = realm.where(TodoModel::class.java)
            .equalTo(TodoModel::title.name, title)
            .equalTo(TodoModel::deadline.name, deadline)
            .equalTo(TodoModel::taskDetail.name, taskDetail)
            .findFirst()

        selectedTodo!!.apply {
            title = inputTitleText.text.toString()
            deadline = inputDateText.text.toString()
            taskDetail = inputDetailText.text.toString()
            isCompleted = if (checkBox.isChecked) true else false
        }
        realm.commitTransaction()
        realm.close()
    }

    private fun addNewTodo() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val newTodo = realm.createObject(TodoModel::class.java)
        newTodo.apply {
            title = inputTitleText.text.toString()
            deadline = inputDateText.text.toString()
            taskDetail = inputDetailText.text.toString()
            isCompleted = if (checkBox.isChecked) true else false
        }
        realm.commitTransaction()
        realm.close()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onDataPickerLaunched()
        fun onDataEdited()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditFragment.
         */

        private val ARG_title = IntentKey.TITLE.name
        private val ARG_deadline = IntentKey.DEADLINE.name
        private val ARG_taskDetail = IntentKey.TASK_DETAIL.name
        private val ARG_isCompleted = IntentKey.IS_COMPLETED.name
        private val ARG_mode = IntentKey.MODE_IN_EDIT.name

        @JvmStatic
        fun newInstance(title: String, deadline: String, taskDetail: String, isCompleted: Boolean, mode: ModeInEdit): EditFragment {
                return EditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_title, title)
                    putString(ARG_deadline, deadline)
                    putString(ARG_taskDetail, taskDetail)
                    putBoolean(ARG_isCompleted, isCompleted)
                    putSerializable(ARG_mode, mode)
                }
            }
        }
    }
}
