package com.example.rumi.apollodemo

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import com.example.rumi.apollodemo.databinding.ActivityMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        getStudents()

    }

    fun getStudents() {
        var allStudentsQuery = MyApolloClient.getMyAppolloClient().query(AllStudentsQuery.builder().build())
        Rx2Apollo.from(allStudentsQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response<AllStudentsQuery.Data>>() {
                    override fun onSuccess(t: Response<AllStudentsQuery.Data>) {
                        println("onSuccess $t")
                        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
                        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                        var studentAdapter=StudentAdapter(t.data()?.allStudents()?.toList())
                        binding.recyclerView.apply {
                            layoutManager = linearLayoutManager
                            adapter=studentAdapter
                        }
                    }

                    override fun onError(e: Throwable) {
                        println("onError $e")
                    }
                })
    }
}
