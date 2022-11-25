package net.noliaware.yumi.feature_login.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.presentation.adapters.ItemViewHolder
import net.noliaware.yumi.feature_login.presentation.views.AccountCategoryView
import net.noliaware.yumi.feature_login.presentation.views.AccountItemView
import net.noliaware.yumi.feature_profile.domain.model.UserProfile

class ManagedAccountAdapter(
    private val onItemClicked: (UserProfile) -> Unit
) : PagingDataAdapter<UserProfile, ItemViewHolder<AccountItemView>>(UserProfileComparator) {

    override fun onBindViewHolder(holder: ItemViewHolder<AccountItemView>, position: Int) {
        getItem(position)?.let { accountData ->
            holder.heldItemView.fillViewWithData(
                mapAdapter(accountData, holder)
            )
        }
    }

    private fun mapAdapter(
        userProfile: UserProfile,
        holder: ItemViewHolder<AccountItemView>
    ) = AccountItemView.AccountItemViewAdapter(
        title = "${userProfile.title} ${userProfile.firstName} ${userProfile.lastName}",
        phoneNumber = holder.heldItemView.context.getString(
            R.string.mobile_short,
            userProfile.cellPhoneNumber
        ),
        lastLogin = userProfile.login.orEmpty()
    ).also { accountItemViewAdapter ->
        userProfile.categories.map { category ->
            AccountCategoryView.AccountCategoryViewAdapter(
                iconName = category.categoryIcon.orEmpty(),
                title = category.categoryLabel,
                count = category.availableVoucherCount ?: 0
            )
        }.also {
            accountItemViewAdapter.accountCategoryViewAdapters.addAll(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder<AccountItemView>(
            LayoutInflater.from(parent.context).inflate(R.layout.account_item_layout, parent, false)
        ) { position ->
            getItem(position)?.let { onItemClicked(it) }
        }

    object UserProfileComparator : DiffUtil.ItemCallback<UserProfile>() {
        override fun areItemsTheSame(
            oldItem: UserProfile,
            newItem: UserProfile
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: UserProfile,
            newItem: UserProfile
        ): Boolean {
            return oldItem == newItem
        }
    }
}