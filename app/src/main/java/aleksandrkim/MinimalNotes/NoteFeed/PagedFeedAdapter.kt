package aleksandrkim.MinimalNotes.NoteFeed

import aleksandrkim.MinimalNotes.Db.Note
import aleksandrkim.MinimalNotes.R
import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Aleksandr Kim on 20 Apr, 2018 4:25 PM for ArchComponentsTest
 */

fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

typealias RecyclerItemClickListener = (position: Int) -> Unit
typealias OnListUpdatedListener = (newSize: Int) -> Unit

class PagedFeedAdapter(private val recyclerItemClickListener: RecyclerItemClickListener,
                       private val onListUpdatedListener: OnListUpdatedListener) :
    PagedListAdapter<Note, NoteFeedVH>(Note.DIFF_ITEM_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteFeedVH {
        val v = parent.inflate(R.layout.note_feed_row)
        val noteFeedVH = NoteFeedVH(v)
        v.setOnClickListener { recyclerItemClickListener(noteFeedVH.adapterPosition) }
        return noteFeedVH
    }

    override fun onCurrentListChanged(currentList: PagedList<Note>?) {
        super.onCurrentListChanged(currentList)
        onListUpdatedListener(currentList?.size ?: 0)
    }

    fun getNote(position: Int): Note? {
        return getItem(position)
    }

    override fun onBindViewHolder(holder: NoteFeedVH, position: Int) {
        val note = getItem(position)
        note?.let { holder.bind(it) } ?: holder.clear()
    }

    companion object {
        val TAG = "PagedFeedAdapter"
    }
}
