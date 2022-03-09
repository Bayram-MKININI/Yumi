package net.noliaware.yumi.controllers

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import net.noliaware.yumi.R
import net.noliaware.yumi.model.DataManager
import net.noliaware.yumi.model.VOUCHERS_LIST_FRAGMENT_TAG
import net.noliaware.yumi.utils.handleErrorResponse
import net.noliaware.yumi.utils.inflate
import net.noliaware.yumi.views.CategoryItemView.CategoryItemViewAdapter
import net.noliaware.yumi.views.ProfileDataView.ProfileDataViewAdapter
import net.noliaware.yumi.views.ProfileView
import net.noliaware.yumi.views.ProfileView.ProfileViewAdapter
import net.noliaware.yumi.views.ProfileView.ProfileViewCallback

class ProfileFragment : Fragment() {

    private var profileView: ProfileView? = null

    private val profileFragmentViewModel: ProfileFragmentViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(context?.applicationContext as Application).create(ProfileFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.profile_layout, false)?.apply {
            profileView = findViewById(R.id.profile_view)
            profileView?.callback = profileViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpEvents()
        ProfileViewAdapter().also {
            createUserDataViewsFake(it)
            createUsedCategoriesViews(it)
            profileView?.fillViewWithData(it)
        }
        profileFragmentViewModel.callGetInfoWebservice()
    }

    private fun setUpEvents() {
        profileFragmentViewModel.dataLoaded.observe(viewLifecycleOwner) {
            createProfileViews()
        }

        profileFragmentViewModel.errorResponseLiveData.observe(viewLifecycleOwner) { errorResponse ->
            activity?.handleErrorResponse(errorResponse)
        }
    }

    private fun createProfileViews() {

        val dataManager = DataManager.get()

        ProfileViewAdapter().also {
            createUserDataViews(it, dataManager)
            createUsedCategoriesViews(it)
            profileView?.fillViewWithData(it)
        }
    }

    private fun createUserDataViews(
        profileViewAdapter: ProfileViewAdapter,
        dataManager: DataManager
    ) {

        val userData = dataManager.user

        ProfileDataViewAdapter(
            title = getString(R.string.surname),
            value = userData.lastName
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.name),
            value = userData.firstName
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.birth),
            value = getString(R.string.birth_data, userData.birthDate, userData.birthCity)
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.occupation),
            value = userData.job
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.phone_numbers),
            value = getString(R.string.phone_numbers_data, userData.phone1, userData.phone2)
        ).also {
            profileViewAdapter.complementaryDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.address),
            value = getString(
                R.string.address_data,
                userData.address1,
                userData.address2,
                userData.zipCode,
                userData.city
            )
        ).also {
            profileViewAdapter.complementaryDataAdapters.add(it)
        }
    }

    private fun createUserDataViewsFake(
        profileViewAdapter: ProfileViewAdapter
    ) {

        ProfileDataViewAdapter(
            title = getString(R.string.surname),
            value = "Bonnet"
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.name),
            value = "Marshall"
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.birth),
            value = "February 26, 1953"
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.occupation),
            value = "Eboueur"
        ).also {
            profileViewAdapter.myDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.phone_numbers),
            value = "February 26, 1953"
        ).also {
            profileViewAdapter.complementaryDataAdapters.add(it)
        }

        ProfileDataViewAdapter(
            title = getString(R.string.address),
            value = "42, rue du Clair Bocage 83500 LA SEYNE-SUR-MER"
        ).also {
            profileViewAdapter.complementaryDataAdapters.add(it)
        }
    }

    private fun createUsedCategoriesViews(profileViewAdapter: ProfileViewAdapter) {

        CategoryItemViewAdapter(
            count = 12,
            iconName = "ic_food",
            title = "Alimentaire"
        ).also {
            profileViewAdapter.categoryItemViewAdapters.add(it)
        }

        CategoryItemViewAdapter(
            count = 3,
            iconName = "ic_sports",
            title = "Sports"
        ).also {
            profileViewAdapter.categoryItemViewAdapters.add(it)
        }

        CategoryItemViewAdapter(
            count = 6,
            iconName = "ic_health",
            title = "Santé"
        ).also {
            profileViewAdapter.categoryItemViewAdapters.add(it)
        }

        CategoryItemViewAdapter(
            count = 15,
            iconName = "ic_services",
            title = "Services"
        ).also {
            profileViewAdapter.categoryItemViewAdapters.add(it)
        }

        CategoryItemViewAdapter(
            count = 2,
            iconName = "ic_energy",
            title = "Energie"
        ).also {
            profileViewAdapter.categoryItemViewAdapters.add(it)
        }

        profileFragmentViewModel.consumedCategoriesList.forEachIndexed { index, category ->

            //val categoryView = CategoryItemView(requireContext())

            /* categoryView.fillViewWithData(CategoryViewAdapter().apply {
                 title = category.categoryName
                 description = category.vouchersList.size.toString()
             })

             categoryView.setTag(R.string.view_tag_key, index)
             profileView.addChildView(categoryView)

             */
        }
    }

    private val profileViewCallback: ProfileViewCallback by lazy {
        object : ProfileViewCallback {

            override fun onCategoryClickedAtIndex(index: Int) {

                val fragmentTransaction = childFragmentManager.beginTransaction()
                val vouchersListFragment =
                    VouchersListFragment.newInstance(profileFragmentViewModel.consumedCategoriesList[index].categoryId)
                vouchersListFragment.show(fragmentTransaction, VOUCHERS_LIST_FRAGMENT_TAG)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        profileView = null
    }
}