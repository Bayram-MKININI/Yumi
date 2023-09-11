package net.noliaware.yumi.feature_categories.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import net.noliaware.yumi.feature_categories.data.repository.CategoryRepositoryImpl
import net.noliaware.yumi.feature_categories.domain.repository.CategoryRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class CategoriesModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindCategoryRepository(categoryRepository: CategoryRepositoryImpl): CategoryRepository
}