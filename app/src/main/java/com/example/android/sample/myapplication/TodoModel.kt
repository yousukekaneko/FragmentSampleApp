package com.example.android.sample.myapplication

import io.realm.RealmObject

open class TodoModel: RealmObject() {

    //タイトル
    var title: String = ""
    //期日(yyyy/MM/dd)
    var deadline: String = ""
    //タスク内容
    var taskDetail: String = ""
    //タスク完了フラグ
    var isCompleted: Boolean = false
}