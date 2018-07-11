package alexey.tvshowapptest.viewmodels;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import alexey.rxnetwork.RxNetwork;
import alexey.tvshowapptest.utils.TestHelperUtils;
import alexey.tvshowapptest.data.api.MovieDBApi;
import alexey.tvshowapptest.data.viewmodels.MainScreenViewModel;
import alexey.tvshowapptest.testrules.SchedulersTestRule;
import alexey.tvshowapptest.views.fragments.View;
import io.reactivex.Observable;

import static org.mockito.Mockito.when;

/**
 * Created by Alexey Lyubchenko.
 */

public class MainViewModelTests {

    @ClassRule
    public static final SchedulersTestRule scheduleTestRule = new SchedulersTestRule();

    @Mock
    private MovieDBApi movieDBApiMock;

    @Mock
    private View viewMock;

    @Mock
    private Context contextMock;

    @Mock
    private NetworkInfo mockNetworkInfo;

    @Mock
    private ConnectivityManager connectivityManagerMock;

    private RxNetwork rxNetwork;
    private MainScreenViewModel mMainScreenViewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        rxNetwork = new RxNetwork(contextMock, connectivityManagerMock);
        mMainScreenViewModel = new MainScreenViewModel(contextMock, rxNetwork, movieDBApiMock);

        when(connectivityManagerMock.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.isConnected()).thenReturn(true);

        mMainScreenViewModel.attachView(viewMock);
    }

    private void mockGetPopularTvShowsResponse() {
        Mockito.when(movieDBApiMock
                .getPopularTvShows(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(Observable.just(TestHelperUtils.getPreparedResponseWithTvShows()));
    }


    @Test
    public void test_GetPopularTvShows_Succeed() {
        mockGetPopularTvShowsResponse();

        mMainScreenViewModel.fetchPopularTvShows();

        Assert.assertTrue(mMainScreenViewModel.getTvShows().size() > 0);
        Assert.assertEquals(mMainScreenViewModel.getTvShows(), TestHelperUtils.getPreparedResponseWithTvShows());

        InOrder inOrder = Mockito.inOrder(viewMock);
        inOrder.verify(viewMock).showLoading();
        inOrder.verify(viewMock).onDataLoadSucceed();
        inOrder.verify(viewMock).hideLoading();
    }

    @Test
    public void test_GetPopularTvShows_ExpectedError() {
        String error = "Expected error";

        Mockito.when(movieDBApiMock.getPopularTvShows(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(Observable.error(new Exception(error)));

        mMainScreenViewModel.fetchPopularTvShows();

        Assert.assertTrue(mMainScreenViewModel.getTvShows().size() == 0);

        InOrder inOrder = Mockito.inOrder(viewMock);
        inOrder.verify(viewMock).showLoading();
        inOrder.verify(viewMock).showMessage(error);
        inOrder.verify(viewMock).hideLoading();
    }


}
