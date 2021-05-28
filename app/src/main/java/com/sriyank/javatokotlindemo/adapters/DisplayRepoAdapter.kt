package com.sriyank.javatokotlindemo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sriyank.javatokotlindemo.extensions.toast
import com.sriyank.javatokotlindemo.data.Repo
import com.sriyank.javatokotlindemo.data.ViewModelRepos
import com.sriyank.javatokotlindemo.databinding.ListItemBinding

class DisplayRepoAdapter(private val context: Context, private val viewModelRepos: ViewModelRepos) :
        ListAdapter<Repo, DisplayRepoAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = getItem(position)
        holder.bindTo(repo)
        holder.itemView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION)
                onItemClickListener.onItemClick(getItem(position))
        }
    }

    inner class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(repo: Repo){
            binding.txvName.text = repo.name
            binding.txvLanguage.text = repo.language
            binding.txvStars.text = repo.stars.toString()
            binding.txvForks.text = repo.forks.toString()
            binding.txvWatchers.text = repo.watchers.toString()
            binding.imgBookmark.setOnClickListener {
               bookmarkRepository(repo)
           }
        }

        private fun bookmarkRepository(current: Repo?) {
            viewModelRepos.insert(current)
            context.toast("Bookmarked Successfully")
//            Util.showMessage(context, "Bookmarked Successfully")
        }

    }

    interface OnItemClickListener {
        fun onItemClick(repo: Repo)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Repo> = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }
        }
    }
}