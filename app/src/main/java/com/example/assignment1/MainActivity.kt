package com.example.assignment1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.util.*

private lateinit var mDbRef:DatabaseReference

private lateinit var userRecyclerView: RecyclerView
private lateinit var ItemList: ArrayList<CheckList>
private lateinit var adapter:CheckListAdapter
private lateinit var editText:EditText

private lateinit var add:FloatingActionButton;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**********************************************INITIALIZING THE VARIABLE******************************************/
        mDbRef=FirebaseDatabase.getInstance().getReference();
        add=findViewById(R.id.floatingActionButton)
        ItemList= ArrayList()                                            // Add the item to the ArrayList by getting it from the database
        userRecyclerView=findViewById(R.id.item_list);
        adapter= CheckListAdapter(this,ItemList)
        userRecyclerView.layoutManager= LinearLayoutManager(this)
        userRecyclerView.adapter=adapter                                             // setting the adapter

       /*******************************************RETRIEVE THE DATA FROM THE DATABASE**********************************/
        mDbRef.child("item").orderByChild("id").addValueEventListener(object: ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {
                //snapshot is use to get the data from the dataBase
                ItemList.clear()            // we have to clear this because whenever there is a change in data it is called so to prevent repeating the value we do this
                for(postSnapshot in snapshot.children){
                    val currentItem=postSnapshot.getValue(CheckList::class.java)
                    ItemList.add(currentItem!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        /****************************************Handling Event when floating Action Button is Clicked*****************/
        add.setOnClickListener{
            add_dialog()
        }


    }

    /***********************************************Showing the alertDialog Box then adding the item to the Database******/
    private fun add_dialog(){

        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater;
        val dialogView: View = inflater.inflate(R.layout.add_dialog, null)

        with(builder)
        {
            setView(dialogView)
            setTitle("")

            val alert11: AlertDialog = builder.create()
            alert11.show()

            val addButton: Button =dialogView.findViewById(R.id.add_item);
            editText=dialogView.findViewById(R.id.add_title);

            /***************************************Handle Event when we click add button in Dialog Box*************/
            addButton.setOnClickListener{

                alert11.dismiss()
                val id=UUID.randomUUID().toString()
                var title = editText.text.toString();
                mDbRef.child("item").child(id).setValue(CheckList(title ,id))

                mDbRef.child("item").addValueEventListener(object: ValueEventListener {


                    override fun onDataChange(snapshot: DataSnapshot) {
                        //snapshot is use to get the data from the dataBase
                        ItemList.clear()            // we have to clear this because whenever there is a change in data it is called so to prevent repeating the value we do this
                        for(postSnapshot in snapshot.children){
                            val currentItem=postSnapshot.getValue(CheckList::class.java)
                            ItemList.add(currentItem!!)
                        }
                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })



            }



        }

    }

}