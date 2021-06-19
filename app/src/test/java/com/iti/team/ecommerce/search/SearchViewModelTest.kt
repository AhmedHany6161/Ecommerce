package com.iti.team.ecommerce.search


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iti.team.ecommerce.getOrAwaitValue
import com.iti.team.ecommerce.model.data_classes.Images
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.remote.Result
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import com.iti.team.ecommerce.ui.search.SearchViewModel
import com.iti.team.ecommerce.ui.shop.FakeRepo
import com.iti.team.ecommerce.ui.shop.OfflineRepo
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.annotation.Config



@Config(sdk = [29])
@RunWith(AndroidJUnit4::class)
class SearchViewModelTest {

    @get:Rule
    var executer= InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepo: ModelRepo
    private lateinit var fakeRepository:FakeRepo
    private lateinit var offlineRepository:OfflineRepo
    private  var viewModel:SearchViewModel? = null

    @Before
    fun setup(){
        fakeRepository = FakeRepo(ApplicationProvider.getApplicationContext())
        offlineRepository = OfflineRepo()
        viewModel =  SearchViewModel(fakeRepository,offlineRepository)
    }

    @After
    fun free(){
        viewModel = null
    }

    @Test
    fun smartCollection_UpdateSmartCollection() {
       mockRepo =  ModelRepository(null,ApplicationProvider.getApplicationContext())
        // Given a mocked Context injected into the object under test...
        runBlocking {
           val result = mockRepo.smartCollection() as Result.Success

//            `when`(result.data?.smart_collections)
//                .thenReturn(smartCollectionList)

            assertThat(result.data?.smart_collections?.size, `is`(12))
        }

    }

    @Test
    fun getProductsByType_updateString(){

        viewModel?.getData("t-shirts","adidas")

        runBlocking {
            val result = fakeRepository.getProductsFromType("t-shirts")
            Assert.assertNotNull(result)
        }

    }

    @Test
    fun navigateToDetails_UpdateLiveData(){
        viewModel?.navigateToDetails(
            Products(
                11, "test", "http://image", "adidas",
                "170", "vendor", listOf(), listOf(), Images("testsImage")
            )
        )
        //when
        val value = viewModel?.navigateToDetails?.getOrAwaitValue()

        //result
        Assert.assertNotNull(value)
    }

    @Test
    fun shoesChipClicked_UpdateLiveData(){
        //when
         viewModel?.shoesChipClicked()

        val result = viewModel?.searchText?.getOrAwaitValue()

        Assert.assertNotNull(result)
    }

    @Test
    fun shirtChipClicked_UpdateLiveData(){
        //when
        viewModel?.shirtChipClicked()

        val result = viewModel?.searchText?.getOrAwaitValue()

        Assert.assertNotNull(result)
    }

    @Test
    fun accessoriesChipClicked_UpdateLiveData(){
        //when
        viewModel?.accessoriesChipClicked()

        val result = viewModel?.searchText?.getOrAwaitValue()

        Assert.assertNotNull(result)
    }
}