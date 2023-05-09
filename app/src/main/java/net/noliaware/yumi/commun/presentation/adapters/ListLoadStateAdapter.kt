package net.noliaware.yumi.commun.presentation.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.util.inflate

class ListLoadStateAdapter(
    //private val retry: () -> Unit
) : LoadStateAdapter<ListLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = LoadStateViewHolder(
        parent.inflate(R.layout.load_state_layout)
    )

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {

        val progress =
            holder.itemView.findViewById<CircularProgressIndicator>(R.id.load_state_progress)
        //val btnRetry = holder.itemView.load_state_retry
        //val txtErrorMessage = holder.itemView.load_state_errorMessage

        //btnRetry.isVisible = loadState !is LoadState.Loading
        //txtErrorMessage.isVisible = loadState !is LoadState.Loading
        progress.isVisible = loadState is LoadState.Loading

        /*if (loadState is LoadState.Error) {
            txtErrorMessage.text = loadState.error.localizedMessage
        }

        btnRetry.setOnClickListener {
            retry.invoke()
        }

         */
    }

    class LoadStateViewHolder(private val view: View) : RecyclerView.ViewHolder(view)
}