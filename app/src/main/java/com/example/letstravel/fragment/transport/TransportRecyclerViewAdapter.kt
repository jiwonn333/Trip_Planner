import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.letstravel.R
import com.example.letstravel.fragment.transport.RecyclerViewDirectionItem

class TransportRecyclerViewAdapter(
    var context: Context,
    private var itemLists: ArrayList<RecyclerViewDirectionItem>?
) :
    RecyclerView.Adapter<TransportRecyclerViewAdapter.ViewHolder>() {
    private var selectedItemPosition = 0
    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(itemLists: ArrayList<RecyclerViewDirectionItem>?) {
        this.itemLists = itemLists
        notifyDataSetChanged()
    }

    // 아이템 뷰를 위한 뷰 홀더 객체를 생성하여 리턴
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.transport_item, parent, false)
        return ViewHolder(view)
    }

    // 생성된 뷰 홀더에 데이터 넣는 함수 (position에 해당하는 데이터를 뷰홀더 아이템뷰에 표시)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.setData(itemLists!![holder.bindingAdapterPosition])
    }

    override fun getItemCount(): Int {
        return if (itemLists != null && itemLists!!.isNotEmpty()) itemLists!!.size else 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var itemLayout: LinearLayout

        init {
            imageView = itemView.findViewById(R.id.direction_item_image)
            itemLayout = itemView.findViewById(R.id.direction_item_layout)
        }

        @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
        fun setData(item: RecyclerViewDirectionItem) {
            imageView.setImageResource(item.iconDrawable)
            val isSelected = selectedItemPosition == bindingAdapterPosition
            val itemColor =
                if (isSelected) R.color.icon_selected_color else R.color.direction_icon_color
            imageView.setColorFilter(context.getColor(itemColor), PorterDuff.Mode.SRC_IN)
            imageView.setOnClickListener { v: View? ->
                selectedItemPosition = bindingAdapterPosition
                if (selectedItemPosition != RecyclerView.NO_POSITION) {
                    if (itemClickListener != null) {
                        itemClickListener!!.onItemClick(itemView, selectedItemPosition)
                    }
                }
                notifyDataSetChanged()
            }
        }
    }
}