package com.mk.diary.adapters.recyclerview

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import com.mk.diary.R
import android.widget.EditText
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.android.material.card.MaterialCardView


class HashTagRecyclerAdapter(private val list: List<String>) :
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
                    Log.i("keyboarrrr","workingggggggg")
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
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deleteBtn: ImageView = itemView.findViewById(R.id.deleteBtn)
        val editText: EditText = itemView.findViewById(R.id.tagTitleEdd)
        val card: MaterialCardView = itemView.findViewById(R.id.card)
    }
}


//class HashTagRecyclerAdapter(private  val dataList: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    private val VIEW_TYPE_HEADER = 0
//    private val VIEW_TYPE_NORMAL = 1
//    lateinit var click:HashTagItemClick
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return if (viewType == VIEW_TYPE_HEADER) {
//            val headerView = LayoutInflater.from(parent.context).inflate(R.layout.hash_tag_edittext_rec, parent, false)
//            HeaderViewHolder(headerView)
//        } else {
//            val normalView = LayoutInflater.from(parent.context).inflate(R.layout.hash_tag_rec, parent, false)
//        return NormalViewHolder(normalView)
//        }
//    }
//    fun hashRecClick(lis:HashTagItemClick){
//        click=lis
//    }
//    interface HashTagItemClick{
//        fun hashTagClick(data:String)
//        fun hashTagDelete(data:String)
//    }
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (holder.itemViewType == VIEW_TYPE_NORMAL) {
//            val normalViewHolder = holder as NormalViewHolder
//            normalViewHolder.bind(dataList[position - 1],dataList,position,normalViewHolder.itemView.context)
//        } else {
//            val headerViewHolder = holder as HeaderViewHolder
//            headerViewHolder.bind(dataList[0],dataList)
//
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return dataList.size + 1
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (position == 0) {
//            VIEW_TYPE_HEADER
//        } else {
//            VIEW_TYPE_NORMAL
//        }
//    }
//
//    // ViewHolder for the normal items
//    inner  class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//         val tagTextView: TextView = itemView.findViewById(R.id.tagTextViewTv)
//         val deleteBtn: ImageView = itemView.findViewById(R.id.deleteBtn)
//        fun bind(data: String,list:ArrayList<String>,position: Int,context: Context) {
//            tagTextView.text=data
//            if (position==1 && data=="#"){
//                tagTextView.visibility=View.GONE
//                deleteBtn.visibility=View.GONE
//            }else{
//                deleteBtn.visibility=View.VISIBLE
//                tagTextView.visibility=View.VISIBLE
//            }
//            deleteBtn.setOnClickListener {
//                click.hashTagDelete(data)
//                notifyDataSetChanged()
//            }
//        }
//    }
//
//    // ViewHolder for the header item
//    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val tagTitleEd: EditText = itemView.findViewById(R.id.tagTitleEdd)
//
//        fun bind(data: String, list: MutableList<String>) {
//            tagTitleEd.setOnEditorActionListener { _, actionId, event ->
//                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    val enteredText = tagTitleEd.text.toString()
//                    click.hashTagClick(enteredText)
//                    tagTitleEd.setText("")
//                    Log.i("keyboarrrr","workingggggggg")
//                    return@setOnEditorActionListener true
//                }
//                return@setOnEditorActionListener false
//            }
//            tagTitleEd.requestFocus()
//            val imm = tagTitleEd.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.showSoftInput(tagTitleEd, InputMethodManager.SHOW_IMPLICIT)
//
//        }
//
//    }
//}


