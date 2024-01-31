package com.mk.diary.adapters.recyclerview

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.card.MaterialCardView
import com.mk.diary.utils.companion.Static
import my.dialy.dairy.journal.dairywithlock.R


class HashTagRecyclerAdapter(private val list: ArrayList<String>) :
    RecyclerView.Adapter<HashTagRecyclerAdapter.ViewHolder>() {
    private lateinit var click: HashTagItemClick
    fun hashRecClick(lis:HashTagItemClick){
        click=lis
    }
    interface HashTagItemClick{
        fun hashTagClick(data:String)
        fun hashTagDelete(data:String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hash_tag_rec, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        if (Static.removehashTagBoolean){
            if (position==0){
                holder.editText.visibility=View.GONE
                holder.deleteBtn.visibility=View.GONE
            }
        }else{
            if (position==0){
                holder.editText.visibility=View.VISIBLE
                holder.deleteBtn.visibility=View.VISIBLE
            }
        }
        // Highlight selected items
        if (position==0) {
            holder.deleteBtn.visibility=View.GONE
            holder.editText.isFocusable=true
            holder.editText.setOnEditorActionListener { _, actionId, event -> if
                    (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.action ==
                        KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                    val enteredText = holder.editText.text.toString()
                    click.hashTagClick(enteredText)
                holder.editText.setText("")
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            holder.editText.requestFocus()
            val imm = holder.editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(holder.editText, InputMethodManager.SHOW_IMPLICIT)
        }else {
            val radiusInPixels = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.radius)
            holder.card.radius = radiusInPixels.toFloat()
            holder.deleteBtn.visibility=View.VISIBLE
            holder.editText.isFocusable=false
            holder.editText.isFocusableInTouchMode = false
            holder.editText.setBackgroundResource(R.drawable.rounded_ten)
            holder.editText.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.d9D9))
            holder.editText.setText(item)
            holder.deleteBtn.setOnClickListener {
                notifyItemRemoved(position)
                click.hashTagDelete(item)
            }
        }
    }
    override fun getItemCount(): Int {
        return  list.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deleteBtn: ImageView = itemView.findViewById(R.id.deleteBtn)
        val editText: EditText = itemView.findViewById(R.id.tagTitleEdd)
        val card: MaterialCardView = itemView.findViewById(R.id.card)
    }
}



