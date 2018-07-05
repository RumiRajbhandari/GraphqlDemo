package com.example.rumi.apollodemo

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import com.example.rumi.apollodemo.databinding.ActivityMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var studentList: MutableList<Any> = mutableListOf()
    lateinit var studentAdapter: StudentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        studentAdapter = StudentAdapter(studentList)
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = studentAdapter
        }

        binding.btnCreate.setOnClickListener {
            var name = binding.etName.text.toString()
            var address = binding.etAddress.text.toString()
            addStudent(name = name, address = address)

        }

        getStudents()


    }

    private fun getStudents() {
        var allStudentsQuery = MyApolloClient.getMyAppolloClient().query(AllStudentsQuery.builder().build())
        Rx2Apollo.from(allStudentsQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response<AllStudentsQuery.Data>>() {
                    override fun onSuccess(t: Response<AllStudentsQuery.Data>) {
                        println("onSuccess $t")
                        t.data()?.allStudents()?.forEach { studentList.add(it) }
                        studentAdapter.notifyDataSetChanged()
                    }

                    override fun onError(e: Throwable) {
                        println("onError $e")
                    }
                })
    }

    private fun addStudent(name: String, address: String) {
        var createStudentQuery = MyApolloClient.getMyAppolloClient().mutate(CreateStudentQuery
                .builder()
                .name(name)
                .address(address)
                .build())
        Rx2Apollo.from(createStudentQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response<CreateStudentQuery.Data>>() {
                    override fun onSuccess(t: Response<CreateStudentQuery.Data>) {
                        var student = t?.data()?.createStudent()
                        if (student != null) {
                            studentList.add(student)
                            studentAdapter.notifyDataSetChanged()
                        }
                        Toast.makeText(this@MainActivity, "Succesful", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                    }

                })

    }

}
