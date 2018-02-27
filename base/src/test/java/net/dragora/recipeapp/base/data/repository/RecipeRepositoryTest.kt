package net.dragora.recipeapp.base.data.repository

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import net.dragora.recipeapp.base.TestSchedulerRule
import net.dragora.recipeapp.base.data.network.IngredientPayload
import net.dragora.recipeapp.base.data.network.RecipeApiService
import net.dragora.recipeapp.base.data.network.RecipePayload
import net.dragora.recipeapp.base.data.repository.RecipeModelStorage.StorageExpired
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by luigipapino on 26/02/2018.
 */

class RecipeRepositoryTest {

    @get:Rule
    private val testSchedulerRule = TestSchedulerRule()

    private lateinit var recipeApiService: RecipeApiService

    private lateinit var storage: RecipeModelStorage

    private lateinit var repo: RecipeRepository

    @Before
    fun setup() {
        recipeApiService = mock()
        storage = mock()
        repo = RecipeRepository(recipeApiService, storage)
    }

    @Test
    fun testExpiredFetch() {
        val count = 10
        val payload = RecipePayloadFactory.list(count)
        whenever(storage.retrieve())
                .thenThrow(StorageExpired())
        whenever(recipeApiService.getRecipes())
                .thenReturn(Single.just(payload))

        val testObserver = repo.retrieveRecipes(emptyList()).test()

        testSchedulerRule.testScheduler.triggerActions()

        testObserver.awaitTerminalEvent()

        testObserver
                .assertValue { it.size == count }

        verify(storage).store(testObserver.values()[0])

    }

    @Test
    fun testCachedFetch() {
        val count = 10
        val models = RecipePayloadFactory.list(count).toModels()

        whenever(storage.retrieve())
                .thenReturn(models)

        val testObserver = repo.retrieveRecipes(emptyList()).test()

        testSchedulerRule.testScheduler.triggerActions()

        testObserver.awaitTerminalEvent()

        testObserver
                .assertValue { it.size == count }

        verify(storage).retrieve()
        verifyZeroInteractions(recipeApiService)

    }

}

internal object RecipePayloadFactory {

    fun list(count: Int): List<RecipePayload> {
        return (0 until count).map { indexBased(it) }
    }

    fun indexBased(index: Int): RecipePayload {
        val range = (0..index)
        val steps = range.map { "step $it" }
        val timers = range.map { it }
        val ingredients = range.map { IngredientPayload("qnt $it", "name $it", "$it") }
        return RecipePayload(
                "name $index",
                ingredients,
                steps,
                timers,
                "image $index",
                "original $index"
        )

    }
}