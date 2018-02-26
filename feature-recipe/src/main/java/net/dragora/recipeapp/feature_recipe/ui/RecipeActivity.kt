package net.dragora.recipeapp.feature_recipe.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.recipe_activity.recipe_image
import kotlinx.android.synthetic.main.recipe_activity.recipe_recycler
import kotlinx.android.synthetic.main.recipe_activity.recipe_toolbar
import net.dragora.recipeapp.base.ui.LokiActivity
import net.dragora.recipeapp.feature_recipe.R
import net.dragora.recipeapp.feature_recipe.di.DaggerRecipeComponent
import net.dragora.recipeapp.feature_recipe.presenter.RecipePresenter
import net.dragora.recipeapp.feature_recipe.presenter.RecipePresenter.ViewModel
import javax.inject.Inject

class RecipeActivity : LokiActivity(R.layout.recipe_activity), RecipePresenter.View {

    @Inject
    internal lateinit var presenter: RecipePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        DaggerRecipeComponent
                .builder()
                .inject(this)

        presenter.init(this)
    }

    private fun setupView() {
        setSupportActionBar(recipe_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this)
        recipe_recycler.layoutManager = layoutManager

        adapter = RecipeRowsAdapter()
        adapter.bindToRecyclerView(recipe_recycler)
    }

    override fun show(viewModel: ViewModel) {

        recipe_image.setImageURI(viewModel.imageUri)
        supportActionBar?.title = viewModel.name
        title = viewModel.name
        adapter.setNewData(viewModel.rows)
    }

    private lateinit var adapter: RecipeRowsAdapter
}
