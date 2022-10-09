package com.example.assignment1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.coroutines.NonDisposableHandle.parent

private lateinit var mDbRef: DatabaseReference;
class CheckListAdapter (val context: Context, val arr:ArrayList<CheckList>) : RecyclerView.Adapter<CheckListAdapter.UserViewHolder>() {




    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        val title=itemView.findViewById<TextView>(R.id.title)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val view: View = LayoutInflater.from(context).inflate(R.layout.checklist_item,parent,false)


        return UserViewHolder(view)



    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val currentItem=arr[position]
        holder.title.text=currentItem.title

        mDbRef=FirebaseDatabase.getInstance().getReference();
        /*****************************When the particular Item is Clicked**************************************/
        holder.itemView.setOnClickListener{

           delete_dialog(arr[position].id,position);

        }
    }

    /*************************************Handling Event to delete the item From The List***************************/
    private fun delete_dialog(id: String?, position: Int) {

        val builder1 = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.delete_dialog, null)

        with(builder1)
        {
            setView(dialogView)
            setTitle("")

            val alert11: AlertDialog = builder1.create()
            alert11.show()

            val deleteButton: Button =dialogView.findViewById(R.id.delete);

            /***************************************Handling Event when we click the delete button of the Dialog box********/
            deleteButton.setOnClickListener{

                alert11.dismiss()

                val applesQuery = mDbRef.child("item").orderByChild("id").equalTo(id)
                applesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (appleSnapshot in dataSnapshot.children) {
                            appleSnapshot.ref.removeValue()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
            }
        }

    }

    override fun getItemCount(): Int {
        return arr.size
    }

}